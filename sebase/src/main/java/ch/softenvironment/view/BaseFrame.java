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

import ch.ehi.basics.view.BrowserControl;
import ch.ehi.basics.view.FileChooser;
import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.ParserCSV;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.MissingResourceException;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import lombok.extern.slf4j.Slf4j;

/**
 * TemplateFrame defining minimal functionality.
 *
 * @author Peter Hirzel
 */

@Slf4j
public abstract class BaseFrame extends javax.swing.JFrame {

	// Relative Offset to Child Window
	public final static int X_CHILD_OFFSET = 50;
	public final static int Y_CHILD_OFFSET = 50;

	private ViewOptions viewOptions = null;
	private java.util.List<?> objects = null; // for DetailView's

	/**
	 * BaseFrame constructor comment.
	 */
	public BaseFrame() {
		super();
	}

	/**
	 * BaseFrame constructor comment.
	 */
	public BaseFrame(ViewOptions viewOptions) {
		super();

		this.viewOptions = viewOptions;
	}

	/**
	 * Typical DetailView Constructor.
	 *
	 * @param viewOptions Special options valid for all GUI's in an application
	 * @param objects Model-instances to be represented by this GUI
	 */
	public BaseFrame(ViewOptions viewOptions, java.util.List<?> objects) {
		super();

		this.viewOptions = viewOptions;
		this.objects = objects;
	}

	/**
	 * BaseFrame constructor comment.
	 *
	 * @param title java.lang.String
	 */
	public BaseFrame(String title) {
		super(title);
	}

	/**
	 * Close this View after save.
	 *
	 * @see DetailView#saveObject()
	 */
	protected void closeOnSave() {
		if ((getViewOptions() == null) || getViewOptions().getCloseOnSave()) {
			if (getObjects().size() == 1) {
				dispose();
			} // else other model-instances may be changed by same view
		}
	}

