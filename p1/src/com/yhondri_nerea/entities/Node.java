package com.yhondri_nerea.entities;



public class Node {
    private Coordinate coordinate;
    private Coordinate goalCoordinate;
    //Distancia hasta el inicio (lo calculamos a partir de su padre).
    private double g;
    //
    private double h;
    //g+h
    private double f;

    public Node(int row, int column, double distancesTo, Coordinate goalCoordinate, Node fatherNode)  {
        coordinate = new Coordinate(column, row);
        this.goalCoordinate = goalCoordinate;
        double result = Math.exp(goalCoordinate.getColumn() - column) + Math.exp(goalCoordinate.getRow() - row);
        h = Math.sqrt(result);

        if (fatherNode != null) {
            g = fatherNode.g + distancesTo;
            f = h + g;
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
