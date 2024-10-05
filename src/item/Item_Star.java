package item;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Star extends SuperItem{
    public Item_Star() {
        name="Star";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/star.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
