package battleship;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class InputUtils {
    // Class with static methods used to simplify the usage of usewr input data
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
}
