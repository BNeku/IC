package com.yhondri_nerea;

import com.yhondri_nerea.entities.Coordinate;
import com.yhondri_nerea.entities.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
    private  Coordinate initCoordinate = new Coordinate(0, 0);
    private  Coordinate goalCoordinate = new Coordinate(5, 2);
    private  int mazeDimension = 5;
    private PriorityQueue<Node> openNodesPriorityQueue = new PriorityQueue<>();
    private List<Coordinate> closedCoordinateList = new ArrayList<>();
    private List<Node> obstacleCoordinateList = new ArrayList<>();
    private char[][] boardGame;
    private List<Coordinate> neighboursArray;

    public void run() {
        boardGame = drawMaze();
        setupNeighboursArray();

        Node initialNode = new Node(initCoordinate, distanceBetween(initCoordinate, goalCoordinate), 0.0);
        openNodesPriorityQueue.add(initialNode);

        Node goalNode = null;
        while (goalNode == null && !openNodesPriorityQueue.isEmpty()) {
            Node currentNode = openNodesPriorityQueue.poll();
            if (currentNode.getCoordinate().isEqualTo(goalCoordinate)) {
                goalNode = currentNode;
            } else {
                for (int i = 0; i < 7; i++) {
                    Coordinate neighbourCoordinate = getNeighbourCoordinate(currentNode.getCoordinate(), i);
                    if (neighbourCoordinate == null || isObstacle(neighbourCoordinate) || isClosed(neighbourCoordinate)) {
                        continue;
                    }

                    double distanceFromStartToNeighbour = distanceBetween(currentNode.getCoordinate(), neighbourCoordinate) + currentNode.getH();
                    Node neighbourNode = getOpenNode(neighbourCoordinate);

                    if (neighbourNode == null) {
                        double distanceToEnd = distanceBetween(neighbourCoordinate, goalCoordinate);
                        Node newNeighbourNode = new Node(neighbourCoordinate, distanceToEnd, distanceFromStartToNeighbour);
                        newNeighbourNode.setParentNode(currentNode);
                        openNodesPriorityQueue.add(newNeighbourNode);
                    } else if (distanceFromStartToNeighbour < neighbourNode.getH()){
                        neighbourNode.setH(distanceFromStartToNeighbour);
                        neighbourNode.setParentNode(currentNode);
                        updateNode(neighbourNode);
                    }
                }
            }
        }

        if (goalNode != null) {
            List<Coordinate> pathToGoal = new ArrayList<>();
            Node currentNode = goalNode;
            while (currentNode != null) {
                pathToGoal.add(0, currentNode.getCoordinate());
                currentNode = currentNode.getParentNode();
            }
        }
    }

    private double distanceBetween(Coordinate coordinate1, Coordinate coordinate2) {
        double x = (coordinate1.getRow() - coordinate2.getRow());
        double y = (coordinate1.getColumn() - coordinate2.getColumn());
        double z = (x*x) + (y*y);
        return Math.sqrt(z);
    }

    private Coordinate getNeighbourCoordinate(Coordinate coordinate, int atIndex) {
        Coordinate neighbourTempCoordinate = neighboursArray.get(atIndex);
        Coordinate neighbourCoordinate = new Coordinate(coordinate.getColumn() + neighbourTempCoordinate.getColumn(), coordinate.getRow() + neighbourTempCoordinate.getRow());

        if (isValidCoordinate(neighbourCoordinate)) {
            return  neighbourCoordinate;
        } else {
            return null;
        }
    }

    private boolean isValidCoordinate(Coordinate coordinate) {
        return (coordinate.getRow() > 0 && coordinate.getRow() < mazeDimension && coordinate.getColumn() < mazeDimension);
    }

    private boolean isObstacle(Coordinate coordinate) {
        return obstacleCoordinateList.contains(coordinate);
    }

    private boolean isClosed(Coordinate coordinate) {
        return closedCoordinateList.contains(coordinate);
    }

    private Node getOpenNode(Coordinate coordinate) {
        Iterator openNodesIterator = openNodesPriorityQueue.iterator();
        boolean stop = false;
        Node currentNode = null;

        while (openNodesIterator.hasNext() && !stop) {
            currentNode = (Node) openNodesIterator.next();
            if (currentNode.getCoordinate() == coordinate) {
                stop = true;
            }
        }
        return currentNode;
    }

    private void updateNode(Node node) {
        openNodesPriorityQueue.remove(node); //Elimina el node ccon esa coordinate.
        openNodesPriorityQueue.add(node); //Lo añadimos con su nueva h.
    }

    private void setupNeighboursArray() {
        neighboursArray = new ArrayList<Coordinate>();
        neighboursArray.add(new Coordinate(-1, -1));
        neighboursArray.add(new Coordinate(0, -1));
        neighboursArray.add(new Coordinate(1, -1));
        neighboursArray.add(new Coordinate(-1, 0));
        neighboursArray.add(new Coordinate(1, 0));
        neighboursArray.add(new Coordinate(-1, 1));
        neighboursArray.add(new Coordinate(0, 1));
        neighboursArray.add(new Coordinate(1, 1));
    }

    private void printwMaze() {
        int row, col;
        for (row = 0; row < boardGame.length; row++) {
            for (col = 0; col < boardGame[row].length; col++) {
                boardGame[row][col] = '.';
            }
        }

        for (row = 0; row < boardGame.length; row++) {
            System.out.println();
            for (col = 0; col < boardGame[row].length; col++) {
                System.out.print(boardGame[row][col]);
            }
        }
    }

    private char[][] drawMaze() {
        int row, col;
        char[][] boardGame = new char[mazeDimension][mazeDimension];
        for (row = 0; row < boardGame.length; row++) {
            for (col = 0; col < boardGame[row].length; col++) {
                boardGame[row][col] = '.';
            }
        }

        for (row = 0; row < boardGame.length; row++) {
            System.out.println();
            for (col = 0; col < boardGame[row].length; col++) {
                System.out.print(boardGame[row][col]);
            }
        }
        return boardGame;
    }

    private Node findNodeInQueue(Node nodeToFind) {
        boolean didFoundNode = false;
        Node node = null;
        List<Node> closedNodeList = new ArrayList<>();

        while (!openNodesPriorityQueue.isEmpty() && !didFoundNode) {
            Node tempNode = openNodesPriorityQueue.poll();
            if (tempNode.equals(nodeToFind)) {
                node = tempNode;
                didFoundNode = true;
            } else {
                closedNodeList.add(tempNode);
            }
        }

        for (int i = 0; i < closedNodeList.size(); i++) {
            openNodesPriorityQueue.add(closedNodeList.get(i));
        }

        return node;
    }

    private void expandirNode(Node fatherNode, char[][] board) {
//        Node newNode;
//        double diagonalValue = Math.sqrt(2);
//
//        //Vertical arriba
//        if (!(fatherNode.getCoordinate().getRow() + 1 > dim-1) && board[fatherNode.getCoordinate().getRow() + 1 ][fatherNode.getCoordinate().getColumn()] != 'x') {
//            newNode = new Node(fatherNode.getCoordinate().getRow() + 1, fatherNode.getCoordinate().getColumn(), 1, goalCoordinate, fatherNode);
//            if (openNodesPriorityQueue.contains(newNode)) {
//                double g = fatherNode.getG() + 1;
//                if(newNode.getG() >= g){
//                    Node foundNode = findNodeInQueue(newNode);
//                    foundNode.setG(newNode.getG());
//                }
//            } else {
//                openNodesPriorityQueue.add(newNode);
//            }
//        }
//
//        //Vertical abajo
//        if ((fatherNode.getCoordinate().getRow() - 1 > 0) && board[fatherNode.getCoordinate().getRow() - 1 ][fatherNode.getCoordinate().getColumn()] != 'x') {
//            newNode = new Node(fatherNode.getCoordinate().getRow() - 1, fatherNode.getCoordinate().getColumn(), 1, goalCoordinate, fatherNode);
//            if (openNodesPriorityQueue.contains(newNode)) {
//                double g = fatherNode.getG() + 1;
//                if(newNode.getG() >= g){
//                    Node foundNode = findNodeInQueue(newNode);
//                    foundNode.setG(newNode.getG());
//                }
//            } else {
//                openNodesPriorityQueue.add(newNode);
//            }
//        }
//
//        try {
//            //Horizontal derecha
//            if (!(fatherNode.getCoordinate().getColumn() + 1 > dim - 1) && board[fatherNode.getCoordinate().getRow()][fatherNode.getCoordinate().getColumn() + 1] != 'x') {
//                newNode = new Node(fatherNode.getCoordinate().getRow(), fatherNode.getCoordinate().getColumn() + 1, 1, goalCoordinate, fatherNode);
//                if (openNodesPriorityQueue.contains(newNode)) {
//                    double g = fatherNode.getG() + 1;
//                    if (newNode.getG() >= g) {
//                        Node foundNode = findNodeInQueue(newNode);
//                        foundNode.setG(newNode.getG());
//                    }
//                } else {
//                    openNodesPriorityQueue.add(newNode);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Exception " + fatherNode.getCoordinate().getColumn() + " " + fatherNode.getCoordinate().getRow());
//        }
//
//        //Horizontal izquierda
//        if ((fatherNode.getCoordinate().getColumn() - 1 > 0) && board[fatherNode.getCoordinate().getRow()][fatherNode.getCoordinate().getColumn() - 1] != 'x') {
//            newNode = new Node(fatherNode.getCoordinate().getRow(), fatherNode.getCoordinate().getColumn()-1, 1, goalCoordinate, fatherNode);
//            if (openNodesPriorityQueue.contains(newNode)) {
//                double g = fatherNode.getG() + 1;
//                if(newNode.getG() >= g){
//                    Node foundNode = findNodeInQueue(newNode);
//                    foundNode.setG(newNode.getG());
//                }
//            } else {cytrdrº
//                openNodesPriorityQueue.add(newNode);
//            }
//        }
//
//        //Diagonal arriba derecha
//        if (!(fatherNode.getCoordinate().getColumn() + 1 > dim-1) && !(fatherNode.getCoordinate().getRow() + 1 > dim-1)  && board[fatherNode.getCoordinate().getRow() + 1 ][fatherNode.getCoordinate().getColumn() + 1] != 'x') {
//            newNode = new Node(fatherNode.getCoordinate().getRow()+1, fatherNode.getCoordinate().getColumn()+1, diagonalValue, goalCoordinate, fatherNode);
//            if (openNodesPriorityQueue.contains(newNode)) {
//                double g = fatherNode.getG() + Math.sqrt(2);
//                if(newNode.getG() >= g){
//                    Node foundNode = findNodeInQueue(newNode);
//                    foundNode.setG(newNode.getG());
//                }
//            } else {
//                openNodesPriorityQueue.add(newNode);
//            }
//        }
//
//        //Diagonal arriba izquierda
//        if (!(fatherNode.getCoordinate().getColumn() + 1 > dim-1) && (fatherNode.getCoordinate().getRow() - 1 > 0)  && board[fatherNode.getCoordinate().getRow() - 1 ][fatherNode.getCoordinate().getColumn()] != 'x') {
//            newNode = new Node(fatherNode.getCoordinate().getRow()+1, fatherNode.getCoordinate().getColumn()-1, diagonalValue, goalCoordinate, fatherNode);
//            if (openNodesPriorityQueue.contains(newNode)) {
//                double g = fatherNode.getG() + Math.sqrt(2);
//                if(newNode.getG() >= g){
//                    Node foundNode = findNodeInQueue(newNode);
//                    foundNode.setG(newNode.getG());
//                }
//            } else {
//                openNodesPriorityQueue.add(newNode);
//            }
//        }
//
//        //Diagonal abajo derecha
//        if ((fatherNode.getCoordinate().getColumn() - 1 > 0) && !(fatherNode.getCoordinate().getRow() + 1 > dim-1) && board[fatherNode.getCoordinate().getRow() + 1 ][fatherNode.getCoordinate().getColumn() - 1] != 'x') {
//            newNode = new Node(fatherNode.getCoordinate().getRow()-1, fatherNode.getCoordinate().getColumn()+1, diagonalValue, goalCoordinate, fatherNode);
//            if (openNodesPriorityQueue.contains(newNode)) {
//                double g = fatherNode.getG() + Math.sqrt(2);
//                if(newNode.getG() >= g){
//                    Node foundNode = findNodeInQueue(newNode);
//                    foundNode.setG(newNode.getG());
//                }
//            } else {
//                openNodesPriorityQueue.add(newNode);
//            }
//        }
//
//        //Diagonal abajo izquierda
//        if ((fatherNode.getCoordinate().getColumn() - 1 > 0) && (fatherNode.getCoordinate().getRow() - 1 > 0)  && board[fatherNode.getCoordinate().getRow() - 1 ][fatherNode.getCoordinate().getColumn() - 1] != 'x') {
//            newNode = new Node(fatherNode.getCoordinate().getRow()-1, fatherNode.getCoordinate().getColumn()-1, diagonalValue, goalCoordinate, fatherNode);
//            if (openNodesPriorityQueue.contains(newNode)) {
//                double g = fatherNode.getG() + Math.sqrt(2);
//                if(newNode.getG() >= g){
//                    Node foundNode = findNodeInQueue(newNode);
//                    foundNode.setG(newNode.getG());
//                }
//            } else {
//                openNodesPriorityQueue.add(newNode);
//            }
//        }
    }
}
