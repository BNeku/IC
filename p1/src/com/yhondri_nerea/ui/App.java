package com.yhondri_nerea.ui;

import com.yhondri_nerea.entities.Coordinate;
import com.yhondri_nerea.entities.CoordinateType;

import javax.swing.*;
import java.awt.*;

public class App implements Board.Delegate {
    private JPanel panelMain;
    private JPanel boardContainerPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public App() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1;
        Board board = new Board(this);
        boardContainerPanel.setLayout(new CardLayout());
        boardContainerPanel.add(board, SwingConstants.CENTER);
    }

    @Override
    public int getNumberOfRows() {
        return 10;
    }

    @Override
    public int getNumberOfColums() {
        return 10;
    }

    @Override
    public void didClickOnCoordinate(Coordinate coordinate) {

    }

    @Override
    public void onCellDragged(Coordinate coordinate) {

    }

    @Override
    public void onCellAlt(Coordinate coordinate) {

    }

    @Override
    public CoordinateType getCoordinateType() {
        return CoordinateType.FREE;
    }
}
