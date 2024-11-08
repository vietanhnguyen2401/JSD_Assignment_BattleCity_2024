package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Tank extends SuperItem{
    GamePanel gp;
    public Item_Tank(GamePanel gp) {
        this.gp = gp;
        name="Tank";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/tank.png"));
            uTool.scaleImage(image, gp.tileSize*2 -6, gp.tileSize*2 -6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
