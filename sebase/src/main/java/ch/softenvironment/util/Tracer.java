package ch.softenvironment.util;

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
 */

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.LogEvent;
import ch.ehi.basics.logging.LogListener;
import ch.ehi.basics.logging.StdLogEvent;
import java.io.PrintStream;

/**
 * This is a <b>Developer Tracing-Tool</b>, meant to trace important info a Developer should be aware of <b>while developing and testing an application</b>. Except of the runtime-logs the API of this
 * Tool should not be misunderstood as a User-Log Tool, rather hide log-info created by this tool from user.
 * <p>
 * Eventually this Tool could help in a productive environment for remote assistance, by starting it in a command-shell only (the application parameters should forward any start-parameters to start a
 * Tracer-Instance.
 * <p>
 * Design Pattern: Singleton
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see ch.ehi.basics.logging.EhiLogger (underlying logger)
 * @deprecated replace by SLF4J
 */
@Deprecated(since = "1.6.0")
public class Tracer implements LogListener {
	// Mode's
	/**
	 * Suppress all Trace-logs.
	 */
	public final static int SILENT = 1;
	/**
	 * Show User-relevant logs.
	 */
	public final static int NORMAL = 2;
	/**
	 * Show Developer relevant info (suppress in productive releases).
	 */
	public final static int DEBUG = 3;
	/**
	 * Show back-end commands.
	 */
	public final static int TRACE_BACKEND = 4;
	/**
	 * Show all logs.
	 */
	public final static int ALL = 5;

	// customer log levels
	private static final int SE_DEVELOPER_ERROR = 100;
	private static final int SE_DEVELOPER_WARNING = 101;

	private static Tracer instance = null; // default instance
	private java.io.PrintStream outStream = null;
	private int mode = SILENT;

	/**
	 * Tracer constructor.
	 *
	 * @see #start(int)
	 */
	private Tracer() {
		super();
	}

