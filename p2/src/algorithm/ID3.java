package algorithm;

import model.Node;

import java.util.*;
import java.util.stream.Collectors;

public class ID3 {
    private List<String> attributes;
    private List<String[]> data;
    private String targetAttribute;

    public Node executteID3() {
        List<String> noTargetAttributes = attributes.stream().filter(attribute -> attribute != targetAttribute ).collect(Collectors.toList());
        List<List<String>> dataList = new ArrayList<>();
        for (String[] arrayData : data) {
            dataList.add(Arrays.asList(arrayData));
        }
        return id3(dataList, noTargetAttributes);
    }

    private Node id3(List<List<String>> data, List<String> restoAttributos) {
        List dataList = new ArrayList();
        dataList.add(Arrays.asList(Arrays.asList(data)));
        Map<String, Integer> targetValues = getTargetValuesFromData(dataList, targetAttribute);

        if (targetValues.size() == 1) {
            Map.Entry<String, Integer> entry = targetValues.entrySet().iterator().next();
            String key = entry.getKey();
            return new Node(key, null);
        }

        if (targetValues.isEmpty()) {
            String key = getMoreRepeatedValueFromData(targetValues);
            return new Node(key, null);
        }

        int size = data.size();
        double entropy = getEntropy(size, targetValues);
        Double maxValue = null;
        String attributesMaxValue = null;
        Map<String, List<List<String>>> partitionDataMaxValue = null;

        for (String value : restoAttributos) {
            Map<String, List<List<String>>> partitionData = getPartitionFromData(data, value);
            double averageEntropy = 0;

            for (Map.Entry<String, List<List<String>>> entry : partitionData.entrySet()) {
                int partitionN = entry.getValue().size();
                Map<String, Integer> partitionTargetValues = getTargetValuesFromData(entry.getValue(), value);
                double partitionEntropy = getEntropy(partitionN, partitionTargetValues);
                averageEntropy += partitionN / size * partitionEntropy;
            }

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
        return null;
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

        for (String[] dataArray : data) {
            for (int i = 0; i < dataArray.length; i++) {
                String attribute = attributes.get(i);
                String value = dataArray[i];
                result.get(attribute).add(value);
            }
        }

        return result;
    }

    private Map<String, List<List<String>>> getPartitionFromData(List<List<String>> data, String targetAttribute) {
        Map<String, List<List<String>>> partitionResultData = new HashMap<>();
        int attributeIndex = attributes.indexOf(targetAttribute);
        for (List<String> attributeArray : data) {
            String value = attributeArray.get(attributeIndex);
            if (partitionResultData.containsKey(value)) {
                partitionResultData.get(value).add(attributeArray);
            } else {
                List newList = new ArrayList();
                newList.add(Arrays.asList(attributeArray));
                partitionResultData.put(value, newList);
            }
        }

        return partitionResultData;
    }

    private Map<String, Integer> getTargetValuesFromData(List<List<String>> data, String attribute) {
        Map<String, Integer> values = new HashMap<>();
        int attributeIndex = attributes.indexOf(attribute);
        for (List<String> attributes : data) {
            String selectedAttribute = attributes.get(attributeIndex);
            if (values.containsKey(selectedAttribute)) {
                Integer newValue = values.get(selectedAttribute);
                newValue += 1;
                values.put(selectedAttribute, newValue);
            } else {
                values.put(selectedAttribute, 1);
            }
        }
        return  values;
    }

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

    private double getEntropy(int n, Map<String, Integer> data) {
        double entropy = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            double pX = entry.getValue();
            double log = Math.log(pX);
            double log2 = log/Math.log(2);
            entropy += -pX * log2;
        }
        return entropy;
    }
}
