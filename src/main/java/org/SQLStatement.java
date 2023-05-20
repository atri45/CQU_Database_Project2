package org;

import java.util.List;

public class SQLStatement {
    private final Operation operation;
    private final String tableName;
    private final List<String> columns;
    private final List<String> values;
    private final String condition;

    public SQLStatement(Operation operation, String tableName, List<String> columns, List<String> values, String condition) {
        this.operation = operation;
        this.tableName = tableName;
        this.columns = columns;
        this.values = values;
        this.condition = condition;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<String> getValues() {
        return values;
    }

    public String getCondition() {
        return condition;
    }

    public enum Operation {
        CREATE, DROP, INSERT, DELETE, UPDATE, SELECT
    }

    public void show() {
        System.out.println("operation: " + operation);
        System.out.println("tableName: " + tableName);
        System.out.println("columns: " + columns);
        System.out.println("values: " + values);
        System.out.println("condition: " + condition);
    }
}
