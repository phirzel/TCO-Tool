package org.tcotool.tools;

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

import ch.softenvironment.util.Tracer;
import ch.softenvironment.util.UserException;
import java.util.ArrayList;
import org.tcotool.model.Course;
import org.tcotool.model.Currency;

/**
 * Utility to calculate currency course transformations.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class CourseUtils {

	/**
	 * Return the targetAmount, which is: targetAmount = sourceAmount * factor
	 *
	 * @see #getCourse(Currency, Currency)
	 */
	public static double getAmount(ModelUtility utility, double sourceAmount, Currency source, Currency target) throws UserException {
		return sourceAmount * getCourse(utility, source, target);
	}

	/**
	 * Return a course between source- and target-Currency.
	 *
	 * @param source
	 * @param target
	 * @return
	 * @throws UserException
	 */
	public static double getCourse(ModelUtility utility, Currency source, Currency target) throws UserException {
		// TODO NLS
		if ((source == null) || (target == null)) {
			throw new IllegalArgumentException("source AND target must not be null");
		}

		if (source.equals(target)) {
			// no transformation at all
			return 1.0;
		}

		java.util.List<Course> list = null;
		try {
			list = utility.getSystemParameter().getCourse();
		} catch (Exception e) {
			Tracer.getInstance().runtimeWarning("SystemParameter fault <Course>: " + e.getLocalizedMessage());
			list = new ArrayList<Course>();
		}
		java.util.Iterator<Course> it = list.iterator();
		while (it.hasNext()) {
			Course course = it.next();
			if (source.equals(course.getSource()) && target.equals(course.getTarget())) {
				// straight
				return course.getFactor().doubleValue();
			}
			if (source.equals(course.getTarget()) && target.equals(course.getSource())) {
				// 1/x
				return 1.0 / course.getFactor().doubleValue();
			}
		}

		throw new UserException("No course defined for <" + source.getNameString() + "> to <" + target.getNameString() + ">", "Course missing");
	}
}
