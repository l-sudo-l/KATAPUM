package com.mistacorp.game.servidor;

import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.Protocolo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * Ventana del servidor dedicado para cuando se abre el .jar con doble clic: en ese caso Windows no muestra
 * ninguna consola, asi que el mapa y el nombre de la sala se eligen aca y el servidor se cierra con un boton.
 */
public final class VentanaServidor {

    private VentanaServidor() {
    }

    public static void abrir() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pedirConfiguracion();
            }
        });
    }

    private static void pedirConfiguracion() {
        String[] nombresMapas = new String[TipoMapa.values().length];
        for (int i = 0; i < nombresMapas.length; i++) {
            nombresMapas[i] = TipoMapa.values()[i].obtenerNombreVisible();
        }
        JComboBox<String> selectorMapa = new JComboBox<String>(nombresMapas);
        JTextField campoNombre = new JTextField(ConfiguracionRed.NOMBRE_SALA_POR_DEFECTO, 16);

        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Mapa:"));
        panel.add(selectorMapa);
        panel.add(new JLabel("Nombre de la sala:"));
        panel.add(campoNombre);

        int opcion = JOptionPane.showConfirmDialog(null, panel, "Servidor KATAPUM", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            System.exit(0);
            return;
        }
        TipoMapa mapa = TipoMapa.values()[selectorMapa.getSelectedIndex()];
        String nombreSala = Protocolo.limpiarNombreSala(campoNombre.getText());
        iniciar(mapa, nombreSala);
    }

    private static void iniciar(TipoMapa mapa, String nombreSala) {
        final JTextArea registro = new JTextArea(18, 60);
        registro.setEditable(false);
        registro.setLineWrap(true);
        registro.setWrapStyleWord(true);
        registro.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        redirigirSalida(registro);

        final HiloServidor servidor;
        try {
            servidor = new HiloServidor(ConfiguracionRed.PUERTO_SERVIDOR, nombreSala, mapa);
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el puerto " + ConfiguracionRed.PUERTO_SERVIDOR
                + ".\nSeguramente ya hay un servidor o una sala abierta en esta PC.\n\n" + e.getMessage(),
                "Servidor KATAPUM", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return;
        }

        final JFrame ventana = new JFrame("Servidor KATAPUM - " + servidor.obtenerNombreSala() + " (" + mapa.obtenerNombreVisible() + ")");
        JLabel titulo = new JLabel("Sala \"" + servidor.obtenerNombreSala() + "\" con el mapa " + mapa.obtenerNombreVisible()
            + ", puerto " + ConfiguracionRed.PUERTO_SERVIDOR);
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JButton botonCerrar = new JButton("Cerrar servidor");
        botonCerrar.addActionListener(e -> cerrar(servidor, ventana));
        ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrar(servidor, ventana);
            }
        });
        ventana.getContentPane().add(titulo, BorderLayout.NORTH);
        ventana.getContentPane().add(new JScrollPane(registro), BorderLayout.CENTER);
        ventana.getContentPane().add(botonCerrar, BorderLayout.SOUTH);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        servidor.start();
        System.out.println("Servidor KATAPUM \"" + servidor.obtenerNombreSala() + "\" escuchando en el puerto "
            + ConfiguracionRed.PUERTO_SERVIDOR + " con el mapa " + mapa.obtenerNombreVisible());
        System.out.println("Los jugadores entran desde PARTIDA LAN (o CONECTAR POR IP con la IP de esta PC)");
    }

    private static void cerrar(HiloServidor servidor, JFrame ventana) {
        servidor.detener();
        try {
            servidor.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ventana.dispose();
        System.exit(0);
    }

    private static void redirigirSalida(final JTextArea registro) {
        final PrintStream original = System.out;
        OutputStream hacia = new OutputStream() {
            private final StringBuilder linea = new StringBuilder();

            @Override
            public synchronized void write(int b) {
                original.write(b);
                if (b == '\n') {
                    final String texto = new String(linea.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    linea.setLength(0);
                    SwingUtilities.invokeLater(() -> {
                        registro.append(texto + "\n");
                        registro.setCaretPosition(registro.getDocument().getLength());
                    });
                } else if (b != '\r') {
                    linea.append((char) (b & 0xFF));
                }
            }
        };
        try {
            System.setOut(new PrintStream(hacia, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.setOut(new PrintStream(hacia, true));
        }
    }
}