	/**
	 * Extend the given menu generically with platform dependent Look&Feel styles.
	 *
	 * @see #setLookAndFeel(String)
	 */
	protected final void createLookAndFeelMenu(JMenu lookAndFeelMenu) {
		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		JMenuItem menuItem = null;

		ButtonGroup buttons = new ButtonGroup();
		for (int i = 0; i < lafs.length; i++) {
			// create generic MenuItem-Button
			menuItem = new JRadioButtonMenuItem(lafs[i].getName());
			buttons.add(menuItem);
			final String lnfClassName = lafs[i].getClassName();
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					setLookAndFeel(lnfClassName);
				}
			});
			lookAndFeelMenu.add(menuItem);
		}
	}

	/**
	 * @see #setVisible(boolean)
	 */
	@Override
	public void dispose() {
		if (getViewOptions() != null) {
			// forget about this reminded GUI
			getViewOptions().getViewManager().checkOut(this);
		}

		super.dispose();
	}

	/**
	 * Overwrite the #dispose() method for <b>Launcher's containing a #main()</b> of any Application extending this BaseFrame. This call will shutdown Application and Java virtual machine by a
	 * System#exit(). // Overwrites public void dispose() { // super.dispose(); => WILL BE CALLED BY #disposeApplication() disposeApplication(); }
	 *
	 * see LauncherView#dispose()
	 */
	protected void disposeApplication() {
		super.dispose();
		System.exit(0);
	}

	/**
	 * Converts the stack trace into a string.
	 */
	public static String exceptionToString(Throwable exception) {
		java.io.StringWriter stringWriter = new java.io.StringWriter();
		java.io.PrintWriter writer = new java.io.PrintWriter(stringWriter);
		exception.printStackTrace(writer);
		return stringWriter.toString();
	}

	/**
	 * Convenience method to Export table-data into a file *.csv, where a File chooser allows selecting File before. The table data is exported in a generic manner, say given table is exported 1:1
	 * including Header-Data. After saving the file will be opened by Excel or alternate spreadsheet application registered for *.csv ending.
	 *
	 * @param table
	 * @return Path of file saved
	 * @see ParserCSV#writeFile(PrintStream, JTable, char)
	 */
	protected final String exportTableData(final JTable table, final char separator) {
		final FileChooser saveDialog = new FileChooser(/*
		 * getSettings().
		 * getWorkingDirectory()
		 */);
		saveDialog.setDialogTitle(CommonUserAccess.getMniFileSaveAsText());//$NON-NLS-1$
		saveDialog.addChoosableFileFilter(ch.ehi.basics.view.GenericFileFilter.createCsvFilter());

		if (saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			showBusy(new Runnable() {
				@Override
				public void run() {
					PrintStream stream = null;
					try {
						FileOutputStream outStream = new FileOutputStream(saveDialog.getSelectedFile());
						stream = new PrintStream(outStream);
						ParserCSV.writeFile(stream, table, separator);
						outStream.flush();
						outStream.close();
					} catch (Exception e) {
						handleException(e);
					} finally {
						if (stream != null) {
							stream.close();
						}
					}
				}
			});
			if ((saveDialog != null) && (saveDialog.getSelectedFile() != null)) {
				try {
					BrowserControl.displayURL(saveDialog.getSelectedFile().getAbsolutePath());
				} catch (Throwable ex) {
					log.warn("could not open CSV by platform spreadshet application", ex);
				}
				return saveDialog.getSelectedFile().getAbsolutePath();
			}
		}
		return null;
	}

	/**
	 * Extremely critical Error.
	 *
	 * @param title Dialogtitle
	 * see #stopWaitDialog()
	 */
	public final void fatalError(JFrame frame, String title, String message, Exception exception) {
		ch.softenvironment.view.BaseDialog.showError(frame, title, message + "\n" + ResourceManager.getResource(BaseFrame.class, "CEFatalError"), exception);
	}

	/**
	 * Display a pop-up menu. Example SearchTable to JPopupMenu 1) Connect MouseReleased-Event (for Windows) and MousePressed-Event (for Unix) from Panel (for e.g. JScrollPane, JTablePane, etc) to
	 * this method. 2) Connect JPopupMenu's this-property visually to the MouseReleased-Event's Parameter popupMenu. 3) Overwrite #adaptSelection(MouseEvent, JPopupMenu)
	 *
	 * @see BaseDialog#genericPopupDisplay(java.awt.event.MouseEvent, javax.swing.JPopupMenu)
	 */
	protected final void genericPopupDisplay(java.awt.event.MouseEvent event, javax.swing.JPopupMenu popupMenu) {
		popupDisplay(this, event, popupMenu);
	}

	protected static void popupDisplay(Component owner, java.awt.event.MouseEvent event, javax.swing.JPopupMenu popupMenu) {
		try {
			if (owner instanceof ListMenuChoice) {
				// might be necessary in following cases:
				// - before opening a PopupMenu
				// - in combined Detail/SearchView's when TableSelection must
				// update current row in detailed fields
				((ListMenuChoice) owner).adaptUserAction(event, popupMenu);

				if (event.getClickCount() == 2) { // && ((event.getID() ==
					// MouseEvent.MOUSE_PRESSED
					// /*Linux*/) ||(event.getID()
					// ==
					// MouseEvent.MOUSE_RELEASED
					// /*Windows*/))) {
					// case: double-click -> defaultAction
					((ListMenuChoice) owner).changeObjects(event.getSource());
				}
			}

			if (event.isPopupTrigger() && (popupMenu != null)) {
				// might depend on ListMenuChoice#adaptUserAction() or
				// #adaptSelection()
				popupMenu.show(event.getComponent(), event.getX(), event.getY());
			}
		} catch (Throwable e) {
			showException(owner, e);
		}
	}

	/**
	 * Return model-instances to be treated by this GUI.
	 */
	protected final java.util.List<?> getObjects() {
		return objects;
	}

	/**
	 * Convenience Method.
	 */
	protected final String getResourceString(String propertyName) {
		return ResourceManager.getResource(this.getClass(), propertyName);
	}

	/**
	 * Calculate the screen size
	 *
	 * @return Dimension
	 */
	protected static Dimension getScreenSize() {
		return (Toolkit.getDefaultToolkit().getScreenSize());
	}

	/**
	 * Return GUI Configuration Options.
	 */
	public final ViewOptions getViewOptions() {
		return viewOptions;
	}

	/**
	 * Top-level Handler. Called whenever the part throws an exception.
	 * <p>
	 * (Catch even Throwable for historical reasons, VAJ, JDO, ..)
	 *
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(Throwable exception) {
		showException(this, exception);
	}

	/**
	 * Initialize View. Call this method in a View's standard initialize method to setup your stuff.
	 *
	 * see #initialize() // user code begin {1}..user code end
	 */
	protected abstract void initializeView() throws Exception;

	/**
	 * Developer utility. Inform user of <Not Yet Implemented> Feature.
	 *
	 * @param title Dialogtitle
	 */
	public final void nyi(String title) {
		nyi(title, ResourceManager.getResource(BaseFrame.class, "CWNotYetImplemented")); //$NON-NLS-1$
	}

	/**
	 * Developer utility. Inform user of <Not Yet Implemented> Feature.
	 *
	 * @param title Dialogtitle
	 * @param message Speaking info for user
	 */
	public final void nyi(String title, String message) {
		BaseDialog.showWarning(this, title, message);
	}

	/**
	 * Set this Frame at center of screen.
	 */
	public final void setCenterLocation() {
		setCenterLocation(this);
	}

	/**
	 * Set the given Window at center of screen.
	 */
	public static void setCenterLocation(Window window) {
		Dimension screenSize = getScreenSize();
		Dimension frameSize = window.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		window.setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2));
	}

	/**
	 * Switch to a new Look&Feel during runtime
	 *
	 * @see #createLookAndFeelMenu(JMenu)
	 */
	protected void setLookAndFeel(String style) {
		try {
			// javax.swing.UIManager.getSystemLookAndFeelClassName();
			// UIManager.getCrossPlatformLookAndFeelClassName(); //metal (Java
			// Standard)
			// "com.sun.java.swing.plaf.motif.MotifLookAndFeel"; //Unix
			// "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; //Windows
			// only
			// "com.sun.java.swing.plaf.mac.MacLookAndFeel"; //Mac only
			// "com.sun.java.swing.plaf.multi.MultiLookAndFeel";
			if ((style == null) || (style.length() == 0)) {
				UIManager.getSystemLookAndFeelClassName();
			} else {
				UIManager.setLookAndFeel(style);
			}
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Throwable e) {
			BaseDialog
				.showWarning(
					this,
					ResourceManager.getResource(BaseFrame.class, "CTlookAndFeel"),
					NlsUtils.formatMessage(ResourceManager.getResource(BaseFrame.class, "CWManagerNotAvailable"), style) + "\n" + ResourceManager
						.getResource(BaseFrame.class, "CWSuppressManager")); //$NON-NLS-4$//$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		}
	}

	/**
	 * Set this Frame relative to parent.
	 */
	public final void setRelativeLocation(java.awt.Component parent) {
		if (parent != null) {
			setLocation(new Point(parent.getX() + X_CHILD_OFFSET, parent.getY() + Y_CHILD_OFFSET));
		}
	}

	/**
	 * Set Look & Feel at startup of Application.
	 *
	 * @see #setLookAndFeel(String)
	 */
	public static void setSystemLookAndFeel() throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// Make sure we have nice window decorations.
		// JFrame.setDefaultLookAndFeelDecorated(true);
	}

	/**
	 * @see #dispose()
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		// TODO JRE7! doubleclick shows window in front as expected
		// change() shows new window behind unexpected
		// @see
		// http://docs.oracle.com/javase/7/docs/webnotes/tsg/TSG-Desktop/html/swing.html
		// super.setSize(getWidth()+1, getHeight()+1);
		// super.validate();
		// super.paint(getGraphics());
		// super.setFocusable(true);
		// SwingUtilities.updateComponentTreeUI(this);
		// @see
		// http://stackoverflow.com/questions/8081559/is-this-a-swing-java-7-rendering-bug
		if (super.getX() == 0) {
			super.setLocation(1, getY());
		}
		if (super.getY() == 0) {
			super.setLocation(getX(), 1);
		}
		super.repaint();

		if (getViewOptions() != null) {
			if (visible) {
				// remind model-instances represented by this GUI
				// to suppress multiple GUI's for the same model-instance
				getViewOptions().getViewManager().checkIn(getObjects(), this);
			}
		}
	}

	/**
	 * @see WaitDialog#showBusy(Frame, Runnable)
	 */
	public final void showBusy(final Runnable block) {
		WaitDialog.showBusy(this, block);
	}

	/**
	 * Top-level Handler. Called whenever the part throws an exception.
	 *
	 * @param exception java.lang.Throwable
	 */
	public static void showException(Component owner, java.lang.Throwable exception) {
		try {
			// update log
			log.warn("In: {}", owner, exception);//$NON-NLS-1$
			exception.printStackTrace(System.out);

			// inform user
			String title = null; //$NON-NLS-1$
			String message = ResourceManager.getResource(BaseFrame.class, "CWTopLevelHandler"); //$NON-NLS-1$
			if (exception instanceof NumberFormatException) {
				if ((exception.getMessage().length() == 0) || exception.getMessage().equals("empty String") || (exception.getMessage().equals("-"))) {//$NON-NLS-2$//$NON-NLS-1$
					log.warn("Developer warning: exception message might change -> use another recognition");//$NON-NLS-2$//$NON-NLS-1$
					log.warn("NumberFormatException ignored", exception);
					return;
				}

				title = ResourceManager.getResource(BaseFrame.class, "CTInputError"); //$NON-NLS-1$
				message = ResourceManager.getResource(BaseFrame.class, "CENumberFormat") + "\n  => " + exception.getLocalizedMessage();//$NON-NLS-2$ //$NON-NLS-1$
			} else if (exception instanceof DeveloperException) {
				title = ((DeveloperException) exception).getTitle();
				message = exception.getMessage();
			} else if (exception instanceof MissingResourceException) {
				log.error("Developer error: ignored", exception);
				return;
			} /*
			 * else if (exception instanceof java.sql.SQLException) {
			 *
			 * @see DbBaseFrame#handleException(..) }
			 */

			BaseDialog.showError(owner, title, message, exception);
		} catch (Throwable e) {
			log.error("Developer error: should not have been reached", e);
		} finally {
			// this method must not throw an Exception under any circumstances
		}
	}

	/**
	 * Show a SplashScreen. Typically used at Application startup.
	 *
	 * @deprecated
	 */
	protected static void showSplashScreen(Dimension preferredWindowSize, String image) {
		try {
			Window window = new SplashScreen(preferredWindowSize, image);

			/* Create the splash screen */
			window.pack();

			/* Center splash screen */
			setCenterLocation(window);
			window.setVisible(true);

			Thread.sleep(5000);

			window.dispose();
		} catch (Throwable e) {
			log.warn("<image={}", image, e);//$NON-NLS-2$//$NON-NLS-1$
		}
	}

	/**
	 * @see #showSplashScreen(Dimension, ImageIcon, long)
	 */
	protected static void showSplashScreen(Dimension preferredWindowSize, ImageIcon image) {
		showSplashScreen(preferredWindowSize, image, 5000);
	}

	/**
	 * Show a SplashScreen. Typically used at Application startup.
	 */
	protected static void showSplashScreen(Dimension preferredWindowSize, ImageIcon image, long timeOut) {
		try {
			Window window = new SplashScreen(preferredWindowSize, image);

			/* Create the splash screen */
			window.pack();

			/* Center splash screen */
			setCenterLocation(window);
			window.setVisible(true);

			Thread.sleep(timeOut);

			window.dispose();
		} catch (Throwable e) {
			log.warn("image={}", image, e);
		}
	}

	/**
	 * Reset GUI-Components NLS-Strings, such as Component-text or Component-textToolTip, usually after the Locale#getDefault() has changed. Property "text" and "toolTipText" are changed in a generic,
	 * recursive matter.
	 *
	 * @see #getResourceString(..)
	 */
	protected void updateStringComponent(Component component) {
		// @see SwingUtilities#updateComponentTreeUI(Component)
		java.lang.Class<?> resource = null;
		Component[] children = null;
		if (component instanceof JMenu) {
			children = ((JMenu) component).getMenuComponents();
		} else if (component instanceof Container) {
			children = ((Container) component).getComponents();
			if (!(component.getClass().getName().startsWith("javax.swing") || component.getClass().getName().startsWith("java.awt"))) {
				// container might be an own Part with its own
				// properties-Resource-File
				resource = component.getClass();
			}
		}
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				Component child = children[i];
				// try change potential NLS-properties
				updateStringProperty(resource, child, "text");
				updateStringProperty(resource, child, "toolTipText");
				// go down UI-Tree recursively
				updateStringComponent(child);
			}
		}
	}

	/**
	 * Look up a String in a properties-Resource-File, according to the following rule: Component = javax.swing.JLabel Component#name = LblMyLabel => entry in properties-File for "text" =
	 * LblMyLabel_text => entry in properties-File for "toolTipText" = LblMyLabel_toolTipText
	 *
	 * @param component Component having the given property
	 * @param property (usually "text" or "toolTipText")
	 */
	private void updateStringProperty(java.lang.Class<?> resource, Component component, final String property) {
		if (component.getName() != null) {
			BeanReflector<Component> bean = new BeanReflector<Component>(component, property);
			if (bean.hasProperty() == BeanReflector.GETTER_AND_SETTER) {
				try {
					String nls = null;
					if (resource == null) {
						nls = getResourceString(component.getName() + "_" + property);
					} else {
						nls = ResourceManager.getResource(resource, component.getName() + "_" + property);
					}
					bean.setValue(nls);
				} catch (Throwable e) {
					// it is quite possible to get an exception here, because
					// not all components are translateable
					// for e.g. ImageIcon's or language independent Strings like
					// "..."
					if ((e instanceof MissingResourceException) && (component instanceof javax.swing.JMenuItem)) {
						// try CommonUserAccess.properties
						try {
							String nls = ResourceManager.getResource(CommonUserAccess.class, component.getName() + "_" + property);
							bean.setValue(nls);
						} catch (Throwable cua) {
							log.warn("updateStringProperty(), Resource missing", e);
						}
					} else {
						log.warn("updateStringProperty(), Resource missing", e);
					}
				}
			}
		}
	}
}
