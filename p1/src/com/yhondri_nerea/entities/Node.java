package com.yhondri_nerea.entities;



public class Node implements Comparable<Node> {
    private Coordinate coordinate;
    private Node parentNode;
    private double g; //Distancia al final
    private double h; //Distancia hasta el inicio
    //g+h
    private double f;

    public Node(Coordinate coordinate, double g, double h)  {
        this.coordinate = coordinate;
        this.g = g;
        this.h = h;
        this.f = g+h;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getF() {
        return f;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    @Override
    public int compareTo(Node other) {
        if (this.f != other.f) {
            return Double.compare(this.f, other.f);
        } else if (this.g != other.g) {
            return Double.compare(this.g, other.g);
        } else {
            return Double.compare(this.h, other.h);
        }
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
}
