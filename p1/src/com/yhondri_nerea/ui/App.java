package com.yhondri_nerea.ui;

import com.yhondri_nerea.AStar;
import com.yhondri_nerea.AStarDelegate;
import com.yhondri_nerea.entities.Coordinate;
import com.yhondri_nerea.entities.CoordinateType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class App implements Board.Delegate, AStarDelegate {
    private JPanel panelMain;
    private JPanel boardContainerPanel;
    private JButton startButton;
    private JButton resetButton;
    private JButton paredButton;
    private JButton inicioButton;
    private JButton barroButton;
    private JButton metaButton;
    private JButton castilloButton;
    private AStar aStar;
    private Board boardView;
    private double penaltyValue = 10;
    private CoordinateType selectedCoordinateType;
    private boolean finished = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setMinimumSize(new Dimension(600, 600));
        frame.pack();
        frame.setVisible(true);
    }

    public App() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1;
        boardView = new Board(this);
        boardContainerPanel.setLayout(new CardLayout());
        boardContainerPanel.add(boardView, SwingConstants.CENTER);

        setupListeners();
        resetGame();
    }

    private void setupListeners() {
        startButton.addActionListener(e -> {
            finished = true;
            aStar.run();
        });

        resetButton.addActionListener(e -> {
            resetGame();
            boardView.reloadData();
        });

        paredButton.addActionListener(e -> {
            selectedCoordinateType = CoordinateType.OBSTACLE;
        });

        barroButton.addActionListener(e -> {
            selectedCoordinateType = CoordinateType.PENALTY;
        });

        inicioButton.addActionListener(e -> {
            selectedCoordinateType = CoordinateType.START;
        });

        metaButton.addActionListener(e -> {
            selectedCoordinateType = CoordinateType.GOAL;
        });

        castilloButton.addActionListener(e -> {
            selectedCoordinateType = CoordinateType.WAYPOINT;
        });

    }

    public void resetGame() {
        aStar = new AStar(this, getNumberOfColums());
        finished = false;
    }

    //region Board.Delegate

    @Override
    public int getNumberOfRows() {
        return 5;
    }

    @Override
    public int getNumberOfColums() {
        return 5;
    }

    @Override
    public void didClickOnCoordinate(Coordinate coordinate) {
        if (finished) {
            return;
        }

        switch (selectedCoordinateType) {
            case OBSTACLE:
                aStar.addObstacle(coordinate);
                break;
            case POINT:
                aStar.addPoint(coordinate);
                break;
            case START:
                aStar.addStart(coordinate);
                break;
            case GOAL:
                aStar.addGoal(coordinate);
                break;
            case PENALTY: aStar.addPenalty(coordinate, penaltyValue);
                break;
            case WAYPOINT:
                aStar.addWaypoint(coordinate);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCellDragged(Coordinate coordinate) {
        aStar.addObstacle(coordinate);
    }

    @Override
    public void onCellAlt(Coordinate coordinate) {
        aStar.addPenalty(coordinate, penaltyValue);
    }

    @Override
    public CoordinateType getCoordinateType(Coordinate coordinate) {
        return aStar.getCoordinateType(coordinate);
    }

    //endregion

    //region AstarDelegate

    @Override
    public void didFindPath(List<Coordinate> path) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardView.reloadData();
            }
        });
    }

    @Override
    public void didNotFindAPath() {

    }

    @Override
    public void didAddPoint() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardView.reloadData();
            }
        });
    }

    @Override
    public void onAddPointError() {

    }

    @Override
    public void didAddAnObstacle() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardView.reloadData();
            }
        });
    }

    @Override
    public void didAddPenalty() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardView.reloadData();
            }
        });
    }

    @Override
    public void didCloseNode() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardView.reloadData();
            }
        });

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    //endregion
}
