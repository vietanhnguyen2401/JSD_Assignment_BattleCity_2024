package main;

import entity.Enemy;
import entity.Entity;
import entity.Explosion;
import item.SuperItem;

import java.awt.*;
import java.util.concurrent.ForkJoinPool;

public class CollisionChecker {
    GamePanel gp;


    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        if (!entity.alive) return; // Skip collision check if entity is not alive
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;


        int entityLeftCol = entityLeftX / gp.tileSize;
        int entityRightCol = entityRightX / gp.tileSize;
        int entityTopRow = entityTopY / gp.tileSize;
        int entityBottomRow = entityBottomY / gp.tileSize;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopY - entity.speed) / gp.tileSize;
                for (int col = entityLeftCol; col <= entityRightCol; col++) {
                    int tileNum = gp.TManager.mapTileNum[col][entityTopRow];
                    if (gp.TManager.tile[tileNum].collision) {
                        entity.collisionOn = true;
                        return;
                    }
                }
                break;
            case "down":
                entityBottomRow = (entityBottomY + entity.speed) / gp.tileSize;
                for (int col = entityLeftCol; col <= entityRightCol; col++) {
                    int tileNum = gp.TManager.mapTileNum[col][entityBottomRow];
                    if (gp.TManager.tile[tileNum].collision) {
                        entity.collisionOn = true;
                        return;
                    }
                }
                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.speed) / gp.tileSize;
                for (int row = entityTopRow; row <= entityBottomRow; row++) {
                    int tileNum = gp.TManager.mapTileNum[entityLeftCol][row];
                    if (gp.TManager.tile[tileNum].collision) {
                        entity.collisionOn = true;
                        return;
                    }
                }
                break;
            case "right":
                entityRightCol = (entityRightX + entity.speed) / gp.tileSize;
                for (int row = entityTopRow; row <= entityBottomRow; row++) {
                    int tileNum = gp.TManager.mapTileNum[entityRightCol][row];
                    if (gp.TManager.tile[tileNum].collision) {
                        entity.collisionOn = true;
                        return;
                    }
                }
                break;
        }
    }

    public void handleItemPickUp(String itemName){
        //TODO add item effects
        System.out.println("picked up: " + itemName);

        if (itemName == "Timer"){
            for(Enemy e : gp.npc) {
                //todo delay 3 seconds -> e.setFreezed(false)
                if (e != null) e.setFreezed(true);
            }
            } else if (itemName == "Star"){
            gp.player.starCount++;
            System.out.println("current star count:" + gp.player.starCount);
        } else if (itemName == "Tank"){
            gp.player.lives++;
        } else if (itemName == "Grenade"){
            for(Enemy e : gp.npc) {
                if (e != null && e.alive){
                    e.alive = false;
                    gp.explosions.add(new Explosion(gp, e.x, e.y));
                };
            }
        } else if (itemName == "Helmet"){
            gp.player.getShield().setActive(true);
        }
    }
    public int checkItem(Entity entity, boolean isPlayer) {
        if (!entity.alive) return -1;
        int index = 999; // placeholder number, any number that is not used by the item index array
        for (int i = 0; i < gp.item.length; i++) {
            if (gp.item[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;

                gp.item[i].solidArea.x = gp.item[i].x + gp.item[i].solidArea.x;
                gp.item[i].solidArea.y = gp.item[i].y + gp.item[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;

                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                           if (isPlayer == true){
                                index = i;
                               handleItemPickUp(gp.item[i].name);
                           }

                        }

                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            if (isPlayer == true){
                                index = i;
                                handleItemPickUp(gp.item[i].name);
                            }
                        }

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            if (isPlayer == true){
                                index = i;
                                handleItemPickUp(gp.item[i].name);

                            }
                        }

                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            if (isPlayer == true){
                                index = i;
                                handleItemPickUp(gp.item[i].name);

                            }

                        }

                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.item[i].solidArea.x = gp.item[i].solidAreaDefaultX;
                gp.item[i].solidArea.y = gp.item[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;
        if (!entity.alive) return index; // Skip check if entity is not alive

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null && target[i].alive) { // Check target is alive
                // Get entity's solid area position
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;

                target[i].solidArea.x = target[i].x + target[i].solidArea.x;
                target[i].solidArea.y = target[i].y + target[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;
    }


    public void checkPlayer(Entity entity) {
        if (!entity.alive) return; // Skip check if entity is not alive

        entity.solidArea.x = entity.x + entity.solidArea.x;
        entity.solidArea.y = entity.y + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.x + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.y + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }

    public boolean checkBaseCollision(Entity entity) {
        // Get entity's solid area position
        entity.solidArea.x = entity.x + entity.solidArea.x;
        entity.solidArea.y = entity.y + entity.solidArea.y;

        // Get base's solid area position
        gp.base.solidArea.x = gp.base.x + gp.base.solidArea.x;
        gp.base.solidArea.y = gp.base.y + gp.base.solidArea.y;

        boolean collisionDetected = false;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(gp.base.solidArea)) {
                    collisionDetected = true;
                }
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(gp.base.solidArea)) {
                    collisionDetected = true;
                }
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(gp.base.solidArea)) {
                    collisionDetected = true;
                }
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                if (entity.solidArea.intersects(gp.base.solidArea)) {
                    collisionDetected = true;
                }
                break;
        }

        // Reset positions
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.base.solidArea.x = gp.base.solidAreaDefaultX;
        gp.base.solidArea.y = gp.base.solidAreaDefaultY;

        return collisionDetected;
    }

    public boolean checkTileCollision(int x, int y) {
        int col = x / gp.tileSize;
        int row = y / gp.tileSize;
        int tileNum = gp.TManager.mapTileNum[col][row];
        return gp.TManager.tile[tileNum].collision;
    }
    public boolean isPositionOccupiedByEntity(int x, int y) {
        // Check if this position collides with the player
        if (gp.player != null && gp.player.solidArea.contains(x, y)) {
            return true;
        }

        // Check other NPCs or entities
        for (Entity entity : gp.npc) {
            if (entity != null && entity.solidArea.contains(x, y)) {
                return true;
            }
        }

        return false;
    }

}
