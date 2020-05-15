package algorithm;

import utils.Matrix;


public class Borroso {
    private Matrix dataMatrix;
    private Matrix initialCentrosMatrix;
    private Matrix membershipGradesMatrix;
    private int numberOfClasses;
    private Matrix distances;
    //Peso exponencial
    private int b = 2;
    //Tolerancia
    private double epsilon = 0.01;

    public Borroso(Matrix dataMatrix, Matrix initialCentrosMatrix, int numberOfClasses) {
        this.dataMatrix = dataMatrix;
        this.initialCentrosMatrix = initialCentrosMatrix;
        this.numberOfClasses = numberOfClasses;
        this.membershipGradesMatrix = new Matrix(numberOfClasses, dataMatrix.getNumbeOfRows());
        this.distances = new Matrix(numberOfClasses, dataMatrix.getNumbeOfRows());

        boolean shouldIterate;
        do {
            shouldIterate = performIteration();
        } while (shouldIterate);

        System.out.println("Terminé");
    }

    private boolean performIteration() {
        for (int i = 0; i < numberOfClasses; i++) {
            for (int j = 0; j < dataMatrix.getNumbeOfRows(); j++) {
                double distanceValue = calculateDistancesBetween(dataMatrix.getArrayAtRow(j), initialCentrosMatrix.getArrayAtRow(i));
                distances.set(i, j, distanceValue);
            }
        }

        for (int i = 0; i < numberOfClasses; i++) {
            for (int j = 0; j < dataMatrix.getNumbeOfRows(); j++) {
                double membershipgrade = calculateMembershipGrade(i, j, distances);
                membershipGradesMatrix.set(i, j, membershipgrade);
            }
        }

        Matrix copyCentrosMatrix = initialCentrosMatrix;
        initialCentrosMatrix = new Matrix(numberOfClasses, dataMatrix.getNumberOfColumns());
        for (int i = 0; i < numberOfClasses; i++) {
            for (int columnIndex = 0; columnIndex < dataMatrix.getNumberOfColumns(); columnIndex++) {
                double dividendo = 0;
                double divisor = 0;
                for (int rowIndex = 0; rowIndex < dataMatrix.getNumbeOfRows(); rowIndex++) {
                    double root = Math.pow(membershipGradesMatrix.get(i, rowIndex), b);
                    dividendo += root*dataMatrix.get(rowIndex, columnIndex);
                    divisor += root;
                }
                double centroValue = dividendo/divisor;
                initialCentrosMatrix.set(i, columnIndex, centroValue);
            }
        }

        for (int i = 0; i < numberOfClasses; i++) {
            double distance = calculateDistancesBetween(initialCentrosMatrix.getArrayAtRow(i), copyCentrosMatrix.getArrayAtRow(i));
            double distanceSqrt = Math.sqrt(distance);
            if (distanceSqrt >= epsilon) {
                return true;
            }
        }

        return false;
    }

    private double calculateDistancesBetween(double[] xArray, double[] vArray) {
        double result = 0;
        for (int i = 0; i < xArray.length; i++) {
            result += Math.pow(xArray[i]-vArray[i], 2.0);
        }
        return result;
    }

    private double calculateMembershipGrade(int i, int j, Matrix distances) {
        double value1 = Math.pow((1/distances.get(i, j)), (1/ (b-1)));
        double dividerValue = 0;
        for (int index = 0; index < numberOfClasses; index++) {
            dividerValue += (1/distances.get(index, j));
        }
        double divider = Math.pow(dividerValue, (1/(b-1)));
        double result =  (value1/divider);
        return (value1/divider);
    }



    private void createUMatrix(int row) {
        System.out.println("Stop");
        for (int i = 0; i < dataMatrix.getNumberOfColumns(); i++) {
//            double d =
        }
    }
}
