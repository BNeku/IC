package com.yhondri_nerea;

import com.yhondri_nerea.entities.Coordinate;

import java.util.List;

public interface AStarDelegate {
    void didFindPath(List<Coordinate> path);
    void didNotFindAPath();
    void didAddAnObstacle();
    void didCloseNode();
}
