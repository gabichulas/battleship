package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;



public class InputUtils {
    // Class with static methods used to simplify the usage of user input data
    public static boolean booleanInput(String msg)
    {
        boolean validInput = false;
        boolean inputResult = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(!validInput) {
            ConsoleColors.printInput(msg);
            try {
                String line = reader.readLine();
                validInput = Objects.equals(line, "y") || Objects.equals(line, "n");
                inputResult = Objects.equals(line, "y");
            } catch (Exception e) {
                ConsoleColors.printError("Respuesta inválida");
            }
            finally {
                if (!validInput)
                    ConsoleColors.printError("Respuesta inválida");
            }
        }
        return inputResult;
    }
    public static int integerInput(String msg)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            ConsoleColors.printInput(msg);
            try {
                String line = reader.readLine();
                return Integer.parseInt(line);
            } catch (Exception e) {
                ConsoleColors.printError("Respuesta inválida, debe ser un entero");
            }
        }
    }
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
    public static Shot inputShot(Player player)
    {
        while (true) {
            try {

                // Gives player the option of choosing any ship to shoot


                //String[] arrayButtons = {"1: Si ", "2: No "};
                //GraphicInterface window = new GraphicInterface("Quiere usar un disparo especial? ",arrayButtons);
                //int buttonPressed = window.showWindow(" BattleShip ",600,180,"images/SoldiersInc.jpg");
                //if (buttonPressed == 2)
                //    return new PointShot();

                GUI.singleDisplayConsole(player.getGui(), "SELECCIONE EL TIPO DE DISPARO", Color.white);

                // Selects special Shot

                GUI gui = player.getGui();
                JButton[] arrayButtonShot = gui.getArrayButtonShot();
                gui.enableShotsButtons(arrayButtonShot);
                int buttonShotPresed = -1;
                gui.setButtonShotPresed(buttonShotPresed);

                while (buttonShotPresed == -1) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buttonShotPresed = gui.getButtonShotPresed();
                }

                Shot shot;
                //String[] arrayButtons1 = {"1: crucero" , "2: submarino" , "3: buque", "4: portaaviones"};
                //GraphicInterface window1 = new GraphicInterface("Que barco quiere utilizar? ",arrayButtons1);
                //int buttonPressed1 = window1.showWindow(" BattleShip ",600,300,"images/SoldiersInc.jpg");

                return switch (buttonShotPresed) {
                    case 0 -> {
                        yield new PointShot();
                    }
                    case 1 -> {
                        shot = chooseShip(player, "Cruise");
                        yield shot;
                    }
                    case 2 -> {
                        shot = chooseShip(player, "Submarine");
                        yield shot;
                    }
                    case 3 -> {
                        shot = chooseShip(player, "Vessel");
                        yield shot;
                    }
                    case 4 -> {
                        shot = chooseShip(player, "AircraftCarrier");
                        yield shot;
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
        } while (name == null || name.trim().isEmpty());
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
