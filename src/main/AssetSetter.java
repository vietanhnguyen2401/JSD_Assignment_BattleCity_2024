package main;

import entity.Enemy;
import item.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class AssetSetter {
    GamePanel gp;
    private Timer timer;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
        this.timer = new Timer();
    }

    public void setTimedItems() {
        // Schedule each item to appear every 10 seconds
        timer.schedule(new SpawnItemTask(new Item_Grenade(gp), 14, 14), 0);          // Immediate
        timer.schedule(new SpawnItemTask(new Item_Helmet(gp), 20, 10), 10000);       // 10 seconds
        timer.schedule(new SpawnItemTask(new Item_Timer(gp), 10, 21), 30000);        // 30 seconds
        timer.schedule(new SpawnItemTask(new Item_Tank(gp), 15, 15), 40000);         // 40 seconds

        // Schedule 3 Star items at 10-second intervals after the Tank
        timer.schedule(new SpawnItemTask(new Item_Star(gp), 17, 18), 0);         // 50 seconds
        timer.schedule(new SpawnItemTask(new Item_Star(gp), 18, 23), 0);         // 60 seconds
        timer.schedule(new SpawnItemTask(new Item_Star(gp), 19, 25), 0);         // 70 seconds
    }

    // Inner class to handle spawning an item at a specific position
    private class SpawnItemTask extends TimerTask {
        private SuperItem item;
        private int x, y;

        public SpawnItemTask(SuperItem item, int x, int y) {
            this.item = item;
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            item.x = x * gp.tileSize;
            item.y = y * gp.tileSize;

            // Find the next available slot for the item in gp.item array
            for (int i = 0; i < gp.item.length; i++) {
                if (gp.item[i] == null) {
                    gp.item[i] = item;
                    break;
                }
            }
        }
    }

    // Cancel the timer when no longer needed to prevent resource leaks
    public void cancelTimers() {
        timer.cancel();
    }
    public void setNPC(){
        Random random = new Random();
        gp.npc[0] = new Enemy(gp);
        gp.npc[0].x = 398;
        gp.npc[0].y = 50;


        gp.npc[1] = new Enemy(gp);
        gp.npc[1].x = 398;
        gp.npc[1].y = 50;

        gp.npc[2] = new Enemy(gp);
        gp.npc[2].x = 398;
        gp.npc[2].y = 50;

        gp.npc[3] = new Enemy(gp);
        gp.npc[3].x = 398;
        gp.npc[3].y = 50;
    }

}
