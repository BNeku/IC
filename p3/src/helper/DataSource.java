package helper;

import utils.Matrix;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSource {
    private Matrix dataMatrix;
    private List<Matrix> classesData;
    private Matrix centrosMatrix;

    public List<Matrix> getClassesData() {
        return classesData;
    }
    public Matrix getCentrosMatrix(){return centrosMatrix;}

    public void loadData() throws IOException {
        List<List<String>> data = readFile("Iris2Clases.txt");
        loadClassesData(data);
        loadCentrosData(data);
        String stop = "";
    }

    public List<List<String>> readFile(String fileName) throws IOException {
        String filePath = ClassLoader.getSystemResource(fileName).getPath();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String currentLine;
        List<List<String>> data = new ArrayList<>();
        while ((currentLine = reader.readLine()) != null) {
            if (!currentLine.isEmpty()) {
                List<String> line = Arrays.asList(currentLine.split(","));
                data.add(line);
            }
        }
        return data;
    }

    private void loadClassesData(List<List<String>> data) {
        dataMatrix = new Matrix(data.size(), data.get(0).size()-1);
        classesData = new ArrayList<>();
        classesData.add(new Matrix(data.size()/2, data.get(0).size()-1));
        classesData.add(new Matrix(data.size()/2, data.get(0).size()-1));
        for (int i = 0; i < 50; i++) {
            List<String> row = data.get(i);
            for (int j = 0; j < row.size()-1; j++) {
                double value = Double.parseDouble(row.get(j));
                dataMatrix.set(i, j, value);
                classesData.get(0).set(i, j, dataMatrix.get(i, j));
                classesData.get(0).setName(row.get(4));
            }
        }

        for (int i = 50; i < 100; i++) {
            List<String> row = data.get(i);
            for (int j = 0; j < row.size()-1; j++) {
                double value = Double.parseDouble(row.get(j));
                dataMatrix.set(i, j, value);
                classesData.get(1).set(i-50, j, dataMatrix.get(i, j));
                classesData.get(1).setName(row.get(4));
            }
        }
    }

    private void loadCentrosData(List<List<String>> data) {
        centrosMatrix = new Matrix(2, data.get(0).size()-1);
        centrosMatrix.set(0, 0, 4.6);
        centrosMatrix.set(0, 1, 3.0);
        centrosMatrix.set(0, 2, 4.0);
        centrosMatrix.set(0, 3, 0.0);
        centrosMatrix.set(1, 0, 6.8);
        centrosMatrix.set(1, 1, 3.4);
        centrosMatrix.set(1, 2, 4.6);
        centrosMatrix.set(1, 3, 0.7);
    }

    public Matrix getDataMatrix() {
        return dataMatrix;
    }
}
