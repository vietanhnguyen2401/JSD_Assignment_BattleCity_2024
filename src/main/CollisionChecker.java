package main;

import entity.Entity;

import java.util.concurrent.ForkJoinPool;

public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity){
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entity.x + entity.solidArea.x +  entity.solidArea.width;
        int entityTopY = entity.y +  entity.solidArea.y;
        int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;


        int entityLeftCol = entityLeftX/gp.tileSize;
        int entityRightCol = entityRightX/gp.tileSize;
        int entityTopRow = entityTopY/gp.tileSize;
        int entityBottomRow = entityBottomY/gp.tileSize;



        int tileNum1, tileNum2;
        switch (entity.direction){
            case "up":
                entityTopRow = (entityTopY - entity.speed)/gp.tileSize;
                tileNum1 = gp.TManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.TManager.mapTileNum[entityRightCol][entityTopRow];
                if (gp.TManager.tile[tileNum1].collision || gp.TManager.tile[tileNum2].collision){
                    entity.collisionOn = true;
                }
                
                break;
            case "down":
                entityBottomRow = (entityBottomY + entity.speed)/gp.tileSize;
                tileNum1 = gp.TManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.TManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.TManager.tile[tileNum1].collision || gp.TManager.tile[tileNum2].collision){
                    entity.collisionOn = true;
                }

                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.speed)/gp.tileSize;
                tileNum1 = gp.TManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.TManager.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.TManager.tile[tileNum1].collision || gp.TManager.tile[tileNum2].collision){
                    entity.collisionOn = true;
                }

                break;
            case "right":
                entityRightCol = (entityRightX + entity.speed)/gp.tileSize;
                tileNum1 = gp.TManager.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.TManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.TManager.tile[tileNum1].collision || gp.TManager.tile[tileNum2].collision){
                    entity.collisionOn = true;
                }

                break;
        }
    }

    public int checkItem(Entity entity, boolean player) {
        int index = 999;
        for (int i = 0; i < gp.item.length; i++){
            if (gp.item[i] != null){
                // Get entity's solid area position
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;

                gp.item[i].solidArea.x = gp.item[i].x + gp.item[i].solidArea.x;
                gp.item[i].solidArea.y = gp.item[i].y + gp.item[i].solidArea.y;

                switch(entity.direction){
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            System.out.println("up collision");
                            break;
                        }

                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            System.out.println("down collision");
                            break;
                        }

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            System.out.println("left collision");
                            break;
                        }

                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.item[i].solidArea)){
                            System.out.println("right collision");
                            break;
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
}
