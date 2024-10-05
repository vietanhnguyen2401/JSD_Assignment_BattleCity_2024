package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }
    private final int collisionTolerance = 5; // Adjust this value as needed

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
}
