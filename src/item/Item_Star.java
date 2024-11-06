package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Star extends SuperItem{
    GamePanel gp;
    public Item_Star(GamePanel gp) {
        this.gp = gp;
        name="Star";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/star.png"));
            uTool.scaleImage(image, gp.tileSize*2 -6, gp.tileSize*2 -6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
