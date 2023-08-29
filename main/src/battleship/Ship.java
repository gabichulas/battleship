package battleship;


public class Ship {
    private int health;
    private final int length; // length is final since it's not modified after construction

    // Ship orientation defined as -1, 0, 1 values
    // where (dx, dy) = (1, 0) means HorizontalToRight
    //       (dx, dy) = (-1, 0) means HorizontalToLeft
    //       (dx, dy) = (0, 1) means VerticalUp
    //       (dx, dy) = (0, -1) means VerticalDown
    private int orientationDx;
    private int orientationDy;
    public Ship(int length){
        this.health = length;
        this.length = length;
    }

    // Getters y Setters

    public int getHealth(){
        return health;
    }
    public int getLength(){
        return length;
    }

    // Métodos
    public boolean isAlive(Ship ship){
        return ship.health > 0;
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


}
