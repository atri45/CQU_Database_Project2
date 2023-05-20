package org;

public class Database {
    public static String databaseName;
    public static String databasePath;
    //public static String databaseInfoPath;

    public Database(String databaseName, String databasePath) {
        Database.databaseName = databaseName;
        Database.databasePath = databasePath;
    }

    // TODO 展示database metadata
    /*
    public List<String> getTableList() throws DocumentException {
        List<String> tableList = new ArrayList<>();
        Document document = loadDocument(databaseInfoPath);
        Element rootElement = document.getRootElement();
        List<Element> tables = rootElement.elements("table");
        for (Element table : tables) {
            tableList.add(table.attributeValue("name"));
        }
        return tableList;
    }

    private void updateDatabaseInfo(String tableName, boolean add) throws DocumentException, IOException {
        Document document;
        File file = new File(databaseInfoPath);
        if (file.exists()) {
            document = loadDocument(databaseInfoPath);
        } else {
            document = DocumentHelper.createDocument();
            Element rootElement = document.addElement("database");
            saveDocument(document, databaseInfoPath);
        }
        Element rootElement = document.getRootElement();
        List<Element> tables = rootElement.elements("table");
        boolean tableExists = false;
        for (Element table : tables) {
            if (table.attributeValue("name").equals(tableName)) {
                tableExists = true;
                if (!add) {
                    table.detach();
                }
                break;
            }
        }
        if (!tableExists && add) {
            Element tableElement = rootElement.addElement("table");
            tableElement.addAttribute("name", tableName);
            tableElement.addElement("columns");
            tableElement.addElement("rows").setText("0");
        }
        saveDocument(document, databaseInfoPath);
    }

    private void updateTableInfo(String tableName, int count) throws DocumentException, IOException {
        Document document = loadDocument(databaseInfoPath);
        Element rootElement = document.getRootElement();
        List<Element> tables = rootElement.elements("table");
        for (Element table : tables) {
            if (table.attributeValue("name").equals(tableName)) {
                Element rowsElement = table.element("rows");
                int rowCount = Integer.parseInt(rowsElement.getText()) + count;
                rowsElement.setText(String.valueOf(rowCount));
                break;
            }
        }
        saveDocument(document, databaseInfoPath);
    }*/
}