package jsd.project.tank90.main;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The UtilityTool class is used for render images faster
 */

public class UtilityTool {
    public BufferedImage scaleImage(BufferedImage original, int width, int height){
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());

        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }
}
