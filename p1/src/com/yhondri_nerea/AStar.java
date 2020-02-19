package com.yhondri_nerea;

import com.yhondri_nerea.entities.Coordinate;
import com.yhondri_nerea.entities.Node;

import java.util.PriorityQueue;

public class AStar {
    private  Coordinate initCoordinate = new Coordinate(0, 0);
    private  Coordinate goalCoordinate = new Coordinate(4, 1);
    private  int dim = 8;
    private PriorityQueue<Node> openNodesPriorityQueue = new PriorityQueue<Node>();
    private PriorityQueue<Node> closedNodesPriorityQueue = new PriorityQueue<Node>();

    public void run() {
        char[][] boardGame = drawMaze();

        Node initialNode = new Node(initCoordinate.getRow(), initCoordinate.getColumn(), 0, goalCoordinate, null);
        expandirNode(null, boardGame);
    }

    private char[][] drawMaze() {
        int row, col;
        char[][] boardGame = new char[dim][dim];
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

    private void expandirNode(Node fatherNode, char[][] board) {
        Node newNode;
        double diagonalValue = Math.sqrt(2);

        //Vertical arriba
        if (!(fatherNode.getCoordinate().getRow() + 1 > dim-1) && board[fatherNode.getCoordinate().getRow() + 1 ][fatherNode.getCoordinate().getColumn()] != 'x') {
            newNode = new Node(fatherNode.getCoordinate().getRow() + 1, fatherNode.getCoordinate().getColumn(), 1, goalCoordinate, fatherNode);
            openNodesPriorityQueue.add(newNode);
        }

        //Vertical abajo
        if ((fatherNode.getCoordinate().getRow() - 1 > 0) && board[fatherNode.getCoordinate().getRow() - 1 ][fatherNode.getCoordinate().getColumn()] != 'x') {
            newNode = new Node(fatherNode.getCoordinate().getRow() - 1, fatherNode.getCoordinate().getColumn(), 1, goalCoordinate, fatherNode);
            openNodesPriorityQueue.add(newNode);
        }

        //Horizontal derecha
        if (!(fatherNode.getCoordinate().getColumn() + 1 > dim-1)  && board[fatherNode.getCoordinate().getRow()][fatherNode.getCoordinate().getColumn() + 1] != 'x') {
            newNode = new Node(fatherNode.getCoordinate().getRow(), fatherNode.getCoordinate().getColumn()+1, 1, goalCoordinate, fatherNode);
            openNodesPriorityQueue.add(newNode);
        }

        //Horizontal izquierda
        if ((fatherNode.getCoordinate().getColumn() - 1 > 0) && board[fatherNode.getCoordinate().getRow()][fatherNode.getCoordinate().getColumn() - 1] != 'x') {
            newNode = new Node(fatherNode.getCoordinate().getRow(), fatherNode.getCoordinate().getColumn()-1, 1, goalCoordinate, fatherNode);
            openNodesPriorityQueue.add(newNode);
        }

        //Diagonal arriba derecha
        if (!(fatherNode.getCoordinate().getColumn() + 1 > dim-1) && !(fatherNode.getCoordinate().getRow() + 1 > dim-1)  && board[fatherNode.getCoordinate().getRow() + 1 ][fatherNode.getCoordinate().getColumn() + 1] != 'x') {
            newNode = new Node(fatherNode.getCoordinate().getRow()+1, fatherNode.getCoordinate().getColumn()+1, diagonalValue, goalCoordinate, fatherNode);
            openNodesPriorityQueue.add(newNode);
        }

        //Diagonal arriba izquierda
        if (!(fatherNode.getCoordinate().getColumn() + 1 > dim-1) && (fatherNode.getCoordinate().getRow() - 1 > 0)  && board[fatherNode.getCoordinate().getRow() - 1 ][fatherNode.getCoordinate().getColumn()] != 'x') {
            newNode = new Node(fatherNode.getCoordinate().getRow()+1, fatherNode.getCoordinate().getColumn()-1, diagonalValue, goalCoordinate, fatherNode);
            openNodesPriorityQueue.add(newNode);
        }

        //Diagonal abajo derecha
        if ((fatherNode.getCoordinate().getColumn() - 1 > 0) && !(fatherNode.getCoordinate().getRow() + 1 > dim-1) && board[fatherNode.getCoordinate().getRow() + 1 ][fatherNode.getCoordinate().getColumn() - 1] != 'x') {
            newNode = new Node(fatherNode.getCoordinate().getRow()-1, fatherNode.getCoordinate().getColumn()+1, diagonalValue, goalCoordinate, fatherNode);
            openNodesPriorityQueue.add(newNode);
        }

        //Diagonal abajo izquierda
        if ((fatherNode.getCoordinate().getColumn() - 1 > 0) && (fatherNode.getCoordinate().getRow() - 1 > 0)  && board[fatherNode.getCoordinate().getRow() - 1 ][fatherNode.getCoordinate().getColumn() - 1] != 'x') {
            newNode = new Node(fatherNode.getCoordinate().getRow()-1, fatherNode.getCoordinate().getColumn()-1, diagonalValue, goalCoordinate, fatherNode);
            openNodesPriorityQueue.add(newNode);
        }
    }
}
