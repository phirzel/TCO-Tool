package ch.softenvironment.view;

import java.awt.Rectangle;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * The public methods support tiling all frames in the JDesktopPane or only those in a particular layer.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class JInternalFrameUtils {

	public static void tile(JDesktopPane desktopPane, int layer) {
		JInternalFrame[] frames = desktopPane.getAllFramesInLayer(layer);
		if (frames.length == 0) {
			return;
		}

		tile(frames, desktopPane.getBounds());
	}

	public static void tile(JDesktopPane desktopPane) {
		JInternalFrame[] frames = desktopPane.getAllFrames();
		if (frames.length == 0) {
			return;
		}

		tile(frames, desktopPane.getBounds());
	}

	private static void tile(JInternalFrame[] frames, Rectangle dBounds) {
		int cols = (int) Math.sqrt(frames.length);
		int rows = (int) (Math.ceil(((double) frames.length) / cols));
		int lastRow = frames.length - cols * (rows - 1);
		int width, height;

		if (lastRow == 0) {
			rows--;
			height = dBounds.height / rows;
		} else {
			height = dBounds.height / rows;
			if (lastRow < cols) {
				rows--;
				width = dBounds.width / lastRow;
				for (int i = 0; i < lastRow; i++) {
					frames[cols * rows + i].setBounds(i * width, rows * height, width, height);
				}
			}
		}

		width = dBounds.width / cols;
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				frames[i + j * cols].setBounds(i * width, j * height, width, height);
			}
		}
	}
}
