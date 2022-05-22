package ch.softenvironment.view;

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
import java.awt.Graphics;
import java.awt.Image;

/**
 * Basic javax.swing.JPanel.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class BasePanel extends javax.swing.JPanel {

	private Image image = null;

	/**
	 * BasePanel constructor comment.
	 */
	public BasePanel() {
		super();
	}

	/**
	 * BasePanel constructor comment.
	 *
	 * @param layout java.awt.LayoutManager
	 */
	public BasePanel(java.awt.LayoutManager layout) {
		super(layout);
	}

	/**
	 * BasePanel constructor comment.
	 *
	 * @param layout java.awt.LayoutManager
	 * @param isDoubleBuffered boolean
	 */
	public BasePanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	/**
	 * BasePanel constructor comment.
	 *
	 * @param isDoubleBuffered boolean
	 */
	public BasePanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	/**
	 * @see BaseFrame#genericPopupDisplay(..)
	 */
	protected void genericPopupDisplay(java.awt.event.MouseEvent event, javax.swing.JPopupMenu popupMenu) {
		BaseFrame.popupDisplay(this, event, popupMenu);
	}

	/**
	 * Convenience method.
	 */
	protected String getResourceString(String propertyName) {
		return ResourceManager.getResource(this.getClass(), propertyName);
	}

	/**
	 * Popup an error Dialog.
	 *
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {
		BaseFrame.showException(null, exception);
	}

	/**
	 * Display given image as Background-Image on Panel.
	 *
	 * @param path
	 * @see #paintComponent()
	 */
	public void setImage(Image image) {
		// image = Toolkit.getDefaultToolkit().createImage(path);
		this.image = image;
	}

	/**
	 * @see #setImage()
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			/*
			 * int w = getWidth(); int h = getHeight(); int x = (w -
			 * imageWidth)/2; int y = (h - imageHeight)/2;
			 */
			g.drawImage(image, (int) (g.getClipBounds().getCenterX() - (image.getWidth(this) / 2.0)),
				(int) (g.getClipBounds().getCenterY() - (image.getHeight(this) / 2.0)), this);
		}
	}
}
