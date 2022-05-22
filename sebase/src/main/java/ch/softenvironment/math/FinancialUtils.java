package ch.softenvironment.math;

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

/**
 * Utility to calculate financial matters
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class FinancialUtils {

    /**
     * Calc interest for given Capital according to the following formula: Interest-Amount = (Capital * InterestRate * Time) / 100 [where: Time=1 Year]
     *
     * @param capital
     * @return interest of capital for a year
     */
    public static double calcInterest(double capital, double interestRate) {
        if (interestRate < 0.0) {
            throw new IllegalArgumentException("Interest-Rate must be >= 0.0");
        }
        return capital * interestRate / 100.0;
    }

    /**
     * Als Abschreibung wird der betriebswirtschaftlich zu ermittelnde Wertverlust des Produktionsvermoegens (Anlagevermoegen und Umlaufvermoegen) bezeichnet. Hierbei ist es unerheblich, aus welchem
     * Grund (Abnutzung, Alterung oder Unfallschaden) dieser Wertverlust herbeigefuehrt wird.
     *
     * <b>Lineare Abschreibung:</b> Die Anschaffungskosten des abzuschreibenden
     * Wirtschaftsgutes (WG) werden gleichmaessig auf die Jahre der Nutzungsdauer aufgeteilt. Damit wird jedes Jahr der gleiche Betrag abgeschrieben. Am Ende der Nutzungsdauer ist das WG vollstaendig
     * abgeschrieben, es sei denn, das WG wird nur bis auf den Schrottwert abgeschrieben. Dann wird der Differenzbetrag aus Anschaffungskosten und Schrottwert abgeschrieben. Der Abschreibungswert wird
     * mit der folgenden Formel berechnet: Konstanter Abschreibungswert = Anschaffungswert / Nutzungsdauer
     *
     * @param duration (de: Nutzungdauer)
     * @param timeOfInterest (same UNIT as duration, for e.g. month or year)
     * @param capital
     * @return (de : Buchwert resp. Restwert im Jahr ( timeOfInterest))
     */
    public static double calcDepreciationLinear(double capital, int duration, int timeOfInterest) {
        double r = capital / ((double) duration); // Abschreibungsbetrag
        double value = capital - timeOfInterest * r; // Wert nach Abschreibung
        if (value < 0.0) {
            return 0.0; // TODO evtl. 1.0 ???
        } else {
            return value;
        }
    }

    /**
     * <b>Geometrisch-degressive Abschreibung</b> Im Anschaffungsjahr wird ein
     * bestimmter Prozentsatz von Anschaffungskosten des Wirtschaftsgutes (WG) festgelegt und abgeschrieben. In den darauffolgenden Jahren wird dieser festgelegte Prozentsatz von dem noch uebrigen
     * Wert des WG (Restbuchwert) abgeschrieben. Der Abschreibungsbetrag wird bei dieser Methode folglich immer kleiner. Das WG ist am Ende der geplanten Nutzungsdauer nicht vollstaendig
     * abgeschrieben.
     * <p>
     * Formula (see http://www.harri-deutsch.de/verlag/titel/pfeifer/k01_1736.pdf) capital[year] = capital[year=0] * (1 - i)^[year]
     *
     * @param capital initial Investment at year=0
     * @param interestRatePercent (de: %-Satz der jaehrlich vom Buchwert abgeschrieben werden soll)
     * @param yearOfInterest
     * @return Buchwert im Jahr(yearOfInterest)
     */
    public static double calcDepreciationGeometricDegressive(double capital, double interestRatePercent, int yearOfInterest) {
        double i = interestRatePercent / 100.0; // Zinssatz
        /*
         * //Jaehrlicher Abschreibungsbetrag r[year] = capital[year] *
         * (1-i)^(year-1) * i;
         */
        return capital * Math.pow(1 - i, yearOfInterest);
    }
}
