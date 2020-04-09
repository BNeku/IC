package algorithm;

import model.Node;

import java.util.*;
import java.util.stream.Collectors;

public class ID3 {
    private List<String> attributes;
    private List<List<String>> data;
    private String targetAttribute;

    public ID3(List<String> attributes, List<List<String>> data, String targetAttribute) {
        this.attributes = attributes;
        this.data = data;
        this.targetAttribute = targetAttribute;
    }

    public Node executeID3() {
        List<String> noTargetAttributes = attributes.stream().filter(attribute -> attribute != targetAttribute ).collect(Collectors.toList());
        return id3(data, noTargetAttributes);
    }

    /**
     * Implementamos el algoritmo siguiendo el pseudocódigo de la página 21 - Tema 04 Aprendizaje II.
     *
     * @param data los atributos que definen las reglas
     * @param restoAttributos atributos restantes a filtrar
     * @return Nodo raíz del subárbol creado.
     */
    private Node id3(List<List<String>> data, List<String> restoAttributos) {
        Map<String, Integer> targetValues = getTargetValuesFromData(data, targetAttribute);

        if (targetValues.size() == 1) {
            Map.Entry<String, Integer> entry = targetValues.entrySet().iterator().next();
            String key = entry.getKey();
            return new Node(key, null);
        }

        //Si la lista está vacía, regresamos.
        if (targetValues.isEmpty()) {
            String key = getMoreRepeatedValueFromData(targetValues);
            return new Node(key, null);
        }

        int size = data.size();
        double entropy = getEntropy(size, targetValues);
        Double maxValue = null;
        String attributesMaxValue = null;
        Map<String, List<List<String>>> partitionDataMaxValue = null;

        /**
         * La ganancia e información se basa en el decremento de la entropía cuando el conjunto de datos se divide en los valores de un atributo.
         * Se calcula la entropía del total
         * Se divide el conjunto de ddatos en función de los diferenes atributos.
         * Se calcula la entropía de cada rama y se suman proporcionalmente las ramas para alcular la entropía del total.
         * Se resta el resultado de la entropía original.
         * El resultado es la ganancia de la información.
         * El atributo con mayor ganancia es seleccionado como nodo de decisión.
         * Fuente: https://es.slideshare.net/FernandoCaparrini/arboles-decision-id3
         */
        for (String value : restoAttributos) {
            Map<String, List<List<String>>> partitionData = getPartitionFromData(data, value); // Se divide el conjunto.
            double averageEntropy = 0;

            for (Map.Entry<String, List<List<String>>> entry : partitionData.entrySet()) {
                int partitionN = entry.getValue().size();
                Map<String, Integer> partitionTargetValues = getTargetValuesFromData(entry.getValue(), value);
                double partitionEntropy = getEntropy(partitionN, partitionTargetValues);
                averageEntropy += partitionN / size * partitionEntropy; // Calculamos entropía de cada rama y la sumamos proporcionalmente.
            }


            //Entropía total
            double result = entropy - averageEntropy;
            if (maxValue == null || result > maxValue) {
                maxValue = result;
                attributesMaxValue = value;
                partitionDataMaxValue = partitionData;
            }
        }

        if (maxValue == null || attributesMaxValue == null || partitionDataMaxValue == null) {
            String moreRepeatedValue = getMoreRepeatedValueFromData(targetValues);
            return new Node(moreRepeatedValue, null);
        }

        Map<String, Node> nodes = new HashMap<>();
        List<String> newRestoAttributos = getRestoAttributesFromData(restoAttributos, attributesMaxValue);
        Set<String> noRepeatedValues = getNoRepeatedValues().get(attributesMaxValue);
        for (String value : noRepeatedValues) {
            if (!partitionDataMaxValue.containsKey(value)) {
                String moreRepeatedValue = getMoreRepeatedValueFromData(targetValues);
                Node newNode = new Node(moreRepeatedValue, null);
                nodes.put(value, newNode);
            } else {
               List<List<String>> partition = partitionDataMaxValue.get(value);
                Node newNode = id3(partition, newRestoAttributos);
                nodes.put(value, newNode);
            }
        }
        return new Node(attributesMaxValue, nodes);
    }

    private List<String> getRestoAttributesFromData(List<String> data, String noTargetValue) {
        List<String> result = new ArrayList<>();
        for (String value : data) {
            if (value != noTargetValue) {
                result.add(value);
            }
        }
        return result;
    }

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
     * Obtiene los attributos a partir del atributo dado.
     * @param data Los datos a partir del cual se va a obtener la partición.
     * @param attribute El atributo a partir del cual se va a crear el subconjunto.
     * @return devuelve los atributos seleccionados.
     */
    private Map<String, Integer> getTargetValuesFromData(List<List<String>> data, String attribute) {
        Map<String, Integer> values = new HashMap<>();
        int attributeIndex = attributes.indexOf(attribute);
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
            if (entry.getValue() >= max) {
                max = entry.getValue();
                moreRepeatedValue = entry.getKey();
            }
        }
        return moreRepeatedValue;
    }

    /**
     * Calccula la entropía a partir de unos datos datos.
     * @param n Número total de datos.
     * @param data Datos a partir de los cuales se calculará la entropía.
     * @return Devuelve la entropía de los datos.
     */
    private double getEntropy(int n, Map<String, Integer> data) {
        double entropy = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            double pX = entry.getValue()/n;
            double log = Math.log(pX);
            double log2 = log/Math.log(2.0);
            entropy += -pX * log2;
        }
        return entropy;
    }
}
