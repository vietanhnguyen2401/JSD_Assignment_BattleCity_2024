package entity;

public enum TankType {
    BIG_RED(1, 4, "/res/player/big_red"),
    QUICK_RED(2, 3, "/res/player/quick_red"),
    SUPER_RED(1, 5, "/res/player/super_red"),
    GREEN(1, 2, "/res/player/green_small"),
    TANK_ARMOR(1, 8, "/res/player/tank_armor");
    public final int speed;
    public final int lives;
    public final String imagePath;
    private TankType(int speed, int lives, String imagePath) {
        this.speed = speed;
        this.lives = lives;
        this.imagePath = imagePath;
    }
}
