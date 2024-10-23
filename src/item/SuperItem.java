package item;

import entity.Player;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperItem {
    public BufferedImage image;
    public String name;
    public boolean collision = true;
    public int x, y;

    public Rectangle solidArea = new Rectangle(0,0, 30, 30);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public void draw(Graphics2D g2, GamePanel gp) {

                g2.drawImage(image, x, y, gp.tileSize*2 -6, gp.tileSize*2 - 6   , null);
        }
}
