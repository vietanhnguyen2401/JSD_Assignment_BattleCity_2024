package entity;

public enum TankType {
    BIG_RED(1, "/res/player/big_red"),
    QUICK_RED(2, "/res/player/quick_red"),
    SUPER_RED(1, "/res/player/super_red"),
    GREEN(1, "/res/player/green_small");
    public final int speed;
    public final String imagePath;
    private TankType(int speed, String imagePath) {
        this.speed = speed;
        this.imagePath = imagePath;
    }
}
