package main;

import helper.DataSource;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import algorithm.Bayes;

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
            List<List<String>> data = new DataSource().loadData();
            //ejemplos de los archivos
            //AQUI ES DONDE TE DIGO QUE DEBERÍA LEER POR PANTALLA O DESDE UN ARCHIVO LOS DATOS
            //new Bayes(data).whichClassBelongTo(5.1,3.5,1.4,0.2);
            //new Bayes(data).whichClassBelongTo(6.9,3.1,4.9,1.5);
            //new Bayes(data).whichClassBelongTo(5.0,3.4,1.5,0.2);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
