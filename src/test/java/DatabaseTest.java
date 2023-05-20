import org.Database;
import org.SQLExecuter;
import org.SQLParser;
import org.SQLStatement;
import org.junit.Ignore;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class DatabaseTest {
    public DatabaseTest() throws IOException {}
    
    public String getDatabasePath() throws IOException {
        File directory = new File("");
        return directory.getCanonicalPath()+"/target/classes/org/database";
    }
    
    Database db = new Database("CQU-DB", getDatabasePath());
    SQLExecuter sqlExecuter = new SQLExecuter(db);

    @Test
    @Ignore
    public void testGetDataBasePath() throws IOException {
        String databasePath = getDatabasePath();
        assertEquals("D:\\Mywork\\CQU_Database_Project2/target/classes/org/database", databasePath);
    }

    @Test
    public void testCreateTable() throws Exception {
        String createSQL1 = "CREATE TABLE TEST";
        SQLStatement createStatement1 = SQLParser.parse(createSQL1);
        sqlExecuter.execute(createStatement1);
        String createSQL2 = "CREATE TABLE TEST2";
        SQLStatement createStatement2 = SQLParser.parse(createSQL2);
        sqlExecuter.execute(createStatement2);
    }

    @Test
    public void testDropTable() throws Exception {
        String dropSQL1 = "DROP TABLE TEST";
        SQLStatement dropStatement1 = SQLParser.parse(dropSQL1);
        sqlExecuter.execute(dropStatement1);
        String dropSQL2 = "DROP TABLE TEST2";
        SQLStatement dropStatement2 = SQLParser.parse(dropSQL2);
        sqlExecuter.execute(dropStatement2);
    }

    @Test
    public void testInsertData() throws Exception {
        if (!new File(sqlExecuter.getTablePath("TEST")).exists()) {
            sqlExecuter.createTable("TEST");
        }
        String insertSQL1 = "INSERT INTO test (id, name, age) VALUES (1, 'Mike', 19)";
        SQLStatement insertStatement1 = SQLParser.parse(insertSQL1);
        sqlExecuter.execute(insertStatement1);
        String insertSQL2 = "INSERT INTO test (id, name, age) VALUES (2, 'Adam', 33)";
        SQLStatement insertStatement2 = SQLParser.parse(insertSQL2);
        sqlExecuter.execute(insertStatement2);
        String insertSQL3 = "INSERT INTO test (id, name, age) VALUES (3, 'Hertz', 20)";
        SQLStatement insertStatement3 = SQLParser.parse(insertSQL3);
        sqlExecuter.execute(insertStatement3);
    }

    @Test
    public void testUpdateData() throws Exception {
        if (!new File(sqlExecuter.getTablePath("TEST")).exists()) {
            sqlExecuter.createTable("TEST");
        }
        String insertSQL = "INSERT INTO test (id, name, age) VALUES (1, 'Mike', 19)";
        SQLStatement insertStatement = SQLParser.parse(insertSQL);
        sqlExecuter.execute(insertStatement);
        String updateSQL = "UPDATE test SET name = 'Hertz' WHERE name = \"Mike\"";
        SQLStatement updateStatement = SQLParser.parse(updateSQL);
        sqlExecuter.execute(updateStatement);
    }

    @Test
    public void testSelectData() throws Exception {
        if (!new File(sqlExecuter.getTablePath("TEST")).exists()) {
            sqlExecuter.createTable("TEST");
        }
        String selectSQL = "SELECT * FROM test";
        SQLStatement selectStatement = SQLParser.parse(selectSQL);
        sqlExecuter.execute(selectStatement);
    }

    @Test
    public void testDeleteData() throws Exception {
        if (!new File(sqlExecuter.getTablePath("TEST")).exists()) {
            sqlExecuter.createTable("TEST");
        }
        String deleteSQL1 = "DELETE FROM test WHERE id = 1";
        SQLStatement deleteStatement1 = SQLParser.parse(deleteSQL1);
        sqlExecuter.execute(deleteStatement1);
        String deleteSQL2 = "DELETE FROM test WHERE name = \"Adam\"";
        SQLStatement deleteStatement2 = SQLParser.parse(deleteSQL2);
        sqlExecuter.execute(deleteStatement2);
        String deleteSQL3 = "DELETE FROM test WHERE age = 20";
        SQLStatement deleteStatement3 = SQLParser.parse(deleteSQL3);
        sqlExecuter.execute(deleteStatement3);
    }
}
