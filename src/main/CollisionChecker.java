package main;

import entity.Entity;

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
                           }

                        }

                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            if (isPlayer == true){
                                index = i;
                            }
                        }

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            if (isPlayer == true){
                                index = i;
                            }
                        }

                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            if (isPlayer == true){
                                index = i;
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



}
