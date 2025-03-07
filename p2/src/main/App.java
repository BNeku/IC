package main;

import algorithm.ID3;
import helper.Reader;
import model.Node;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App {
    private JButton gameAttributesButton;
    private JLabel attributesFileLabel;
    private JPanel panelMain;
    private JButton gameAttributesFileButton;
    private JLabel gameAttributesLabel;
    private JButton executeButton;
    private JTextArea resultTextArea;
    private List<List<String>> dataList;
    private List<String> attributesList;
    private String[] attributesArray;

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
        gameAttributesButton.addActionListener(e -> onChooseGame());
        gameAttributesFileButton.addActionListener(e -> onChooseGameAttributes());
        executeButton.addActionListener(e -> execuetID3());
    }

    private void onChooseGameAttributes() {
        JFileChooser jFileChooser = new JFileChooser();
        int option = jFileChooser.showOpenDialog(panelMain);
        attributesList = new ArrayList<>();

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            String[] attributesArray = Reader.readFile(file).get(0);
            gameAttributesLabel.setText(file.getName());
            for (String value : attributesArray) {
                attributesList.add(value);
            }
        }

        setupComboBox();
    }

    private void setupComboBox() {
        attributesArray = new String[attributesList.size()];
        for (int i = 0; i < attributesList.size(); i++) {
            attributesArray[i] = attributesList.get(i);
        }
    }

    private void onChooseGame() {
        JFileChooser jFileChooser = new JFileChooser();
        int option = jFileChooser.showOpenDialog(panelMain);
        dataList = new ArrayList<>();
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            List<String[]> readList = Reader.readFile(file);
            attributesFileLabel.setText(file.getName());
            for (String[] array : readList) {
                dataList.add(Arrays.asList(array));
            }
        }
    }

    private void execuetID3() {
        ID3 id3 = new ID3(attributesList, dataList);
        Node rootNode = id3.executeID3();
        String result = getResult(rootNode, 0);
        resultTextArea.setText(result);
    }

    /**
     * Leyenda:
     * -> Nodo.
     * = Hoja (attributos del nodo seleccionado).
     * @param node Nodo raíz del subárbol.
     * @param level Nivel.
     * @return Devuelve el string con el árbol creado.+
     *
     * Cambiarlo para poner el atributo elegido y
     */
    private String getResult(Node node, int level) {
        if (node == null) {
            return "";
        }

        String leftSpaces = " ";
        for (int i = 0; i < (level * 4); i++) {
            leftSpaces += " ";
        }

        String result = String.format("%s -> %s\n", leftSpaces, node.getValue());

        if (node.getNodes() != null) {
            for (Map.Entry<String, Node> entry : node.getNodes().entrySet()) {
                result += String.format("%s   =%s\n", leftSpaces, entry.getKey());
                result += getResult(entry.getValue(), level + 1);
            }
        }

        return result;
    }
}
