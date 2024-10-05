package item;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Helmet extends SuperItem{
    public Item_Helmet() {
        name="Helmet";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/helmet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
