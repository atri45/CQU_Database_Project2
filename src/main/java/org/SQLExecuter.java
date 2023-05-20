package org;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLExecuter {
    public static Database db;

    public SQLExecuter(Database database) {
        this.db = database;
    }

    public static void execute(SQLStatement statement) throws Exception {
        String tableName = statement.getTableName();
        switch (statement.getOperation()) {
            case CREATE:
                createTable(tableName);
                break;
            case DROP:
                dropTable(tableName);
                break;
            case INSERT:
                List<String> columns = statement.getColumns();
                List<String> values = statement.getValues();
                insertData(tableName, columns, values);
                break;
            case DELETE:
                String condition = statement.getCondition();
                deleteData(tableName, condition);
                break;
            case UPDATE:
                columns = statement.getColumns();
                values = statement.getValues();
                condition = statement.getCondition();
                updateData(tableName, columns, values, condition);
                break;
            case SELECT:
                condition = statement.getCondition();
                selectData(tableName, condition);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation");
        }
    }

    public static void createTable(String tableName) throws Exception {
        // 创建文件夹
        File directory = new File(db.databasePath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        // 创建document
        Document document = DocumentHelper.createDocument();
        document.addElement(tableName);
        // 将document转化为xml文件
        saveDocument(document, getTablePath(tableName));
        System.out.println("sql执行结束");
    }

    public static void dropTable(String tableName) throws Exception {
        String tablePath = getTablePath(tableName);
        File file = new File(tablePath);
        if (file.exists()) {
            file.delete();
        }
        System.out.println("sql执行结束");
    }

    public static void insertData(String tableName, List<String> columns, List<String> values) throws IOException, DocumentException {
        Document document = loadDocument(getTablePath(tableName));
        Element rootElement = document.getRootElement();
        Element rowElement = rootElement.addElement("row");
        rowElement.addAttribute("rowID", generateRowId(rootElement));
        for (int i = 0; i < columns.size(); i++) {
            rowElement.addElement(columns.get(i)).setText(values.get(i));
        }
        saveDocument(document, getTablePath(tableName));
        System.out.println("sql执行结束");
    }

    public static void deleteData(String tableName, String condition) throws IOException, DocumentException {
        Document document = loadDocument(getTablePath(tableName));
        Element rootElement = document.getRootElement();
        String xpathExpression = "//row" + (condition != null ? "[" + condition + "]" : "");
        List<Node> nodes = rootElement.selectNodes(xpathExpression);
        for (Node node : nodes) {
            node.detach();
        }
        saveDocument(document, getTablePath(tableName));
        System.out.println("sql执行结束");
    }

    public static void updateData(String tableName, List<String> columns, List<String> values, String condition) throws IOException, DocumentException {
        Document document = loadDocument(getTablePath(tableName));
        Element rootElement = document.getRootElement();
        String xpathExpression = "//row" + (condition != null ? "[" + condition + "]" : "");
        List<Node> nodes = rootElement.selectNodes(xpathExpression);
        int count = 0;
        for (Node node : nodes) {
            Element element = (Element) node;
            for (int i = 0; i < columns.size(); i++) {
                Element columnElement = element.element(columns.get(i));
                if (columnElement != null) {
                    columnElement.setText(values.get(i));
                    count++;
                }
            }
        }
        saveDocument(document, getTablePath(tableName));
        System.out.println("sql执行结束");
    }

    public static void selectData(String tableName, String condition) throws DocumentException {
        Document document = loadDocument(getTablePath(tableName));
        String xpathExpression = "//row" + (condition != null ? "[" + condition + "]" : "");
        List<Node> nodes = document.selectNodes(xpathExpression);
        for (Node node : nodes) {
            Element recordElement = (Element) node;
            Map<String, String> rowData = new HashMap<>();
            for (Element fieldElement : recordElement.elements()) {
                if (fieldElement != null) {
                    rowData.put(fieldElement.getName(), fieldElement.getText());
                }
            }
            System.out.println(rowData);
        }
        System.out.println("sql执行结束");
    }

    private static String generateRowId(Element rootElement) {
        int rowCount = rootElement.elements("row").size();
        return String.valueOf(rowCount);
    }

    public static String getTablePath(String tableName) {
        return db.databasePath + "/" + tableName + ".xml";
    }

    private static Document loadDocument(String filePath) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(new File(filePath));
    }

    private static void saveDocument(Document document, String filePath) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        format.setIndentSize(4);
        XMLWriter writer = new XMLWriter(new FileWriter(filePath), format);
        writer.write(document);
        writer.close();
    }
}
