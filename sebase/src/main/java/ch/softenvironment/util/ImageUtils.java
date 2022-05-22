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

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Node;

/**
 * Image encoding Utility.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class ImageUtils {

    /**
     * Save current Screen-Contents to given filename in PNG-Format (suffix will be set if missing in filename).
     *
     * @param filename
     * @throws AWTException
     * @see http://schmidt.devlib.org/java/save-screenshot.html
     */
    public static void screenShot(String filename) throws IOException, AWTException {
        // wait for a user-specified time
        // long time = 500; // ms to wait until shooting
        // Thread.sleep(time);

        // determine current screen size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        // create screen shot
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRect);
        // save captured image to PNG file
        ImageIO.write(image, "png", new File(checkSuffixPNG(filename)));

        // use System.exit if the program hangs after writing the file;
        // that's an old bug which got fixed only recently
        // System.exit(0);
    }

    /**
     * @see StringUtls#tryAppendSuffix()
     */
    private static String checkSuffixPNG(String filename) {
        return StringUtils.tryAppendSuffix(filename, ".png");
    }

    /**
     * Write given image into given file.
     *
     * @param filename
     * @param bufferedImage
     * @see http ://forum.java.sun.com/thread.jspa?threadID=653302&messageID=3840311
     */
    public static void writeImage(String filename, BufferedImage bufferedImage) throws IOException {
        Iterator<ImageWriter> iterator = ImageIO.getImageWritersBySuffix("png");
        if (!iterator.hasNext()) {
            throw new IllegalStateException("no writers found");
        }
        ImageWriter imageWriter = iterator.next();
        FileOutputStream outputStream = new FileOutputStream(checkSuffixPNG(filename));
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        ImageTypeSpecifier imageTypeSpecifier = new ImageTypeSpecifier(bufferedImage);
        IIOMetadata metadata = imageWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);
        String sFormat = "javax_imageio_png_1.0";
        Node node = metadata.getAsTree(sFormat);
        IIOMetadataNode gammaNode = new IIOMetadataNode("gAMA");
        String sGamma = "55556";
        gammaNode.setAttribute("value", sGamma);
        node.appendChild(gammaNode);
        metadata.setFromTree(sFormat, node);
        imageWriter.write(new IIOImage(bufferedImage, null, metadata));
    }
}