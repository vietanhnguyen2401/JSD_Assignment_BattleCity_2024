package main;

import item.*;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setItem() {
        gp.item[0] =  new Item_Grenade(gp);
        gp.item[0].x = 14 * gp.tileSize;
        gp.item[0].y = 14 * gp.tileSize;

        gp.item[1] =  new Item_Helmet(gp);
        gp.item[1].x = 20 * gp.tileSize;
        gp.item[1].y = 10 * gp.tileSize;

        gp.item[2] =  new Item_Shovel(gp);
        gp.item[2].x = 21 * gp.tileSize;
        gp.item[2].y = 11 * gp.tileSize;

        gp.item[3] =  new Item_Timer(gp);
        gp.item[3].x = 10 * gp.tileSize;
        gp.item[3].y = 21 * gp.tileSize;

        gp.item[4] =  new Item_Tank(gp);
        gp.item[4].x = 15 * gp.tileSize;
        gp.item[4].y = 15 * gp.tileSize;

        gp.item[5] =  new Item_Star(gp);
        gp.item[5].x = 17 * gp.tileSize;
        gp.item[5].y = 18 * gp.tileSize;
    }

}
