package main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class App {
    private JPanel panelMain;

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setMinimumSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);

        loadData();
    }

    private static void loadData() {
        try {
            new DataSource().loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
