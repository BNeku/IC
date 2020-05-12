package helper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    public static List<String[]> readFile(File file) {
        FileReader in = null;
        try {
            in = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(in);
        List<String[]> data = new ArrayList<>();
        try {
            String line = br.readLine();
            while (line != null) {
                if (!line.isEmpty()) {
                    String[] lineData = line.split(",");
                    data.add(lineData);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
