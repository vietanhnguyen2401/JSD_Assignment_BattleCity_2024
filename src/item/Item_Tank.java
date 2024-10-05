package item;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Tank extends SuperItem{
    public Item_Tank() {
        name="Tank";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/tank.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
