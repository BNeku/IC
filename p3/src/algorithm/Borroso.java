package algorithm;

import utils.Matrix;

import java.text.DecimalFormat;


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
    private String[] algorithmClasses;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public Borroso(Matrix dataMatrix, Matrix initialCentrosMatrix) {
        this.dataMatrix = dataMatrix;
        this.initialCentrosMatrix = initialCentrosMatrix;
        this.algorithmClasses = new String[]{"Iris-setosa", "Iris-versicolor"};
        this.numberOfClasses = algorithmClasses.length;
        this.membershipGradesMatrix = new Matrix(numberOfClasses, dataMatrix.getNumbeOfRows());
        this.distances = new Matrix(numberOfClasses, dataMatrix.getNumbeOfRows());
        boolean shouldIterate;
        do {
            shouldIterate = performIteration();
        } while (shouldIterate);
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

    public String getClassForValues(double[] values) {
        Matrix distances = new Matrix(numberOfClasses,1);
        for (int i = 0; i < numberOfClasses; i++) {
            double distance = calculateDistancesBetween(values, initialCentrosMatrix.getArrayAtRow(i));
            distances.set(i, 0, distance);
        }

        int membershipClassIndex = 0;
        double bestMembership = 0;
        for (int i = 0; i < numberOfClasses; i++) {
            double membershipGrade = calculateMembershipGrade(i, 0, distances);
            if (membershipGrade > bestMembership) {
                bestMembership = membershipGrade;
                membershipClassIndex = i;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        String headerResult = String.format("Clasificación: %s\nGrado de pertenencia: %.3f", algorithmClasses[membershipClassIndex], bestMembership);
        stringBuilder.append(headerResult);

        stringBuilder.append("\n\nCentros\n");
        stringBuilder.append("x1 (");
        for (int i = 0; i < initialCentrosMatrix.getNumberOfColumns(); i++) {
            String comma = "";
            if (i < initialCentrosMatrix.getNumberOfColumns()-1) {
                comma = ", ";
            }

            String value = String.format("%.3f%s ",initialCentrosMatrix.get(0, i), comma);
            stringBuilder.append(value);
        }
        stringBuilder.append(")\n");
        stringBuilder.append("x2 (");
        for (int i = 0; i < initialCentrosMatrix.getNumberOfColumns(); i++) {
            String comma = "";
            if (i < initialCentrosMatrix.getNumberOfColumns()-1) {
                comma = ", ";
            }

            String value = String.format("%.3f%s ",initialCentrosMatrix.get(1, i), comma);
            stringBuilder.append(value);
        }
        stringBuilder.append(")");

        stringBuilder.append("\n\n");
        stringBuilder.append("Grados de pertenencia\n");
        for (int i = 0; i < membershipGradesMatrix.getNumberOfColumns(); i++) {
            String value = String.format("x%d (%.3f, %.3f)\n", i+1, membershipGradesMatrix.get(0, i), membershipGradesMatrix.get(1, i));
            stringBuilder.append(value);
        }

        return stringBuilder.toString();
    }
}
