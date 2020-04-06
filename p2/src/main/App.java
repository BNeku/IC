package main;

import helper.Reader;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class App {
    private JButton gameAttributesButton;
    private JLabel attributesFileLabel;
    private JPanel panelMain;
    private JButton gameAttributesFileButton;
    private JLabel gameAttributesLabel;
    private List<String[]> dataList;
    private String[] attributesList;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ID3");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setMinimumSize(new Dimension(600, 600));
        frame.pack();
        frame.setVisible(true);
    }

    public App() {
        gameAttributesButton.addActionListener(e -> onChooseGameAttributes());
        gameAttributesFileButton.addActionListener(e -> onChooseGame());
    }

    private void onChooseGameAttributes() {
        JFileChooser jFileChooser = new JFileChooser();
        int option = jFileChooser.showOpenDialog(panelMain);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            dataList = Reader.readFile(file);
            attributesFileLabel.setText(file.getName());
            for (String[] array : dataList) {
                for (String value : array) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        }
    }

    private void onChooseGame() {
        JFileChooser jFileChooser = new JFileChooser();
        int option = jFileChooser.showOpenDialog(panelMain);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            attributesList = Reader.readFile(file).get(0);
            gameAttributesLabel.setText(file.getName());
            for (String value : attributesList) {
                System.out.print(value + ", ");
            }
        }
    }
}
