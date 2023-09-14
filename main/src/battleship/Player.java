package battleship;

/**
 * Clase que modela al jugador.
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class Player {
    private String name;
    private Map map;
    private GUI gui;
    private int shots;
    private int hits;
    private int remainingShots = 30;    // (numero provisorio)


    // Getters y Setters

    /**
     * Obtiene los tiros restantes de un jugador.
     * @return Tiros restantes.
     */
    public int getRemainingShots() {
        return remainingShots;
    }
    /**
     * Establece los tiros restantes de un jugador.
     * @param remainingShots Tiros restantes.
     */
    public void setRemainingShots(int remainingShots) {
        this.remainingShots = remainingShots;
    }

    /**
     * Obtiene el nombre del jugador.
     * @return Nombre del jugador.
     */
    public String getName() {
        return name;
    }
    /**
     * Establece el nombre del jugador.
     * @param name Nombre del jugador.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el mapa asociado a un jugador.
     * @return Mapa del jugador.
     */
    public Map getMap() {
        return map;
    }
    /**
     * Establece el mapa asociado a un jugador.
     * @param map Mapa del jugador.
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Obtiene la interfaz gráfica asociada a un jugador.
     * @return Interfaz grafica.
     */
    public GUI getGui() {
        return gui;
    }
    /**
     * Establece la interfaz gráfica asociada a un jugador.
     * @param gui Interfaz grafica.
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    /**
     * Obtiene la cantidad de disparos que acertó un jugador.
     * @return Disparos acertados por el jugador.
     */
    public int getHits() {
        return hits;
    }
    /**
     * Establece la cantidad de disparos que acertó un jugador.
     * @param hits Disparos acertados por el jugador.
     */
    public void setHits(int hits) {
        this.hits = hits;
    }

    // Constructores

    public Player(String name, Map map, int shots, int hits, GUI gui) {
        this.name = name;
        this.map = map;
        this.shots = shots;
        this.hits = hits;
        this.gui = gui;
    }

    /**
     * Crea un jugador.
     */
    public Player(){}
}
