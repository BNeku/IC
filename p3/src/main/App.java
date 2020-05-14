package main;

import helper.DataSource;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import utils.Matrix;

import algorithm.Bayes;
import algorithm.Lloyd;

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
            DataSource d = new DataSource();
            d.loadData();
            List<Matrix> data = d.getClassesData();
            Matrix centers = d.getCentrosMatrix();

            //PRUEBAS DE LOS ALGORITMOS
            Bayes algorithmBayes = new Bayes(data);
            Lloyd algorithmLloyd = new Lloyd(data, 0.0000000001,10, centers);

            //ejemplos de los archivos
            //AQUI ES DONDE TE DIGO QUE DEBERÍA LEER POR PANTALLA O DESDE UN ARCHIVO LOS DATOS
            double[][] prueba = new double[1][4];
            //prueba[0][0]=5.0;prueba[0][1]=3.5;prueba[0][2]=1.4;prueba[0][3]=0.2;
            prueba[0][0]=6.9;prueba[0][1]=3.1;prueba[0][2]=4.9;prueba[0][3]=1.5;
            //prueba[0][0]=5.0;prueba[0][1]=3.4;prueba[0][2]=1.5;prueba[0][3]=0.2;
            Matrix M = new Matrix(prueba);
            algorithmBayes.whichClassBelongTo(M);
            algorithmLloyd.whichClassBelongTo(M);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
