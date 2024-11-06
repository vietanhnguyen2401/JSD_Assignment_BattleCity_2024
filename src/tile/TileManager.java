package tile;

import main.GamePanel;
import entity.Player;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
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
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/" + imagePath + "_tile.png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap() {
        try {
            InputStream is = getClass().getResourceAsStream("/res/maps/level01.txt");
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
    public void draw(Graphics2D g2, Player player) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        while(col < gp.maxScreenCol  && row < gp.maxScreenRow){
            int tileNum = mapTileNum[col][row];
            if(tileNum != 5) { // If the tile is not grass, draw it
                g2.drawImage(tile[tileNum].image, x , y, null);
            }
            col++;
            x+= gp.tileSize;

            if (col == gp.maxScreenCol){
                col = 0;
                x=0;
                row++;
                y += gp.tileSize;
            }
        }


        // Draw the player
        player.draw(g2);

        // Draw the grass tiles
        col = 0;
        row = 0;
        x = 0;
        y = 0;
        while(col < gp.maxScreenCol && row < gp.maxScreenRow){
            int tileNum = mapTileNum[col][row];
            if(tileNum == 5) { // If the tile is grass, draw it
                g2.drawImage(tile[tileNum].image, x, y, null);
            }
            col++;
            x+= gp.tileSize;

            if (col == gp.maxScreenCol){
                col = 0;
                x=0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}
