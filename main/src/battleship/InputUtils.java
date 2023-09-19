package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;



public class InputUtils {
    // Class with static methods used to simplify the usage of user input data
    public static Shot inputShot(Player player, GUI playerGui)
    {
        while (true) {
            playerGui.printTextQuestion("SELECCIONE EL TIPO DE DISPARO", Color.white);

            // Selects special Shot
            JButton[] arrayButtonShot = playerGui.getArrayButtonShot();
            playerGui.enableShotsButtons(arrayButtonShot);
            int buttonShotPresed = -1;
            playerGui.setButtonPressed(buttonShotPresed);
            while (buttonShotPresed == -1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buttonShotPresed = playerGui.getButtonPressed();
            }

            return switch (buttonShotPresed) {
                case 1 -> chooseShip(player, playerGui, "Cruise");
                case 2 -> chooseShip(player, playerGui,"Submarine");
                case 3 -> chooseShip(player, playerGui, "Vessel");
                case 4 -> chooseShip(player, playerGui,"AircraftCarrier");
                default -> new PointShot();
            };
        }
    }
    public static String inputName(int i) {
        String name;
        do {
            name = JOptionPane.showInputDialog("NOMBRE DEL JUGADOR " + i );
            if (name == null) {
                // Si se presiona "Cancelar", el programa se cierra
                System.exit(0);
            }
        } while (name.trim().isEmpty());
        return name;
    }

    public static int inputNum(String text, int numFinal) {
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
        } while (num <= 0 || num > numFinal);
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
                if (ship.specialShotLeft > 0 && enoughMissiles) {

                    ship.setSpecialShotLeft(ship.getSpecialShotLeft()-1);
                    return shot;
                } else {
                    if (ship.specialShotLeft == 0)
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
