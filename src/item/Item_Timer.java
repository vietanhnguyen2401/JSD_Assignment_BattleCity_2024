package item;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Timer extends SuperItem{
    public Item_Timer() {
        name="Timer";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/timer.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
