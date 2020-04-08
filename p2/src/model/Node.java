package model;

import java.util.Map;

public class Node {
    private String value;
    private Map<String, Node> nodes;

    public Node(String value, Map<String, Node> nodes) {
        this.value = value;
        this.nodes = nodes;
    }

    public String getValue() {
        return value;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }
}
