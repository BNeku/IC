package com.yhondri_nerea.ui;

import com.yhondri_nerea.ImagesHolder;
import com.yhondri_nerea.entities.Coordinate;
import com.yhondri_nerea.entities.CoordinateType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Board extends JComponent implements MouseMotionListener, MouseListener {

    private Coordinate selectedCoordinate;
    private int cellSize;
    private int leftOffset;
    private int topOffset;
    private Delegate delegate;
//    private BufferedImage barroImage;
//    private BufferedImage castilloImage;
//    private BufferedImage exploradoImage;
//    private BufferedImage inicioImage;
//    private BufferedImage metaImage;
//    private BufferedImage paredImage;
//    private BufferedImage pointImage;
//    private BufferedImage stepsImage;
    private final ImagesHolder imagesHolder = new ImagesHolder();
    public Delegate getDelegate() {
        return delegate;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    interface Delegate {
        int getNumberOfRows();
        int getNumberOfColums();
        void didClickOnCoordinate(Coordinate coordinate);
        void onCellDragged(Coordinate coordinate);
        void onCellAlt(Coordinate coordinate);
        CoordinateType getCoordinateType(Coordinate coordinate);
    }

    public Board(Delegate delegate) throws IOException {
        this.delegate = delegate;
        setBackground(Color.gray);
        setOpaque(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        setupImages();
    }

    private void setupImages() {
//        try {
//            String path;
//
//            path = getClass().getResource("../resources/barro.png").getPath();
//            barroImage = ImageIO.read(new File(path));
//            path = getClass().getResource("../resources/Castillo.png").getPath();
//            castilloImage = ImageIO.read(new File(path));
//            path = getClass().getResource("../resources/Explorado.png").getPath();
//            exploradoImage = ImageIO.read(new File(path));
//            path = getClass().getResource("../resources/Inicio.png").getPath();
//            inicioImage = ImageIO.read(new File(path));
//            path = getClass().getResource("../resources/Meta.png").getPath();
//            metaImage = ImageIO.read(new File(path));
//            path = getClass().getResource("../resources/Pared.png").getPath();
//            paredImage = ImageIO.read(new File(path));
//            path = getClass().getResource("../resources/Point.png").getPath();
//            pointImage = ImageIO.read(new File(path));
//            path = getClass().getResource("../resources/Steps.png").getPath();
//            stepsImage = ImageIO.read(new File(path));
//        } catch (Exception ex) {
//            System.out.println("Exception " +ex);
//        }
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
            delegate.didClickOnCoordinate(selectedCoordinate);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedCoordinate != null) {
            delegate.didClickOnCoordinate(selectedCoordinate);
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
        int imageX = x + 5;
        int imageY = y + 5;
        int imageSize = cellSize-10;

        switch (coordinateType) {
            case INVALID:
            case FREE:
                graphics.setColor(Color.white);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                break;
            case OBSTACLE:
                graphics.setColor(Color.WHITE);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                graphics.drawImage(imagesHolder.paredImage.getImage(), imageX, imageY, imageSize, imageSize, null);
                break;
            case WAYPOINT:
                graphics.setColor(Color.WHITE);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                graphics.drawImage(imagesHolder.castilloImage.getImage(), imageX, imageY, imageSize, imageSize, null);
                break;
            case OPEN:
                graphics.setColor(Color.green);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                break;
            case CLOSED:
                graphics.setColor(Color.white);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                graphics.drawImage(imagesHolder.exploradoImage.getImage(), imageX, imageY, imageSize, imageSize, null);
                break;
            case PENALTY:
                graphics.setColor(Color.WHITE);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                graphics.drawImage(imagesHolder.barroImage.getImage(), imageX, imageY, imageSize, imageSize, null);
                break;
            case PATH:
                graphics.setColor(Color.WHITE);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                graphics.drawImage(imagesHolder.stepsImage.getImage(), imageX, imageY, imageSize, imageSize, null);
                break;
            case POINT:
                graphics.setColor(Color.WHITE);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                graphics.drawImage(imagesHolder.pointImage.getImage(), imageX, imageY, imageSize, imageSize, null);
                break;
            case START:
                graphics.setColor(Color.WHITE);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                graphics.drawImage(imagesHolder.inicioImage.getImage(), imageX, imageY, imageSize, imageSize, null);
                break;
            case GOAL:
                graphics.setColor(Color.WHITE);
                graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                graphics.drawImage(imagesHolder.metaImage.getImage(), imageX, imageY, imageSize, imageSize, null);
                break;
        }

        if (selectedCoordinate != null && selectedCoordinate == new Coordinate(column, row)) {
            graphics.setColor(Color.CYAN);
            graphics.fillRect(x+1, y+1, cellSize-2, cellSize-2);
        }
    }
}
