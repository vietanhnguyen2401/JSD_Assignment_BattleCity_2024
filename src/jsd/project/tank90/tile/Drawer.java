package jsd.project.tank90.tile;

import jsd.project.tank90.entity.Bullet;
import jsd.project.tank90.entity.Enemy;
import jsd.project.tank90.main.GamePanel;
import jsd.project.tank90.entity.Player;
import jsd.project.tank90.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * The Drawer class is used for drawing all components in the Game
 */

public class Drawer {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public Drawer(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
        loadMap();
    }
    public void getTileImage() {
            setup(0, "black", false);
            setup(1, "brick", true);
            setup(2, "iron", true);
            setup(3, "boundary", true);
            setup(4, "water", true);
            setup(5, "grass", false);
    }

    public void setup(int index, String imagePath, boolean collision){
        UtilityTool uTool = new UtilityTool();

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/jsd/project/tank90/res/tiles/" + imagePath + "_tile.png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.TILE_SIZE, gp.TILE_SIZE);
            tile[index].collision = collision;
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap() {
        String levelMapPath = "/jsd/project/tank90/res/maps/level01.txt";
        if (gp.currentLevel == 2) levelMapPath = "/jsd/project/tank90/res/maps/level02.txt";
        if (gp.currentLevel == 3) levelMapPath = "/jsd/project/tank90/res/maps/level03.txt";
        if (gp.currentLevel == 4) levelMapPath = "/jsd/project/tank90/res/maps/level04.txt";
        if (gp.currentLevel == 5) levelMapPath = "/jsd/project/tank90/res/maps/level05.txt";

        try {
            InputStream is = getClass().getResourceAsStream(levelMapPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxScreenCol && row< gp.maxScreenRow){
                String line = br.readLine();

                while (col < gp.maxScreenCol) {
                    String numbers[] = line.split(" ");

                    int number = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = number;
                    col++;
                }
                if (col == gp.maxScreenCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2, Player player, Enemy[] enemy) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        while(col < gp.maxScreenCol  && row < gp.maxScreenRow){
            int tileNum = mapTileNum[col][row];
            if(tileNum != 5) { // If the jsd.project.tank90.tile is not grass, draw it
                g2.drawImage(tile[tileNum].image, x , y, null);
            }
            col++;
            x+= gp.TILE_SIZE;

            if (col == gp.maxScreenCol){
                col = 0;
                x=0;
                row++;
                y += gp.TILE_SIZE;
            }
        }


        // Draw the player
        player.draw(g2);

        // Draw the player
        for ( int i = 0 ; i < gp.npc.length; i++) {
            if (enemy[i] != null && enemy[i].alive) {
                enemy[i].draw(g2);
            }
        }
        for ( int i = 0 ; i < gp.npc.length; i++) {
            if (enemy[i] != null && enemy[i].alive) {
                for (Bullet bullet : enemy[i].bullets) {
                    if (bullet.alive) {
                        bullet.draw(g2); // Render bullet fired by the enemy
                    }
                }
            }
        }
        // Draw the grass tiles
        col = 0;
        row = 0;
        x = 0;
        y = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow){
            int tileNum = mapTileNum[col][row];
            if(tileNum == 5) { // If the jsd.project.tank90.tile is grass, draw it
                g2.drawImage(tile[tileNum].image, x, y, null);
            }
            col++;
            x+= gp.TILE_SIZE;

            if (col == gp.maxScreenCol){
                col = 0;
                x=0;
                row++;
                y += gp.TILE_SIZE;
            }
        }
    }
}
