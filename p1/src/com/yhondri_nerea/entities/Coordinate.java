package com.yhondri_nerea.entities;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Coordinate)) return false;

        Coordinate otherCoordinate = (Coordinate)other;
        return this.getRow() == otherCoordinate.getRow() && this.getColumn() == otherCoordinate.getColumn();
    }

}
