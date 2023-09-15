package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;



public class InputUtils {
    // Class with static methods used to simplify the usage of user input data
    private static Shot chooseShip(Player player, String className)
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
                        ConsoleColors.printWarning("A tu barco no le quedan disparos especiales!");
                    else
                        ConsoleColors.printWarning("El barco seleccionado requiere " +
                                        shot.getRequiredMissileCount() +
                                        " misiles, cuando tenes " +
                                        player.getRemainingShots() +
                                        " disparos restantes"
                                );

                }
            }
        }
        if (!hasRequiredShipType)
            ConsoleColors.printWarning("No tiene barcos de tipo " + className + ". Desplegando disparo puntual.");
        return new PointShot();
    }
    public static Shot inputShot(Player player, GUI playerGui)
    {
        while (true) {
            try {
                GUI.singleDisplayConsole(playerGui, "SELECCIONE EL TIPO DE DISPARO", Color.white);

                // Selects special Shot
                JButton[] arrayButtonShot = playerGui.getArrayButtonShot();
                playerGui.enableShotsButtons(arrayButtonShot);
                int buttonShotPresed = -1;
                playerGui.setButtonShotPresed(buttonShotPresed);

                while (buttonShotPresed == -1) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buttonShotPresed = playerGui.getButtonShotPresed();
                }

                Shot shot;

                return switch (buttonShotPresed) {
                    case 0 -> {
                        yield new PointShot();
                    }
                    case 1 -> {
                        yield chooseShip(player, "Cruise");
                    }
                    case 2 -> {
                        yield chooseShip(player, "Submarine");
                    }
                    case 3 -> {
                        yield chooseShip(player, "Vessel");
                    }
                    case 4 -> {
                        yield chooseShip(player, "AircraftCarrier");
                    }
                    default -> throw new IOException();
                };
            } catch (IOException e) {
                ConsoleColors.printError("Seleccione un numero entre 1 y 4.");
            }
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

}
