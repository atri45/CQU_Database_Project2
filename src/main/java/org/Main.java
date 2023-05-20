package org;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String sql;
        // 数据库和sql执行器初始化
        System.out.println("请输入您的数据库名：");
        String databaseName = reader.readLine();
        File directory = new File("");
        Database db = new Database(databaseName, directory.getCanonicalPath()+"/target/classes/org/database");
        SQLExecuter sqlExecuter = new SQLExecuter(db);
        // 执行用户输入的sql语句
        System.out.println("请输入您要执行的sql语句：");
        while ((sql = reader.readLine()) != null) {
            SQLStatement createStatement1 = SQLParser.parse(sql);
            sqlExecuter.execute(createStatement1);
            System.out.println("请输入您要执行的sql语句：");
        }
    }
}
