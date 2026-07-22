package backend.model;
// The helper object for dynamic selection query letting the user add as many conditions as needed.
public class SelectionCondition {
    private final String connector;   // null for first condition since there will be no connector for the first one.
    private final String attribute;
    private final String operator; // private fields so that none of these can be used for sql injection like "DROP TABLE";
    private final String value;

    public SelectionCondition(String connector, String attribute, String operator, String value) {
        this.connector = connector;
        this.attribute = attribute;
        this.operator = operator;
        this.value = value;
    }

    public String getConnector() {
        return connector;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }
}
