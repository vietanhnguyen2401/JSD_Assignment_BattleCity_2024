package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Helmet extends SuperItem{
    GamePanel gp;
    public Item_Helmet(GamePanel gp) {
        this.gp = gp;
        name="Helmet";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/helmet.png"));
            uTool.scaleImage(image, gp.tileSize*2 -6, gp.tileSize*2 -6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
