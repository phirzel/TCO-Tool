package ch.softenvironment.jomm.tools;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.jomm.serialize.HtmlSerializer;
import ch.softenvironment.math.MathUtils;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import ch.softenvironment.util.UserException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Keep statistic about logged in users of a "application" context for e.g. a multi-User Web-Application.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class LoginValidator {

    public static int MAX_LOGIN_INFINITE = -1;
    private static LoginValidator validator = null;

    private int login = 0;
    private int logoutByUser = 0;
    private int logoutByTimeout = 0;
    private volatile int concurrentMax = 0;
    private volatile int concurrentNow = 0;
    private volatile int exceededLogins = 0;
    private final java.util.Map<String, Integer> concurrentUsers = new java.util.HashMap<String, Integer>();
    private final java.util.List<HistoryEntry> log = new java.util.ArrayList<HistoryEntry>();

    /**
     * Login or Logout record data.
     */
    private static class HistoryEntry {

        private static final int LOGIN = 10;
        private static final int LOGIN_EXCEEDED = 11; // too many logins
        private static final int LOGOUT_BY_USER = 20;
        private static final int LOGOUT_BY_TIMEOUT = 21;

        private String userId = null;
        private int kind = -1;
        private java.util.Date timestamp = null;
        private String sessionId = null;

        private HistoryEntry(java.util.Date date, final int kind, final String userId, final String sessionId) {
            super();
            this.kind = kind;
            this.userId = userId;
            this.sessionId = sessionId;
            this.timestamp = date;
        }
    }

    /**
     * Singleton per Web-Application to control Login/logout statistics..
     *
     * @return
     */
    public synchronized static LoginValidator getInstance() {
        if (validator == null) {
            Tracer.getInstance().runtimeInfo("LoginValidator Singleton created");
            validator = new LoginValidator();
            //validator.start = new java.util.Date();
        }
        return validator;
    }

    /**
     * Method just for synchronization of currentLogins. Return whether login is ok; Logout is always ok
     *
     * @param userId
     * @param maxLogins
     * @return
     */
    private synchronized boolean maxLogin(final String userId, boolean login, final int maxLogins) {
        int currentLogins = 0;
        if (concurrentUsers.containsKey(userId)) {
            currentLogins = concurrentUsers.get(userId).intValue();
        }
        if (login) {
            if ((maxLogins != MAX_LOGIN_INFINITE) && (currentLogins == maxLogins)) {

                return true;
            } else {
                concurrentUsers.put(userId, Integer.valueOf(++currentLogins));
            }
        } else {
            // logout
            if (--currentLogins < 0) {
                Tracer.getInstance().developerError("User already logged out: " + userId + " (evtl. bad Browser navigation)");
                currentLogins = 0;
            }
            concurrentUsers.put(userId, Integer.valueOf(currentLogins));
        }
        return false;
    }

    /**
     * Try to login given userId, which might be restricted by parallel login maximum.
     *
     * @param userId
     * @param maxLogins number of parallel logins allowed (or MAX_LOGIN_INFINITE)
     */
    public void login(final String userId, final String sessionId, final int maxLogins) throws UserException {
        login(new java.util.Date(), userId, sessionId, maxLogins);
    }

    private synchronized void login(java.util.Date date, final String userId, final String sessionId, final int maxLogins) throws UserException {
        try {
            if (StringUtils.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("UserId must not be null");
            }

            boolean exceeded = false;
            if (maxLogin(userId, true, maxLogins)) {
                exceededLogins++;
                exceeded = true;
                Tracer.getInstance().runtimeInfo("MaxLogin exceeded for: " + userId + " (max.=" + maxLogins + ")");

                // reset -1 after session-timeout will reduce login
                //TODO
/*   
                   
                throw new UserException(ResourceManager.getResource(LoginFormConstants.class, "CWLoginTooManyLogins", Locale.GERMAN) + "/" + ResourceManager.getResource(LoginFormConstants.class, "CWLoginTooManyLogins", Locale.FRENCH) + ": " + maxLogins, 
                    ResourceManager.getResource(LoginFormConstants.class, "CELoginError", Locale.GERMAN) + "/" + ResourceManager.getResource(LoginFormConstants.class, "CELoginError", Locale.FRENCH));
*/
            }

            login++;
            if (++concurrentNow > concurrentMax) {
                concurrentMax = concurrentNow;
            }

            // OK to allow secured context
            log.add(new HistoryEntry(date, (exceeded ? HistoryEntry.LOGIN_EXCEEDED : HistoryEntry.LOGIN), userId, sessionId));
        } catch (Exception e) {
            Tracer.getInstance().runtimeError("login failed for User-Id: " + userId, e);
        }
    }

    /**
     * Will be called, when user logs out from Tomcat-Server.
     *
     * @param userId
     * @param sessionId
     * @param regular true->triggered by user; false->forced by Session-Timout (or browser abortion)
     * @see logout.jsp
     */
    public void logout(final String userId, final String sessionId, boolean regular) {
        logout(new java.util.Date(), userId, sessionId, regular);
    }

    private synchronized void logout(java.util.Date date, final String userId, final String sessionId, boolean regular) {
        try {
            if (regular) {
                logoutByUser++;
            } else {
                logoutByTimeout++;
            }
            concurrentNow--;
            maxLogin(userId, false, -1 /*irrelevant*/);
            log.add(new HistoryEntry(date, (regular ? HistoryEntry.LOGOUT_BY_USER : HistoryEntry.LOGOUT_BY_TIMEOUT), userId, sessionId));
        } catch (Exception e) {
            Tracer.getInstance().runtimeError("logout problem for user: " + userId, e);
        }
    }

    /**
     * Statistics of User-Logging (as HTML stream).
     *
     * @return
     */
    public synchronized String getStatistics() {
        try {
            if (concurrentUsers.size() == 0) {
                return HtmlSerializer.LESS_THAN + "none" + HtmlSerializer.GREATER_THAN;
            } else {
                java.util.Date firstLogin = null;
                java.util.Date currentDate = null;
                Iterator<HistoryEntry> it = log.iterator(); // sorted by time (Ascending) here!
                StringBuffer dailyJournal = new StringBuffer(); // log per day and users (for all days in day sequence)
                while (it.hasNext()) {
                    // calc statistics
                    HistoryEntry entry = it.next();
                    if (firstLogin == null) {
                        firstLogin = entry.timestamp;
                        currentDate = entry.timestamp;
                        // get journal for first day
                        dailyJournal.append(printDay(currentDate));
                    } else if ((entry.timestamp.getDate() != currentDate.getDate()) || (entry.timestamp.getMonth() != currentDate.getMonth()) || (entry.timestamp.getYear() != currentDate.getYear())) {
                        // get journal for next day
                        currentDate = entry.timestamp;
                        dailyJournal.append(printDay(currentDate));
                    }
                }
                StringBuffer buffer = new StringBuffer();
                buffer.append("<h3>Overall</h3>");
                buffer.append("<table cellpadding='0' cellspacing='0'>");
                buffer.append("<tr><td><b>User-ID total:</b></td><td><b>" + concurrentUsers.size() + "</b></td>");
                buffer.append("<tr><td><b>Logins total:</b></td><td><b>" + login + "</b>; Logouts total: " + (logoutByUser + logoutByTimeout) + " (by timeout: " + logoutByTimeout + ")" + "</td>");
                buffer.append("<tr><td>Logins (> max. allowed):</td><td>" + exceededLogins + "</td>");
                buffer.append("<tr><td><b>Concurrent max:</b></td><td><b>" + concurrentMax + "</b></td>");
                buffer.append("<tr><td>Concurrent now:</td><td>" + concurrentNow + "</td>");
                long time = (new java.util.Date()).getTime() - firstLogin.getTime();
                buffer.append("<tr><td>Up-time:</td><td>" + MathUtils.round(((double) time) / (60 * 60000.0 /*min.*/), 2) + " [hours since first login]</td>");
                buffer.append("<tr><td>Average login rate:</td><td>" + MathUtils.round(((double) time) / (60 * 60000.0 /*min.*/) / ((double) login), 5) + " [Logins per hour]</td>");
                buffer.append("</table>");

                //String tmp = buffer.toString();
                //tmp = StringUtils.replace(tmp, "<", HtmlSerializer.LESS_THAN);
                //tmp = StringUtils.replace(tmp, ">", HtmlSerializer.GREATER_THAN);
                //return StringUtils.replace(tmp, "\n", "<br>");
                return buffer.toString() + dailyJournal.toString();
            }
        } catch (Exception e) {
            Tracer.getInstance().runtimeError("Login-Statistic problem", e);
            return "Problem happened:\n" + e.getLocalizedMessage();
        }
    }

    /**
     * Filter all logins/logouts out of log history and create an overview per day globally and per user individually.
     *
     * @param currentDate
     * @return
     */
    private StringBuffer printDay(java.util.Date currentDate) {
        java.util.Map<String, java.util.List<HistoryEntry>> usersCurrentDate = new java.util.HashMap<String, java.util.List<HistoryEntry>>();

        // filter all logs per user and currentDate        
        Iterator<HistoryEntry> it = log.iterator();
        while (it.hasNext()) {
            // sort the log => map of logins/logouts per user for the given day
            HistoryEntry entry = it.next();
            if ((entry.timestamp.getDate() == currentDate.getDate()) && (entry.timestamp.getMonth() == currentDate.getMonth()) && (entry.timestamp.getYear() == currentDate.getYear())) {
                // log of the wanted day
                java.util.List<HistoryEntry> userHistory = null;
                if (usersCurrentDate.containsKey(entry.userId)) {
                    userHistory = usersCurrentDate.get(entry.userId);
                } else {
                    userHistory = new ArrayList<HistoryEntry>();
                    usersCurrentDate.put(entry.userId, userHistory);
                }
                userHistory.add(entry);
            }
        }

        // print each user after each other
        StringBuffer logCurrentDate = new StringBuffer();
        int dailyLogin = 0;
        int dailyLogoutByUser = 0;
        int dailyLogoutByTimeout = 0;
        // temporary info of iterated user
        StringBuffer userLog = null;
        String userId = null;
        int userLogin = 0;
        int userLoginExceeded = 0;
        int userLogoutByUser = 0;
        int userLogoutByTimeout = 0;
        Iterator<String> itUser = usersCurrentDate.keySet().iterator();
        while (itUser.hasNext()) {
            // print the specific log-entries per user in ascending time
            if (userLog != null) {
                logCurrentDate.append(printDayUser(userId, userLogin, userLoginExceeded, userLogoutByUser, userLogoutByTimeout, userLog));
            }
            userLog = new StringBuffer();
            userId = itUser.next();
            userLogin = 0;
            userLoginExceeded = 0;
            userLogoutByUser = 0;
            userLogoutByTimeout = 0;
            Iterator<HistoryEntry> itLog = usersCurrentDate.get(userId).iterator();
            while (itLog.hasNext()) {
                HistoryEntry entry = itLog.next();
                userLog.append("<tr>");
                //currentDate = entry.timestamp; 
                String kind = null;
                switch (entry.kind) {
                    case HistoryEntry.LOGIN:
                        kind = "Login";
                        dailyLogin++;
                        userLogin++;
                        break;
                    case HistoryEntry.LOGIN_EXCEEDED:
                        kind = "Login (> max. login!)";
                        dailyLogin++;
                        userLoginExceeded++;
                        break;
                    case HistoryEntry.LOGOUT_BY_USER:
                        kind = "Logout (by user)";
                        dailyLogoutByUser++;
                        userLogoutByUser++;
                        break;
                    case HistoryEntry.LOGOUT_BY_TIMEOUT:
                        kind = "Logout (by timeout)";
                        dailyLogoutByTimeout++;
                        userLogoutByTimeout++;
                        break;
                    default:
                        Tracer.getInstance().developerError("entry.kind unknown: " + kind);
                }
                userLog.append(/*"<td>" + "<b>" + entry.userId + "</b></td>" */
                    "<td>" + kind + "</td><td>" + NlsUtils.formatDateTime(entry.timestamp) + "</td><td>" + " (Session-Id: " + entry.sessionId + ")</td>");
                userLog.append("</tr>");
            }
        }
        if (userLog != null) {
            // very last user of iteration
            logCurrentDate.append(printDayUser(userId, userLogin, userLoginExceeded, userLogoutByUser, userLogoutByTimeout, userLog));
        }

        logCurrentDate.insert(0, "<h3>" + NlsUtils.formatDate(currentDate) + "</h3>" +
            "<p><table><tr><td>User-ID's:</td><td>" + usersCurrentDate.size() + "</td></tr>" +
            "<tr><td>Logins:</td><td>" + dailyLogin + "; Logouts: " + (dailyLogoutByUser + dailyLogoutByTimeout) + " (by timeout: " + dailyLogoutByTimeout + ")</td>" +
            "</table></p>");

        return logCurrentDate;
    }

    /**
     * Print user of last iteration.
     *
     * @return
     */
    private StringBuffer printDayUser(final String userId, final int userLogin, final int userLoginExceeded, final int userLogoutByUser, final int userLogoutByTimeout, StringBuffer userLog) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<h4>" + userId + "</h4>");
        buffer.append("<p>Logins: " + userLogin + " (> max. Logins: " + userLoginExceeded + "); Logouts: " + (userLogoutByUser + userLogoutByTimeout) + " (by timeout: " + userLogoutByTimeout
            + "); concurrent-session: " + (userLogin + userLoginExceeded - userLogoutByUser - userLogoutByTimeout) + " (only for current day above!)</p>");
        buffer.append("<p><table>");
        buffer.append(userLog);
        buffer.append("</table></p>");
        return buffer;
    }
    /**
     * Testcase for #getStatistics()
     * @return public static LoginValidator testStatistics() {
    LoginValidator validator = new LoginValidator();
    validator.login(new java.util.Date(107, 4, 18), "00276", null, MAX_LOGIN_INFINITE);
    validator.login(new java.util.Date(107, 4, 18), "00111", null, MAX_LOGIN_INFINITE);
    validator.logout(new java.util.Date(107, 4, 18), "00276", null, true);
    validator.logout(new java.util.Date(107, 4, 18), "00111", null, false);
    validator.login(new java.util.Date(107, 4, 22), "00276", null, MAX_LOGIN_INFINITE);
    validator.logout(new java.util.Date(107, 4, 22), "00276", null, true);
    validator.login(new java.util.Date(107, 4, 25), "00276", null, MAX_LOGIN_INFINITE);
    validator.logout(new java.util.Date(107, 4, 25), "00276", null, true);
    validator.login(new java.util.Date(107, 4, 25), "00276", null, MAX_LOGIN_INFINITE);
    validator.logout(new java.util.Date(107, 4, 25), "00276", null, false);
    validator.login(new java.util.Date(107, 5, 1), "00276", null, MAX_LOGIN_INFINITE);
    validator.logout(new java.util.Date(107, 5, 1), "00276", null, false);
    validator.login(new java.util.Date(107, 5, 3), "00276", null, MAX_LOGIN_INFINITE);
    validator.logout(new java.util.Date(107, 5, 3), "00276", null, true);
    validator.login(new java.util.Date(107, 5, 9), "00276", null, MAX_LOGIN_INFINITE);
    validator.logout(new java.util.Date(107, 5, 9), "00276", null, true);
    return validator;
    }
     */
}