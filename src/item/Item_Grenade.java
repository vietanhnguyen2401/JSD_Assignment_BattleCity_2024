package item;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Grenade extends SuperItem{
    public Item_Grenade() {
        name="Grenade";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/grenade.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
