package battleship;


public abstract class Ship {
    private int health;
    private final int length; // length is final since it's not modified after construction

    // Ship orientation defined as -1, 0, 1 values
    // where (dx, dy) = (1, 0) means HorizontalToRight
    //       (dx, dy) = (-1, 0) means HorizontalToLeft
    //       (dx, dy) = (0, 1) means VerticalUp
    //       (dx, dy) = (0, -1) means VerticalDown
    private int orientationDx;
    private int orientationDy;
    private int originColumn;
    private int originRow;
    protected int specialShotLeft;

    public Ship(int length){
        this.health = length;
        this.length = length;
        this.originColumn = 0;
        this.originRow = 0;
        orientationDx = 1;
        orientationDy = 0;
    }

    // Getters y Setters

    public int getSpecialShotLeft() {
        return specialShotLeft;
    }

    public void setSpecialShotLeft(int specialShotLeft) {
        this.specialShotLeft = specialShotLeft;
    }

    public int getHealth(){
        return health;
    }
    public int getLength(){
        return length;
    }
    public int getOriginRow() {
        return originRow;
    }
    public void setOriginRow(int originRow) {
        this.originRow = originRow;
    }
    public int getOriginColumn() {
        return originColumn;
    }

    public void setOriginColumn(int originColumn) {
        this.originColumn = originColumn;
    }
    public boolean isAlive(){
        return health > 0;
    }
    public void hit(){
        health -= 1;
    }

    // Getters and setters for orientation
    public int getOrientationDx() {
        return orientationDx;
    }

    public void setOrientationDx(int orientationDx) {
        this.orientationDx = orientationDx;
    }
    public int getOrientationDy() {
        return orientationDy;
    }

    public void setOrientationDy(int orientationDy) {
        this.orientationDy = orientationDy;
    }

    public boolean hasSpecialShotLeft() { return specialShotLeft > 0;}

    public abstract Shot getSpecialShot();
}
