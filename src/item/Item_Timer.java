package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Timer extends SuperItem{
    GamePanel gp;
    public Item_Timer(GamePanel gp) {
        this.gp = gp;
        name="Timer";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/timer.png"));
            uTool.scaleImage(image, gp.tileSize*2 -6, gp.tileSize*2 -6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
