package algorithm;

import model.Node;
import java.util.*;
import java.util.stream.Collectors;

public class ID3 {
    private List<String> attributes;
    private List<List<String>> data;
    private static final double LOG2 = Math.log(2.0);

    public ID3(List<String> attributes, List<List<String>> data) {
        this.attributes = attributes;
        this.data = data;
    }

    public Node executeID3() {
         return id3(data, attributes);
    }

    private boolean allAreSameClass(List<List<String>> data) {
        String currentClass = null;
        for (List<String> row : data) {
            String attribute = row.get(row.size()-1);
            if (currentClass == null) {
                currentClass = attribute;
            } else if (!currentClass.equals(attribute)) {
                return false;
            }
        }
        return true;
    }

    private String getClass(List<List<String>> data) {
        List<String> row = data.get(0);
        return row.get(row.size()-1);
    }

    private Node id3(List<List<String>> mData, List<String> restoAttributos) {
        if (restoAttributos.isEmpty() || mData == null) {
            return null;
        }

        if (allAreSameClass(mData)) {
            String value = getClass(mData);
            return new Node(value, null);
        }

        String bestAttribute = getBestAttribute(restoAttributos, mData);

        //Obtenemos los atributos del mejor. (Por ejemplo, de TiempoExterior obtenemos, soleado, lluvioso, nublado).
        Set<String> noRepeatedValues = getNoRepeatedValues().get(bestAttribute);

        //Los nuevos atributos a explorar.
        List<String> newRestoAttributos = restoAttributos.stream().filter(attribute -> attribute != bestAttribute).collect(Collectors.toList());

        Map<String, List<List<String>>> partitionDataMaxValue = getPartitionFromData(mData, bestAttribute);

        /**
         * Preparamos subárbol cuya raíz es el mejor atributo.
         */
        Map<String, Node> nodes = new HashMap<>();

        for (String value : noRepeatedValues) {
               List<List<String>> partition = partitionDataMaxValue.get(value);
                Node newNode = id3(partition, newRestoAttributos); //Devolvemos ID3(ejemplos restantes, atributos-restantes).
                nodes.put(value, newNode);
        }

        return new Node(bestAttribute, nodes);
    }

    private String getBestAttribute(List<String> restoAttributos, List<List<String>> mData) {
        Double size = new Double(mData.size());
        Double minMerito = null;
        String bestAttribute = null;

        //Calculamos el mejor elemento, es decir, aquel con menor mérito.
        for (String value : restoAttributos) {
            if (value.equals("Jugar")) {
                continue;
            }
            Map<String, List<List<String>>> partitionData = getPartitionFromData(mData, value); // Se divide el conjunto.
            Double merito = 0.0;

            for (Map.Entry<String, List<List<String>>> entry : partitionData.entrySet()) {
                int partitionN = entry.getValue().size(); //Porque estamos incluyendo también el valor
                Double infor = getInfor(entry.getValue());
                merito += (partitionN/size) * infor; // Calculamos el mérito de cada rama
            }

            if (minMerito == null || minMerito > merito) {
                minMerito = merito;
                bestAttribute = value;
            }
        }

        return bestAttribute;
    }

    /**
     * Obtiene la lista de de los atributos (Viento, TiempoExterior, Jugar, Humedad, Temperatura).
     * @return Devuelve la lista de atributos.
     */
    private Map<String, Set<String>> getNoRepeatedValues() {
        Map<String, Set<String>> result = new HashMap<>();
        for (String attribute: attributes) {
            result.put(attribute, new HashSet<>());
        }

        for (List<String> dataArray : data) {
            for (int i = 0; i < dataArray.size(); i++) {
                String attribute = attributes.get(i);
                String value = dataArray.get(i);
                result.get(attribute).add(value);
            }
        }

        return result;
    }

    /**
     * Calcula el número de repeticiones que tiene cada uno de los posibles atributos del atributo dado.
     * Por ejemplo: targetAttribute = Temperatura --> caluroso(4), templado(6), frío(4).
     * @param data Los datos a partir del cual se van a extraer los atributos.
     * @param targetAttribute El atributo objetivo.
     * @return Los atributos del target y el valor de sus repeticiones.
     */
    private Map<String, List<List<String>>> getPartitionFromData(List<List<String>> data, String targetAttribute) {
        Map<String, List<List<String>>> partitionResultData = new HashMap<>();
        int attributeIndex = attributes.indexOf(targetAttribute);
        for (List<String> attributeArray : data) {
            String value = attributeArray.get(attributeIndex);
            if (partitionResultData.containsKey(value)) {
                partitionResultData.get(value).add(attributeArray);
            } else {
                List newList = new ArrayList();
                newList.add(attributeArray);
                partitionResultData.put(value, newList);
            }
        }

        return partitionResultData;
    }

    /**
     * Obtiene los ejemplos a partir del atributo dado. Data filas de la matriz de ejemplos (ojo solo filas restantes en el atributo pasado por parámetro).
     * Ejemplo: Temperatura -> Caluroso(4).
     * @param data Los datos a partir del cual se va a obtener la partición.
     * @param attribute El atributo a partir del cual se va a crear el subconjunto.
     * @return devuelve los atributos seleccionados.
     */
    private Map<String, Integer> getTargetValuesFromData(List<List<String>> data, String attribute) {
        Map<String, Integer> values = new HashMap<>();
        int attributeIndex = attributes.indexOf(attribute);
        if (attributeIndex == -1) {
            System.out.println("Stop");
        }
        for (List<String> dataAttributes : data) {
            String selectedAttribute = dataAttributes.get(attributeIndex);
            if (values.containsKey(selectedAttribute)) {
                Integer newValue = values.get(selectedAttribute);
                newValue += 1;
                values.put(selectedAttribute, newValue);
            } else {
                values.put(selectedAttribute, 1);
            }
        }

        return values;
    }

    private Double getInfor(List<List<String>> entry) {
        double pX = 0;
        double nX = 0;
        int N = entry.size();
        for (List<String> mList : entry) {
            if (mList.contains("si")) {
                pX++;
            } else {
                nX++;
            }
        }
        double p = -pX/N*getBase2Log(pX/N);
        double n = -nX/N*getBase2Log(nX/N);
       return p+n;
    }

        /**
         * Calcula el logaritmo en base 2 de un valor.
         * @param x valor cuyo algoritmo se va a calcular.
         * @return el logagritmo en base 2 de x.
         */
    public static Double getBase2Log(Double x) {
        return x == 0 ? 0 : (Math.log(x) / LOG2);
    }
}
