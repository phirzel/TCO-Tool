package ch.softenvironment.jomm.demoapp;
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

import ch.softenvironment.jomm.DbObjectIdTestCase;
import ch.softenvironment.jomm.implementation.DbPropertyChangeTestCase;
import ch.softenvironment.jomm.implementation.DbStateTestCase;
import ch.softenvironment.jomm.serialize.XmlSerializerTestCase;
import ch.softenvironment.jomm.tools.DbDataGeneratorTestCase;
import ch.softenvironment.jomm.tools.LoginValidatorTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Define a set/suite of TestCases to testsuite JOMM independent of a specific Target-System.
 *
 * @author Peter Hirzel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DbStateTestCase.class,
        DbPropertyChangeTestCase.class,
        DbObjectIdTestCase.class,
        DbDataGeneratorTestCase.class,
        XmlSerializerTestCase.class,
        LoginValidatorTestCase.class
})
public class DBIndependentTestSuite {
}
