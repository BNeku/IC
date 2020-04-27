package algorithm;

import model.Node;
import java.util.*;
import java.util.stream.Collectors;

public class ID3 {
    private List<String> attributes;
    private List<List<String>> data;
    private String targetAttribute;
    private static final double LOG2 = Math.log(2.0);

    public ID3(List<String> attributes, List<List<String>> data, String targetAttribute) {
        this.attributes = attributes;
        this.data = data;
        this.targetAttribute = targetAttribute;
    }

    public Node executeID3() {
//        List<String> restosAtributos = attributes.stream().filter(attribute -> attribute != targetAttribute ).collect(Collectors.toList());
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

            int i = 0;
            i++;

//            if (!partitionDataMaxValue.containsKey(value)) {
//                String moreRepeatedValue = getMoreRepeatedValueFromData(targetValues);
//                Node newNode = new Node(moreRepeatedValue, null);
//                nodes.put(value, newNode);
//            } else {
               List<List<String>> partition = partitionDataMaxValue.get(value);
                Node newNode = id3(partition, newRestoAttributos); //Devolvemos ID3(ejemplos restantes, atributos-restantes).
                nodes.put(value, newNode);
//            }
        }

        return new Node(bestAttribute, nodes);
    }

    private String getBestAttribute(List<String> restoAttributos, List<List<String>> mData) {
        Double size = new Double(mData.size());
        Double minMerito = null;
        String bestAttribute = null;
        Map<String, List<List<String>>> partitionDataMaxValue = null;

        //Calculamos el mejor elemento, es decir, aquel con menor mérito.
        for (String value : restoAttributos) {
            if (value.equals("Jugar")) {
                continue;
            }
            Map<String, List<List<String>>> partitionData = getPartitionFromData(mData, value); // Se divide el conjunto.
            Double merito = 0.0;

            for (Map.Entry<String, List<List<String>>> entry : partitionData.entrySet()) {
                int partitionN = entry.getValue().size(); //Porque estamos incluyendo también el valor
                Map<String, Integer> partitionTargetValues = getTargetValuesFromData(entry.getValue(), value);
                Double infor = getInfor(entry.getValue());
//                System.out.println("(partitionN/size) " + (partitionN/size) + " HipartitionEntropy " +partitionEntropy);
                merito += (partitionN/size) * infor; // Calculamos el mérito de cada rama
            }

            if (minMerito == null || minMerito > merito) {
                minMerito = merito;
                bestAttribute = value;
                partitionDataMaxValue = partitionData;
            }
        }

        return bestAttribute;
    }

    /**
     * Implementamos el algoritmo siguiendo el pseudocódigo de la página 21 - Tema 04 Aprendizaje II.
     * @param data los ejemplos que definen las reglas
     * @param restoAttributos atributos restantes a filtrar
     * @return Nodo raíz del subárbol creado.
     */
//    private Node id3(List<List<String>> data, List<String> restoAttributos, String targetAttribute, Map<String, Integer> targetValues) {
////        Map<String, Integer> targetValues = getTargetValuesFromData(data, targetAttribute); //Pasar primer atributo por defecto
//
//        if (targetValues.size() == 1) {
//            Map.Entry<String, Integer> entry = targetValues.entrySet().iterator().next();
//            String key = entry.getKey();
//            return new Node(key, null);
//        }
//
//        //Si la lista está vacía, regresamos.
//        if (targetValues.isEmpty()) {
//            String key = getMoreRepeatedValueFromData(targetValues);
//            return new Node(key, null);
//        }
//
//        Double size = new Double(data.size());
//        Double minMerito = null;
//        String attributesMaxValue = null;
//        Map<String, List<List<String>>> partitionDataMaxValue = null;
//
//        //Calculamos el mejor elemento, es decir, aquel con menor mérito.
//        for (String value : restoAttributos) {
//            Map<String, List<List<String>>> partitionData = getPartitionFromData(data, value); // Se divide el conjunto.
//            Double merito = 0.0;
//
//            for (Map.Entry<String, List<List<String>>> entry : partitionData.entrySet()) {
//                int partitionN = entry.getValue().size();
//                Map<String, Integer> partitionTargetValues = getTargetValuesFromData(entry.getValue(), value);
//                Double partitionEntropy = getEntropy(partitionN, partitionTargetValues);
////                System.out.println("(partitionN/size) " + (partitionN/size) + " HipartitionEntropy " +partitionEntropy);
//                merito += (partitionN/size) * partitionEntropy; // Calculamos el mérito de cada rama
//            }
//
//
//            if (minMerito == null || minMerito > merito) {
//                minMerito = merito;
//                attributesMaxValue = value;
//                partitionDataMaxValue = partitionData;
//            }
//        }
//
//        //Condición de parada, hemos llegado a una hoja (nodo sin hijos), hora de regresar.
//        if (minMerito == null || attributesMaxValue == null || partitionDataMaxValue == null && !targetAttribute.isEmpty()) {
//            String moreRepeatedValue = getMoreRepeatedValueFromData(targetValues);
//            return new Node(moreRepeatedValue, null);
//        }
//
//        /**
//         * Preparamos subárbol cuya raíz es el mejor atributo.
//         */
//        Map<String, Node> nodes = new HashMap<>();
//        String finalAttributesMaxValue = attributesMaxValue;
//
//        //Los nuevos atributos a explorar.
//        List<String> newRestoAttributos = restoAttributos.stream().filter(attribute -> attribute != finalAttributesMaxValue).collect(Collectors.toList());
//
//        //Obtenemos los atributos del mejor. (Por ejemplo, de temperatura obtenemos, caluroso, frío, templado).
//        Set<String> noRepeatedValues = getNoRepeatedValues().get(attributesMaxValue);
//
//        //Para cada valor vi de mejor, íncluimos en ejemplos-restantes los elementos de la lista-ejemplos que tengan valor vi del atributo mejor.
//        for (String value : noRepeatedValues) {
//            if (!partitionDataMaxValue.containsKey(value)) {
//                String moreRepeatedValue = getMoreRepeatedValueFromData(targetValues);
//                Node newNode = new Node(moreRepeatedValue, null);
//                nodes.put(value, newNode);
//            } else {
//               List<List<String>> partition = partitionDataMaxValue.get(value);
//                Node newNode = id3(partition, newRestoAttributos, finalAttributesMaxValue); //Devolvemos ID3(ejemplos restantes, atributos-restantes).
//                nodes.put(value, newNode);
//            }
//        }
//        return new Node(attributesMaxValue, nodes);
//    }

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

    /**
     * Obtenemos el valor que más se repite.
     * @param data Datos desde donde se extraerá el valor que más se repite.
     * @return Devuelve el valor que más se repite.
     */
    private String getMoreRepeatedValueFromData(Map<String, Integer> data) {
        String moreRepeatedValue = null;
        Integer max = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                moreRepeatedValue = entry.getKey();
            }
        }
        return moreRepeatedValue;
    }

    /**
     * Calcula la entropía aplicando el algoritmo página 19, Entropía.
     * @param n Número total de datos.
     * @param data Datos a partir de los cuales se calculará la entropía.
     * @return Devuelve la entropía de los datos.
     */
    private Double getEntropy(int n, Map<String, Integer> data) {
        Double entropy = 0.0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            Double pX = new Double(entry.getValue()) / n;
            Double log2 = getBase2Log(pX);
            entropy += -pX * log2;
        }
        return entropy;
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
