package jsd.project.tank90.main;

import jsd.project.tank90.entity.Enemy;

import jsd.project.tank90.item.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

/**
 * The AssetSetter class is responsible for setting items and NPCs on the game screen.
 */
public class AssetSetter {
    private final GamePanel gp;
    private final Timer timer;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
        this.timer = new Timer();
    }

    /**
     * Sets timed items to appear on the game screen at specific intervals.
     */
    public void setTimedItems() {
        // Schedule each item to appear at specified intervals
        timer.schedule(new SpawnItemTask(new Item_Grenade(gp), 14, 14), 40000);  // Immediate
        timer.schedule(new SpawnItemTask(new Item_Helmet(gp), 20, 10), 20000);   // 20 seconds
        timer.schedule(new SpawnItemTask(new Item_Timer(gp), 10, 21), 30000);    // 30 seconds
        timer.schedule(new SpawnItemTask(new Item_Tank(gp), 15, 15), 40000);     // 40 seconds

        // Schedule 3 Star items at intervals after the Tank
        timer.schedule(new SpawnItemTask(new Item_Star(gp), 17, 18), 50000);     // 50 seconds
        timer.schedule(new SpawnItemTask(new Item_Star(gp), 18, 23), 60000);     // 60 seconds
        timer.schedule(new SpawnItemTask(new Item_Star(gp), 19, 25), 70000);     // 70 seconds
    }

    /**
     * Cancels all scheduled tasks to prevent resource leaks.
     */
    public void cancelTimers() {
        timer.cancel();
    }

    /**
     * Spawns NPCs at specific locations.
     */
    public void setNPC() {
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            gp.npc[i] = new Enemy(gp);
            gp.npc[i].x = 398;
            gp.npc[i].y = 50;
        }
    }

    /**
     * Inner class responsible for spawning an item at a specific position.
     */
    private class SpawnItemTask extends TimerTask {
        private final SuperItem item;
        private final int x, y;

        public SpawnItemTask(SuperItem item, int x, int y) {
            this.item = item;
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            item.x = x * gp.TILE_SIZE;
            item.y = y * gp.TILE_SIZE;

            // Check for existing item of the same type and replace it if found
            boolean itemReplaced = false;
            for (int i = 0; i < gp.item.length; i++) {
                if (gp.item[i] != null && gp.item[i].getClass() == item.getClass()) {
                    gp.item[i] = item;  // Replace existing item of the same type
                    itemReplaced = true;
                    break;
                }
            }

            // If no replacement was made, add the item to the next available slot
            if (!itemReplaced) {
                for (int i = 0; i < gp.item.length; i++) {
                    if (gp.item[i] == null) {
                        gp.item[i] = item;
                        break;
                    }
                }
            }
        }
    }
}
