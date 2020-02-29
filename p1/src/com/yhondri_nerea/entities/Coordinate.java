package com.yhondri_nerea.entities;

public class Coordinate {
    private int column;
    private int row;

    public Coordinate(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

//    a negative integer, zero, or a positive integer as this object
//     *          is less than, equal to, or greater than the specified object.
//
    public boolean isEqualTo(Coordinate other) {
        return (this.getRow() == other.getRow() && this.getColumn() == other.getColumn());
    }
}
