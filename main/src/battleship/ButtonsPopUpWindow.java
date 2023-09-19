package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

/**
 * PopUpWindow es una clase especializada para abrir ventanas pequeñas con botones
 *
 * @version 1.0, 21/09/2023
 * @author Lopez, Lucero, Yudica
 */

public class ButtonsPopUpWindow extends JPanel {
    private String imageURL; // URL de la imagen de fondo
    private int buttonPressed; // Botón presionado por el usuario
    private JFrame window; // Ventana principal

    // Constructor de la interfaz gráfica

    /**
     * Crea una interfaz grafica.
     * @param titleOfLabel titulo
     * @param arrayButtons array de Strings, que representan los botones a crear
     */
    public ButtonsPopUpWindow(String titleOfLabel, String[] arrayButtons) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Configura el diseño vertical
        addTitle(titleOfLabel); // Agrega un título
        addButtons(arrayButtons); // Agrega botones
    }

    // Método para agregar un título a la interfaz
    private void addTitle(String titleOfLabel) {
        JPanel panelBackground = new JPanel();
        panelBackground.setBackground(Color.WHITE);
        panelBackground.setLayout(new BoxLayout(panelBackground, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(titleOfLabel);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelBackground.add(Box.createVerticalGlue());
        panelBackground.add(title);
        panelBackground.add(Box.createVerticalGlue());

        add(Box.createVerticalStrut(10)); // Espacio vertical
        add(panelBackground);
        add(Box.createVerticalStrut(25)); // Espacio vertical
    }

    // Método para agregar botones a la interfaz
    private void addButtons(String[] arrayButtons) {
        for (int i = 0; i < arrayButtons.length; i++) {
            addButton(String.valueOf(arrayButtons[i]),  (i + 1));
            add(Box.createVerticalStrut(5)); // Espacio vertical
        }
        add(Box.createVerticalStrut(15)); // Espacio vertical
    }

    // Método para agregar un botón a la interfaz
    private void addButton(String text, final int buttonIndex) {
        JButton button = new JButton(text);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPressed = buttonIndex; // Establece el botón presionado
                window.dispose(); // Cierra la ventana
            }
        });
        add(button);
        add(Box.createVerticalStrut(5)); // Espacio vertical
    }

    // Método para pintar la imagen de fondo
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon image = new ImageIcon(imageURL);
        g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    public int showWindow(String title, int width, int height, String imageURL) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(this);
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        this.imageURL = imageURL;

        final CountDownLatch latch = new CountDownLatch(1);

        // Manejador de eventos para cerrar la ventana
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                latch.countDown(); // Reduce el contador de espera
            }
        });

        try {
            latch.await(); // Espera hasta que se cierre la ventana
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return buttonPressed; // Devuelve el botón presionado por el usuario
    }
}