	/**
	 * Log a debug message, for e.g. to help a developer to trace its code without using a Debugger. This method is meant for temporarily use only, be means any debug-logs should be removed after
	 * testing or within production releases.
	 */
	public void debug(String message) {
		// EhiLogger.debug(message);
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.DEBUG_TRACE, message, null, getOrigin(0)));
	}

	/**
	 * Log Developer Errors, for e.g. when a developer misunderstood the concept of a called method/mechanism. If this happens, the developer is urged to change this part of code immediately.
	 */
	public void developerError(String error) {
		// EhiLogger.logError(error, SE_DEVELOPER_ERROR);
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR, error, null, getOrigin(0), SE_DEVELOPER_ERROR));
	}

	/**
	 * Log developer Warnings, for e.g. if something was wrongly used by Developer, but work around has taken place automatically. The developer should change its code as soon as possible if this
	 * trace-log happens to remain compatible for future releases.
	 */
	public void developerWarning(String warning) {
		// EhiLogger.traceUnusualState(warning, SE_DEVELOPER_WARNING);
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.UNUSUAL_STATE_TRACE, warning, null, getOrigin(0), SE_DEVELOPER_WARNING));
	}

	/**
	 * @return console-error Stream
	 */
	private static java.io.OutputStream getConsoleError() {
		return System.err;
	}

	/**
	 * Design Pattern: Singleton
	 *
	 * @return Tracer.
	 */
	public static Tracer getInstance() {
		if (instance == null) {
			// do not influence EhiLogger defaults
			instance = new Tracer();
			instance.setMode(NORMAL);
		}
		return instance;
	}

	private void log(String kind, String message, String origin) {
		try {
			outStream.println(/*
			 * NlsUtils.formatDateTime(new java.util.Date()) +
			 * ">" +
			 */kind + ": " + message + (StringUtils.isNullOrEmpty(origin) ? "" : " [" + origin + "]"));
		} catch (Throwable e) {
			System.err.println("Tracer#log(): " + kind + ": " + message + "=>" + e.getLocalizedMessage());
		}
	}

	/**
	 * Log a command executed on a back-end target (for e.g. a DBMS or any other Server-System).
	 *
	 * @param command the remotely executed statement on Target-System
	 */
	public void logBackendCommand(String command) {
		// EhiLogger.traceBackendCmd(command);
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.BACKEND_CMD, command, null, null));
	}

	/**
	 * @see #runtimeError(String, Throwable)
	 */
	public void runtimeError(String error) {
		runtimeError(error, null);
	}

	/**
	 * Log Errors during runtime, by means a serious error happened, which might corrupt application behaviour sooner or later.
	 */
	public void runtimeError(String error, Throwable exception) {
		// EhiLogger.logError(error);
		// EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,
		// error == null ? "Unexpected error" : error, exception,
		// getOrigin(0)));
		runtimeError(error, exception, 1);
	}

	/**
	 * Log Errors during runtime, by means a serious error happened, which might corrupt application behaviour sooner or later.
	 * <p>
	 * Useful for intermediate Trace-layers.
	 *
	 * @param offsetToStackTrace (0 for calling method; 1 for one method earlier)
	 */
	public void runtimeError(String error, Throwable exception, int offsetToStackTrace) {
		// EhiLogger.logError(error);
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR, error == null ? "Unexpected error" : error, exception, getOrigin(offsetToStackTrace)));
	}

	/**
	 * Log informations during runtime, by means this may represent a valuable info which might help to interpret application behaviour.
	 */
	public void runtimeInfo(String info) {
		// log("Info:", source, methodName, info);
		// EhiLogger.logState(info);
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.STATE, info, null, getOrigin(0)));
	}

	/**
	 * Log Warnings during runtime, by means errors might have been happened but were handled automatically. However any other follow-up errors might be expected.
	 */
	public void runtimeWarning(String warning) {
		// log("Runtime Warning:", source, methodName, warning);
		// EhiLogger.logAdaption(warning);
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.ADAPTION, warning, null, getOrigin(0)));
	}

	/**
	 * Start Tracer and use Console-Error. The command line args as followed are filtered out of args by this method: - "-all" - "-silent" - "-trace" - "traceSQL" - "-debug")
	 *
	 * @param args
	 */
	public static java.lang.String[] start(java.lang.String[] args) {
		int mode = SILENT; // default
		if (args != null) {
			java.util.List<String> ret = new java.util.ArrayList<String>(java.util.Arrays.asList(args));
			java.util.Iterator<String> it = ret.iterator();
			while (it.hasNext()) {
				String option = it.next();
				if (option.equalsIgnoreCase("-debug")) {
					mode = DEBUG;
					it.remove();
					break;
				} else if (option.equalsIgnoreCase("-trace")) {
					mode = NORMAL;
					it.remove();
					break;
				} else if (option.equalsIgnoreCase("-traceSQL")) {
					mode = TRACE_BACKEND;
					it.remove();
					break;
				} else if (option.equalsIgnoreCase("-all")) {
					mode = ALL;
					it.remove();
					break;
				}
			}
			start((java.io.PrintStream) getConsoleError(), mode);
			return ret.toArray(new String[0]);
		} else {
			start((java.io.PrintStream) getConsoleError(), mode);
			return null;
		}
	}

	/**
	 * Start Tracer and use Console-Error.
	 *
	 * @see #start(PrintStream, int)
	 */
	public static void start(int mode) {
		start((java.io.PrintStream) getConsoleError(), mode);
	}

	/**
	 * Start Tracer.
	 *
	 * @param stream Outstream for logging
	 * @param mode
	 * @see #setMode(int)
	 */
	public static synchronized void start(java.io.PrintStream stream, int mode) {
		if (instance != null) {
			throw new DeveloperException("Default Tracer-instance already initialized!");
		}
		instance = new Tracer();
		instance.outStream = stream;
		instance.setMode(mode);

		EhiLogger.getInstance().addListener(getInstance());
		EhiLogger.getInstance().removeListener(ch.ehi.basics.logging.StdListener.getInstance());

		// write startup info directly to log-stream
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE, "Java Version: " + System.getProperty("java.version"), null, null));
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE, "Java VM Version: " + System.getProperty("java.vm.version"), null, null));
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE, "OS Name: " + System.getProperty("os.name"), null, null));
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE, "OS Architecture: " + System.getProperty("os.arch"), null, null));
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE, "OS Version: " + System.getProperty("os.version"), null, null));
		EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE, "OS Locale: " + java.util.Locale.getDefault().toString(), null, null));
	}

	/**
	 * Change the filter-level for logging messages.
	 *
	 * @param mode (SILENT, NORMAL, DEBUG, TRACE_BACKEND, ALL)
	 */
	public void setMode(final int mode) {
		this.mode = mode;
		EhiLogger.getInstance().setTraceFilter((mode == SILENT) || (mode == NORMAL));
	}

	/**
	 * Stop Tracer.
	 */
	public synchronized void stop() {
		try {
			if (outStream != null) {
				outStream.close();
			}
			EhiLogger.getInstance().removeListener(this);
		} catch (Throwable e) {
			System.err.println("Tracer#stop(): " + e.getLocalizedMessage());
		}
		// Bad practice to write static variable
		instance = null;
	}

	/**
	 * Use this message to trace non-visually handled exceptions which might be ignored during application run. Serious malfunctioning application behaviour might be possible.
	 */
	public void uncaughtException(Throwable exception) {
		try {
			// exception.printStackTrace(outStream);
			// log("Uncaught Exception:", source, methodName,
			// exception.toString());
			EhiLogger.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR, "Uncaught exception", exception, getOrigin(0)));
		} catch (Throwable e) {
			System.err.println("Tracer#uncaughtException(): " + e.getLocalizedMessage());
		}
	}

	/**
	 * Correct the stack-trace origin which is one method later in EhiLogger convenience methods.
	 *
	 * @return
	 */
	protected static StackTraceElement getOrigin(int offset) {
		StackTraceElement[] stack = (new Throwable()).getStackTrace();
		// stack[0]: #getOrigin()
		// stack[1]: for e.g. #logError()
		// stack[2]: #myUserTracePoint()
		if ((2 + offset) < stack.length) {
			StackTraceElement st = stack[2 + offset];
			return st;
		}
		return null;
	}

	/**
	 * Listener method.
	 *
	 * @see EhiLogger
	 */
	@Override
	public void logEvent(LogEvent event) {
		if (mode != ALL) {
			if (mode == SILENT) {
				return;
			} else if ((event.getEventKind() == LogEvent.DEBUG_TRACE) || (event.getEventKind() == LogEvent.STATE_TRACE) || (event.getEventKind() == LogEvent.UNUSUAL_STATE_TRACE) || (
				event.getCustomLevel() == SE_DEVELOPER_ERROR)
				|| (event.getCustomLevel() == SE_DEVELOPER_WARNING)) {
				if (mode != DEBUG) {
					return;
				}
			} else if (event.getEventKind() == LogEvent.BACKEND_CMD) {
				if (mode != TRACE_BACKEND) {
					return;
				}
			}
		}

		String kind = EhiLogger.kindToString(event.getEventKind());
		if (event.getCustomLevel() != LogEvent.LEVEL_UNDEFINED) {
			switch (event.getCustomLevel()) {
				case LogEvent.LEVEL_UNDEFINED:
					// as is
					break;
				case SE_DEVELOPER_ERROR:
					kind = kind + "<Developer-Error>";
					break;
				case SE_DEVELOPER_WARNING:
					kind = kind + "<Developer-Warning>";
					break;
				default:
					kind = kind + "<CUSTOM-LEVEL[" + event.getCustomLevel() + "]>";
					break;
			}
		}

		log(kind, event.getEventMsg(), formatOrigin(event.getOrigin()));
		if (event.getException() != null) {
			// @see ch.ehi.basics.logging.AbstractStdListener#logThrowable()
			event.getException().printStackTrace(outStream);
		}
	}

	/**
	 * Format the source triggering method.
	 *
	 * @param st
	 * @return "MyFile.java#myMethod():line"
	 */
	protected static String formatOrigin(StackTraceElement st) {
		StringBuffer origin = new StringBuffer();
		if (st != null) {
			if (st.getFileName() != null) {
				origin.append(st.getFileName());
			} else {
				origin.append(st.getClassName());
			}
			origin.append("#");
			origin.append(st.getMethodName());
			origin.append("()");
			int line = st.getLineNumber();
			if (line >= 0) {
				origin.append(":");
				origin.append(line);
			}
		}
		return origin.toString();
	}
}
