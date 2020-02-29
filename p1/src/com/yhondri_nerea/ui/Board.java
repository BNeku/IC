package com.yhondri_nerea.ui;

import com.yhondri_nerea.entities.Coordinate;
import com.yhondri_nerea.entities.CoordinateType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Board extends JComponent implements MouseMotionListener, MouseListener {

    private Coordinate selectedCoordinate;
    private int cellSize;
    private int leftOffset;
    private int topOffset;
    private Delegate delegate;

    public Delegate getDelegate() {
        return delegate;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    interface Delegate {
        public int getNumberOfRows();
        public int getNumberOfColums();
        public void didClickOnCoordinate(Coordinate coordinate);
        public void onCellDragged(Coordinate coordinate);
        public void onCellAlt(Coordinate coordinate);
        public CoordinateType getCoordinateType(Coordinate coordinate);
    }

    public Board(Delegate delegate) {
        this.delegate = delegate;
        setBackground(Color.gray);
        setOpaque(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void reloadData() {
        repaint();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        selectedCoordinate = null;
        repaint();
    }

    //region MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {
        Coordinate newSelectedCoordinate = getCoordinate(e.getX(), e.getY());

        if (newSelectedCoordinate == null || selectedCoordinate == null || newSelectedCoordinate != selectedCoordinate) {
            if (newSelectedCoordinate != null) {
                selectedCoordinate = newSelectedCoordinate;
                if (e.getButton() == 1) {

                } else if (e.getButton() == 2 || e.isAltDown()) {

                }
            }
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    // endregion MouseMotionListener


    //region MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectedCoordinate = getCoordinate(e.getX(), e.getY());
        if (selectedCoordinate != null) {
            if (e.getButton() == 2 || e.isAltDown()) {
                delegate.onCellAlt(selectedCoordinate);
            } else if (e.getButton() == 1) {
                delegate.onCellDragged(selectedCoordinate);
            }
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedCoordinate != null) {
            if (e.getButton() == 3) {
                delegate.didClickOnCoordinate(selectedCoordinate);
            }

            selectedCoordinate = null;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    // endregion MouseListener

    private Coordinate getCoordinate(int x, int y) {
        if (!isEnabled()) {
            return null;
        }

        if (cellSize == 0 || x < leftOffset || x > getWidth() - leftOffset || y < topOffset || y > getHeight() - topOffset) {
            return null;
        }

        int row = (y - topOffset)/cellSize;
        int column = (x - leftOffset)/cellSize;

        return new Coordinate(column, row);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        int rows = delegate.getNumberOfRows();
        int colums = delegate.getNumberOfColums();

        if (rows <= 0 || colums <= 0) {
            return;
        }

        cellSize = Math.min(getWidth()/colums, getHeight()/rows);
        topOffset = (getHeight()-cellSize*rows)/2;
        leftOffset = (getWidth()-cellSize*rows)/2;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < colums; column++) {
                CoordinateType coordinateType = delegate.getCoordinateType(new Coordinate(column, row));
                setupCell(row, column, coordinateType, g);
            }
        }
    }

    private void setupCell(int row, int column, CoordinateType coordinateType, Graphics graphics) {
        int x = column * cellSize + leftOffset;
        int y = row * cellSize + topOffset;

        switch (coordinateType) {
            case INVALID:
            case FREE:
                graphics.setColor(Color.white);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                break;
            case OBSTACLE:
                graphics.setColor(Color.red);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                break;
            case OPEN:
                graphics.setColor(Color.green);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                break;
            case CLOSED:
                graphics.setColor(Color.magenta);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                break;
            case PENALTY:
                graphics.setColor(Color.orange);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                break;
            case PATH:
                graphics.setColor(Color.PINK);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                break;
        }

        if (selectedCoordinate != null && selectedCoordinate == new Coordinate(column, row)) {
            graphics.setColor(Color.CYAN);
            graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
        }
    }
}
