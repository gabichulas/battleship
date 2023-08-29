package battleship;

public class Player {
    private String name;
    private Map map;
    private int shots;
    private int hits;

    // Getters y Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    // Constructores

    public Player(String name, Map map, int shots, int hits) {
        this.name = name;
        this.map = map;
        this.shots = shots;
        this.hits = hits;
    }

    public Player(){}
}
