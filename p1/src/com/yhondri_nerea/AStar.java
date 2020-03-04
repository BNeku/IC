package com.yhondri_nerea;

import com.yhondri_nerea.entities.Coordinate;
import com.yhondri_nerea.entities.CoordinateType;
import com.yhondri_nerea.entities.Node;
import java.util.*;

public class AStar {
    private Coordinate initCoordinate;
    private Coordinate goalCoordinate;
    private int mazeDimension;
    private PriorityQueue<Node> openNodesPriorityQueue = new PriorityQueue<>();
    private List<Coordinate> closedCoordinateList = new ArrayList<>();
    private List<Coordinate> obstacleCoordinateList = new ArrayList<>();
    private List<Coordinate> pointList = new ArrayList<>();
    private HashMap<Coordinate, Double> penaltyMap = new HashMap<>();
    private List<Coordinate> neighboursArray;
    private AStarDelegate delegate;
    private List<Coordinate> pathToGoal = new ArrayList<>();

    public AStar(AStarDelegate delegate, int mazeDimension) {
        this.delegate = delegate;
        this.mazeDimension = mazeDimension;
    }

    public void run() {
        setupNeighboursArray();

        Node initialNode = new Node(initCoordinate, distanceBetween(initCoordinate, goalCoordinate), 0.0);
        openNodesPriorityQueue.add(initialNode);

        Node goalNode = null;
        while (goalNode == null && !openNodesPriorityQueue.isEmpty()) {
            Node currentNode = openNodesPriorityQueue.poll();
            closedCoordinateList.add(currentNode.getCoordinate());

            if (currentNode.getCoordinate().equals(goalCoordinate)) {
                goalNode = currentNode;
            } else {
                for (int i = 0; i < 8; i++) {
                    Coordinate neighbourCoordinate = getNeighbourCoordinate(currentNode.getCoordinate(), i);
                    if (neighbourCoordinate == null || isObstacle(neighbourCoordinate) || isClosed(neighbourCoordinate)) {
                        continue;
                    }

                    Double penalty = getPenaltyPointAt(currentNode.getCoordinate());
                    if (penalty == null) {
                        penalty = 0.0;
                    }
                    double distanceFromStartToNeighbour = distanceBetween(currentNode.getCoordinate(), neighbourCoordinate) + currentNode.getH() + penalty;
                    Node neighbourNode = getOpenNode(neighbourCoordinate);

                    if (neighbourNode == null) {
                        penalty = getPenaltyPointAt(neighbourCoordinate);
                        if (penalty == null) {
                            penalty = 0.0;
                        }
                        double distanceToEnd = distanceBetween(neighbourCoordinate, goalCoordinate) + penalty;
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

            delegate.didCloseNode();
        }

        if (goalNode == null) {
            delegate.didNotFindAPath();
        } else {
            Node currentNode = goalNode;
            while (currentNode != null) {
                pathToGoal.add(0, currentNode.getCoordinate());
                currentNode = currentNode.getParentNode();
            }

            delegate.didFindPath(pathToGoal);
        }
    }

    private double distanceBetween(Coordinate coordinate1, Coordinate coordinate2) {
        double x = (coordinate1.getColumn() - coordinate2.getColumn());
        double y = (coordinate1.getRow() - coordinate2.getRow());
        double z = (x*x) + (y*y);
        return Math.sqrt(z);
    }

    private Coordinate getNeighbourCoordinate(Coordinate coordinate, int atIndex) {
        Coordinate neighbourTempCoordinate = neighboursArray.get(atIndex);
        Coordinate neighbourCoordinate = new Coordinate(coordinate.getColumn() + neighbourTempCoordinate.getColumn(), coordinate.getRow() + neighbourTempCoordinate.getRow());

        if (isValidCoordinate(neighbourCoordinate)) {
            return neighbourCoordinate;
        } else {
            return null;
        }
    }

    private boolean isValidCoordinate(Coordinate coordinate) {
        return (coordinate.getRow() >= 0 && coordinate.getRow() < mazeDimension && coordinate.getColumn() >= 0 && coordinate.getColumn() < mazeDimension);
    }

    private boolean isObstacle(Coordinate coordinate) {
        synchronized (obstacleCoordinateList) {
            return obstacleCoordinateList.contains(coordinate);
        }
    }

    private boolean isClosed(Coordinate coordinate) {
        synchronized (closedCoordinateList) {
            return closedCoordinateList.contains(coordinate);
        }
    }

    private boolean isPath(Coordinate coordinate) {
        synchronized (pathToGoal) {
            return pathToGoal.contains(coordinate);
        }
    }

    private Node getOpenNode(Coordinate coordinate) {
        Iterator openNodesIterator = openNodesPriorityQueue.iterator();
        boolean stop = false;
        Node currentNode;
        Node foundNode = null;

        while (openNodesIterator.hasNext() && !stop) {
            currentNode = (Node) openNodesIterator.next();
            if (currentNode.getCoordinate() == coordinate) {
                foundNode = currentNode;
                stop = true;
            }
        }
        return foundNode;
    }

    private Double getPenaltyPointAt(Coordinate coordinate) {
        synchronized (coordinate) {
            return penaltyMap.get(coordinate);
        }
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

    //region MVC
    public void addPoint(Coordinate coordinate) {
        if (pointList.size() == 2) {
            delegate.onAddPointError();
            return;
        }

        if (pointList.size() == 0) {
            initCoordinate = coordinate;
        } else {
            goalCoordinate = coordinate;
        }

        CoordinateType coordinateType = getCoordinateType(coordinate);
        if (coordinateType == CoordinateType.FREE) {
            synchronized (pointList) {
                pointList.add(coordinate);
            }
            delegate.didAddPoint();
        }
    }

    public void addObstacle(Coordinate coordinate) {
        CoordinateType coordinateType = getCoordinateType(coordinate);
        if (coordinateType == CoordinateType.FREE) {
            synchronized (obstacleCoordinateList) {
                obstacleCoordinateList.add(coordinate);
            }
            delegate.didAddPenalty();
        }
    }

    public void addPenalty(Coordinate coordinate, double penaltyValue) {
        CoordinateType coordinateType = getCoordinateType(coordinate);
        if (coordinateType == CoordinateType.FREE) {
            synchronized (penaltyMap) {
                penaltyMap.put(coordinate, penaltyValue);
            }
            delegate.didAddAnObstacle();
        }
    }

    public void addStart(Coordinate coordinate) {
        CoordinateType coordinateType = getCoordinateType(coordinate);
        if (coordinateType == CoordinateType.FREE) {
            initCoordinate = coordinate;
            delegate.didAddPoint();
        }
    }

    public void addGoal(Coordinate coordinate) {
        CoordinateType coordinateType = getCoordinateType(coordinate);
        if (coordinateType == CoordinateType.FREE) {
            goalCoordinate = coordinate;
            delegate.didAddPoint();
        }
    }

    public void addWaypoint(Coordinate coordinate) {

    }

    public CoordinateType getCoordinateType(Coordinate coordinate) {
        if (!isValidCoordinate(coordinate)) {
            return CoordinateType.INVALID;
        }

        //Integer index = getPointIndexAt(coordinate);
        if (coordinate.equals(initCoordinate)) {
            return CoordinateType.START;
        }

        if (coordinate.equals(goalCoordinate)) {
            return CoordinateType.GOAL;
        }

        if (isObstacle(coordinate)) {
            return CoordinateType.OBSTACLE;
        }

        if (isPath(coordinate)) {
            return CoordinateType.PATH;
        }

        if (isClosed(coordinate)) {
            return CoordinateType.CLOSED;
        }

        Node openNode = getOpenNode(coordinate);
        if (openNode != null) {
            return CoordinateType.OPEN;
        }

        Double penalty = getPenaltyPointAt(coordinate);
        if (penalty != null) {
            return CoordinateType.PENALTY;
        }

        return CoordinateType.FREE;
    }

    private Integer getPointIndexAt(Coordinate coordinate) {
        synchronized (pointList) {
            int index = pointList.indexOf(coordinate);
            if(index >= 0)  {
                return index;
            } else {
                return null;
            }
        }
    }

    //endregion MVC

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
