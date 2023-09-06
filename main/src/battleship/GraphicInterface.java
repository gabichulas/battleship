package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

public class GraphicInterface extends JPanel {
    private String imageURL; // URL de la imagen de fondo
    private String buttonPressed; // Botón presionado por el usuario
    private JFrame window; // Ventana principal

    // Constructor de la interfaz gráfica
    public GraphicInterface(String titleOfLabel, String[] arrayButtons) {
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
            addButton(String.valueOf(arrayButtons[i]), "Button " + (i + 1));
            add(Box.createVerticalStrut(5)); // Espacio vertical
        }
        add(Box.createVerticalStrut(15)); // Espacio vertical
    }

    // Método para agregar un botón a la interfaz
    private void addButton(String text, final String buttonName) {
        JButton button = new JButton(text);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setButtonPressed(buttonName); // Establece el botón presionado
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
        ImageIcon image = new ImageIcon(getClass().getResource(getImageURL()));
        g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    // Métodos getter y setter para imageURL y buttonPressed
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(String buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    // Método para mostrar la ventana
    public String showWindow(String title, int width, int height, String ImageURL) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(this);
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        setImageURL(ImageURL);

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

        return getButtonPressed(); // Devuelve el botón presionado por el usuario
    }
}

