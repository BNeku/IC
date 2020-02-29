package com.yhondri_nerea;

import com.yhondri_nerea.entities.Coordinate;

import java.util.List;

public interface AStarDelegate {
    void didFindPath(List<Coordinate> path);
    void didNotFindAPath();
    void didAddPoint();
    void onAddPointError(); //Cuando el usuario ya ha añadido 2 puntos.
    void didAddAnObstacle();
    void didAddPenalty();
    void didCloseNode();
}
