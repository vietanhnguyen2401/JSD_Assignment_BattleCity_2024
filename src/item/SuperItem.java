package item;

import entity.Player;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperItem {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int x, y;

    public void draw(Graphics2D g2, GamePanel gp) {

                g2.drawImage(image, x, y, gp.tileSize*2 -6, gp.tileSize*2 - 6   , null);
        }
}
