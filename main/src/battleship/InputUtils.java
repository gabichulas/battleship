package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;



public class InputUtils {
    // Class with static methods used to simplify the usage of user input data

    /**
     * Función estática que permite seleccionar los tipos de disparos
     * disponibles.
     * */
    public static Shot inputShot(Player player, GUI playerGui)
    {
        while (true) {
            playerGui.printTextQuestion("SELECCIONE EL TIPO DE DISPARO", Color.white);

            // Selects special Shot
            JButton[] arrayButtonShot = playerGui.getArrayButtonShot();
            playerGui.enableShotsButtons(arrayButtonShot);
            int buttonShotPressed = -1;
            playerGui.setButtonPressed(buttonShotPressed);
            while (buttonShotPressed == -1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buttonShotPressed = playerGui.getButtonPressed();
            }

            return switch (buttonShotPressed) {
                case 1 -> chooseShip(player, playerGui, "Cruise");
                case 2 -> chooseShip(player, playerGui,"Submarine");
                case 3 -> chooseShip(player, playerGui, "Vessel");
                case 4 -> chooseShip(player, playerGui,"AircraftCarrier");
                default -> new PointShot();
            };
        }
    }

    public static Position inputShootPosition(GUI playerGui, Map enemyMap)
    {
        Position shootPosition = new Position(0, 0);
        Quadrant shootQuadrant;
        JButton[][] enemyMatrix = playerGui.getEnemyMatrix();

        do {
            playerGui.enableMatrixButtons(enemyMatrix);
            int[] array = {-1, -1};
            playerGui.setListPosition(array);
            while (array[0] == -1 || array[1] == -1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                array = playerGui.getListPosition();
            }
            shootPosition.x = array[1];
            shootPosition.y = array[0];

            shootQuadrant = enemyMap.getQuadrant(shootPosition);

            // Verifica que el cuadrante sea valido
            if (shootQuadrant.isShot()) {
                playerGui.printTextConsole("YA DISPARÓ EN ESTE CUADRANTE, SELECCIONE UNO NUEVO", Color.YELLOW);
            }

        } while(shootQuadrant.isShot());

        return shootPosition;
    }
    /**
     * Abre una ventana para que el usuario puede ingresar una cadena
     * */
    public static String openStringPopUp(String label) {
        String name;
        do {
            name = JOptionPane.showInputDialog(label);
            if (name == null) {
                // Si se presiona "Cancelar", el programa se cierra
                System.exit(0);
            }
        } while (name.trim().isEmpty());
        return name;
    }

    /**
     * Abre una ventana para que el usuario pueda ingresar un entero
     * */
    public static int openIntPopUp(String text, int max) {
        int num;
        do {
            String inputP1 = JOptionPane.showInputDialog(text);
            if (inputP1 == null) {
                System.exit(0);
            }
            // Intenta convertir la entrada en un número
            try {
                num = Integer.parseInt(inputP1);
            } catch (NumberFormatException e) {
                num = -1; // Valor no válido
            }
        } while (num <= 0 || num > max);
        return num;
    }

    private static Shot chooseShip(Player player, GUI playerGui, String className)
    {
        boolean hasRequiredShipType = false;

        // For all alive Ships
        for (Ship ship : player.getMap().getAlive()) {

            if (ship.getClass().getSimpleName().equals(className)) {
                hasRequiredShipType = true;
                // If Ship has any special shot left and player has enough missiles to shoot
                Shot shot = ship.getSpecialShot();
                boolean enoughMissiles = shot.getRequiredMissileCount() <= player.getRemainingShots();
                if (ship.hasSpecialShotLeft() && enoughMissiles) {

                    ship.setSpecialShotLeft(ship.getSpecialShotLeft()-1);
                    return shot;
                } else {
                    if (!ship.hasSpecialShotLeft())
                        playerGui.printConsoleWarning("A tu barco no le quedan disparos especiales!");
                    else
                        playerGui.printConsoleWarning("El barco seleccionado requiere " +
                                shot.getRequiredMissileCount() +
                                " misiles, cuando tenes " +
                                player.getRemainingShots() +
                                " disparos restantes"
                        );

                }
            }
        }
        if (!hasRequiredShipType)
            playerGui.printConsoleWarning("No tiene barcos de tipo " + className + ". Desplegando disparo puntual.");
        return new PointShot();
    }
}
