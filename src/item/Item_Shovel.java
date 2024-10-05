package item;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Shovel extends SuperItem {
    public Item_Shovel() {
        name="Shovel";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/shovel.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
