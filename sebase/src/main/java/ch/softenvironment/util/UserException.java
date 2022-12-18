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

import ch.softenvironment.client.ResourceManager;

/**
 * Show failures within Application a User must know.
 *
 * @author Peter Hirzel
 */
public class UserException extends DeveloperException {

	/**
	 * Create a new User relevant exception.
	 *
	 * @param message Detailed description of failure
	 * @param title Short title of failure
	 * @param e Original exception
	 */
	public UserException(String message, String title, Throwable e) {
		super(message, (title == null ? ResourceManager.getResource(UserException.class, "CTApplicationError") : title), e);
	}

	/**
	 * @see #UserException(String, String, Throwable)
	 */
	public UserException(String message, String title) {
		this(message, (title == null ? ResourceManager.getResource(UserException.class, "CTApplicationError") : title), null);
	}
}
