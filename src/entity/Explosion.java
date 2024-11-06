package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Explosion {
    private int x, y;
    private GamePanel gp;
    private BufferedImage[] explosionImages;
    private int frame = 0;
    private boolean alive = true;

    public Explosion(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        loadExplosionImages();
    }

    private void loadExplosionImages() {
        explosionImages = new BufferedImage[3];
        try {
            explosionImages[0] = ImageIO.read(getClass().getResourceAsStream("/res/explosion/big.png"));
            explosionImages[1] = ImageIO.read(getClass().getResourceAsStream("/res/explosion/small.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        frame++;
        if (frame >= explosionImages.length * 10) {
            alive = false;
        }
    }

    public void draw(Graphics2D g2) {
        if (frame < explosionImages.length * 10) {
            int index = frame / 10;
            g2.drawImage(explosionImages[index], x, y, gp.tileSize, gp.tileSize, null);
        }
    }

    public boolean isAlive() {
        return alive;
    }
}
