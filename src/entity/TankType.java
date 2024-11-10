package entity;

public enum TankType {
    BIG_RED(1, 4, "/res/player/big_red", 600),
    QUICK_RED(2, 3, "/res/player/quick_red", 600),
    SUPER_RED(1, 5, "/res/player/super_red", 700),
    GREEN(1, 2, "/res/player/green_small", 200),
    TANK_ARMOR(1, 8, "/res/player/tank_armor", 800);

    public final int speed;
    public final int lives;
    public final String imagePath;
    public int point;
  
    private TankType(int speed, int lives, String imagePath, int point) {
        this.speed = speed;
        this.lives = lives;
        this.imagePath = imagePath;
        this.point = point;
    }
}
