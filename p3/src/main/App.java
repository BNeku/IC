package main;

import algorithm.Borroso;
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
    private JComboBox testFileComboBox;
    private JComboBox algorithmComboBox;
    private JButton executeAlgorithmButton;
    private JTextArea resultTextArea;
    private JScrollPane resultScrollPanel;
    private final String[] testsCaseFiles = new String[]{"TestIris01.txt", "TestIris02.txt", "TestIris03.txt"};

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setMinimumSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }

    public App() {
        setupListeners();
    }

    private void setupListeners() {
        DefaultComboBoxModel testsCaseFilesModel = new DefaultComboBoxModel(testsCaseFiles);
        testFileComboBox.setModel(testsCaseFilesModel);

        String[] alogrithms = new String[]{"Bayes", "Borroso", "Lloyd"};
        DefaultComboBoxModel alogrithmsModel = new DefaultComboBoxModel(alogrithms);
        algorithmComboBox.setModel(alogrithmsModel);

        executeAlgorithmButton.addActionListener(e -> {
            executeAlgorithm();
        });
    }

    private void executeAlgorithm() {
        String testCaseFile = testsCaseFiles[testFileComboBox.getSelectedIndex()];
        DataSource dataSource = new DataSource();
        try {
            dataSource.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (algorithmComboBox.getSelectedIndex()) {
            case 0:
                executeBayes(testCaseFile);
                break;
            case 1:
                executeBorroso(testCaseFile);
                break;
            case 2:
                executeLloyd(testCaseFile);
                break;
            default:
                break;
        }
    }

    private void executeBorroso(String testCaseFile) {
        DataSource dataSource = new DataSource();
        try {
            dataSource.loadData();
            Borroso borroso = new Borroso(dataSource.getDataMatrix(), dataSource.getCentrosMatrix());
            List<String> fileValues =  dataSource.readFile(testCaseFile).get(0);
            double[] values = new double[fileValues.size()-1];
            for (int i = 0; i < values.length; i++) {
                values[i] = Double.parseDouble(fileValues.get(i));
            }
            String result = borroso.getClassForValues(values);
            resultTextArea.setText(result);
            javax.swing.SwingUtilities.invokeLater(() -> resultScrollPanel.getVerticalScrollBar().setValue(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeBayes(String testCaseFile) {
        DataSource dataSource = new DataSource();
        try {
            dataSource.loadData();
            Bayes bayes = new Bayes(dataSource.getClassesData());

            List<String> fileValues =  dataSource.readFile(testCaseFile).get(0);
            double[][] values = new double[1][fileValues.size()-1];
            for (int i = 0; i < fileValues.size()-1; i++) {
                values[0][i] = Double.parseDouble(fileValues.get(i));
            }
            Matrix M = new Matrix(values);
            String result = bayes.whichClassBelongTo(M);

            resultTextArea.setText(result);
            javax.swing.SwingUtilities.invokeLater(() -> resultScrollPanel.getVerticalScrollBar().setValue(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeLloyd(String testCaseFile) {
        DataSource dataSource = new DataSource();
        try {
            dataSource.loadData();
            Lloyd lloyd = new Lloyd(dataSource.getClassesData(),0.0000000001,10,dataSource.getCentrosMatrix());

            List<String> fileValues =  dataSource.readFile(testCaseFile).get(0);
            double[][] values = new double[1][fileValues.size()-1];
            for (int i = 0; i < fileValues.size()-1; i++) {
                values[0][i] = Double.parseDouble(fileValues.get(i));
            }
            Matrix M = new Matrix(values);
            String result = lloyd.whichClassBelongTo(M);

            resultTextArea.setText(result);
            javax.swing.SwingUtilities.invokeLater(() -> resultScrollPanel.getVerticalScrollBar().setValue(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
