package org;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLParser {
    public static SQLStatement parse(String sql) {
        System.out.println("开始执行sql：" + sql);
        if (sql.startsWith("CREATE")) {
            return buildCreateTableStatement(sql.substring(13));
        }
        else if (sql.startsWith("DROP")) {
            return buildDropTableStatement(sql.substring(11));
        }
        else {
            SqlParser sqlParser = SqlParser.create(sql);
            try {
                SqlNode sqlNode = sqlParser.parseStmt();
                return buildSQLStatement(sqlNode);
            } catch (SqlParseException e) {
                throw new IllegalArgumentException("Invalid SQL statement: " + sql, e);
            }
        }
    }

    private static SQLStatement buildSQLStatement(SqlNode sqlNode) {
        SqlKind sqlKind = sqlNode.getKind();
        switch (sqlKind) {
            case INSERT:
                return buildInsertStatement((SqlInsert) sqlNode);
            case DELETE:
                return buildDeleteStatement((SqlDelete) sqlNode);
            case UPDATE:
                return buildUpdateStatement((SqlUpdate) sqlNode);
            case SELECT:
                return buildSelectStatement((SqlSelect) sqlNode);
            default:
                throw new IllegalArgumentException("Unsupported SQL statement: " + sqlNode);
        }
    }

    private static SQLStatement buildCreateTableStatement(String tableName) {
        return new SQLStatement(SQLStatement.Operation.CREATE, tableName, null, null, null);
    }

    private static SQLStatement buildDropTableStatement(String tableName) {
        return new SQLStatement(SQLStatement.Operation.DROP, tableName, null, null, null);
    }

    private static SQLStatement buildInsertStatement(SqlInsert insert) {
        String tableName = insert.getTargetTable().toString();
        List<String> columns = new ArrayList<>();
        List<String> values;

        SqlNodeList columnList = insert.getTargetColumnList();
        SqlNode valueList = insert.getSource();

        for (int i = 0; i < columnList.size(); i++) {
            SqlIdentifier column = (SqlIdentifier) columnList.get(i);
            String columnName = column.getSimple();
            columns.add(columnName);
        }

        String valueString = valueList.toString().replace("'", "");
        values = Arrays.asList(valueString.substring("[VALUES ROW(".length() - 1, valueString.length() - 1).split(","));
        return new SQLStatement(SQLStatement.Operation.INSERT, tableName, columns, values, null);
    }

    private static SQLStatement buildDeleteStatement(SqlDelete delete) {
        String tableName = delete.getTargetTable().toString();
        String condition = parseCondition(delete.getCondition());
        return new SQLStatement(SQLStatement.Operation.DELETE, tableName, null, null, condition);
    }

    private static SQLStatement buildUpdateStatement(SqlUpdate update) {
        String tableName = update.getTargetTable().toString();
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        String condition = null;

        SqlNodeList updateList = update.getTargetColumnList();
        SqlNodeList valueList = update.getSourceExpressionList();
        SqlNode conditionNode = update.getCondition();

        for (int i = 0; i < updateList.size(); i++) {
            SqlIdentifier column = (SqlIdentifier) updateList.get(i);
            String columnName = column.getSimple();
            columns.add(columnName);

            SqlNode valueNode = valueList.get(i);
            String value = valueNode.toString();
            values.add(value);
        }

        if (conditionNode != null) {
            condition = parseCondition(conditionNode);
        }

        return new SQLStatement(SQLStatement.Operation.UPDATE, tableName, columns, values, condition);
    }

    private static SQLStatement buildSelectStatement(SqlSelect select) {
        String tableName = select.getFrom().toString();
        String condition = null;

        SqlNode whereNode = select.getWhere();
        if (whereNode != null) {
            condition = parseCondition(whereNode);
        }

        return new SQLStatement(SQLStatement.Operation.SELECT, tableName, null, null, condition);
    }

    public static String parseCondition(SqlNode conditionNode) {
        String condition = conditionNode.toString();
        if (condition.matches(".*\\d$")) {
            condition = condition.replace("`", "");
        }
        else {
            condition = condition.replace("`", "'");
            condition = condition.replaceFirst("'", "").replaceFirst("'", "");
        }
        return condition;
    }
}
