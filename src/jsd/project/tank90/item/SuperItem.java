package jsd.project.tank90.item;

import jsd.project.tank90.main.GamePanel;
import jsd.project.tank90.main.UtilityTool;

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
    UtilityTool uTool = new UtilityTool();

    public void draw(Graphics2D g2, GamePanel gp) {

                g2.drawImage(image, x, y, gp.TILE_SIZE*2 -6, gp.TILE_SIZE*2 - 6   , null);
        }
}
