package Game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The ImageLoader class is responsible for loading images from resources,
 * providing utility methods to create `ImageIcon` and `BufferedImage` objects 
 * based on file paths.
 */
public class ImageLoader {

    /**
     * Creates an `ImageIcon` from a given file path and description.
     * The image is loaded from the resources folder and returned as an `ImageIcon` object.
     *
     * @param path The relative path to the image file.
     * @param description The description of the image for accessibility purposes.
     * @return The `ImageIcon` created from the image file, or null if the file is not found.
     */
    static public Icon createImageIcon(String path, String description) {
        URL imgURL = ImageLoader.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Creates a `BufferedImage` from a given file path.
     * The image is loaded from the resources folder and returned as a `BufferedImage` object.
     *
     * @param path The relative path to the image file.
     * @return The `BufferedImage` created from the image file, or null if the file is not found or could not be decoded.
     */
    static public BufferedImage createImage(String path) {
        URL imgURL = ImageLoader.class.getResource(path);
        if (imgURL != null) {
            try {
                BufferedImage img = ImageIO.read(imgURL);
                if (img == null)
                    throw new IOException();
                return img;
            } catch (IOException e) {
                System.err.println("Couldn't decode file: " + path);
                return null;
            }
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}