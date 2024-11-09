package entity;

public enum TankType {
    BIG_RED(1, "/res/player/big_red", 400),
    QUICK_RED(2, "/res/player/quick_red", 500),
    SUPER_RED(1, "/res/player/super_red", 800),
    GREEN(1, "/res/player/green_small", 200);
    public final int speed;
    public final String imagePath;
    public int point;
    private TankType(int speed, String imagePath, int point) {
        this.speed = speed;
        this.imagePath = imagePath;
        this.point = point;
    }
}
