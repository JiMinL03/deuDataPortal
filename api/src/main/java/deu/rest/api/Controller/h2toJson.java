package deu.rest.api.Controller;

import java.sql.*;
import java.io.FileWriter;
import org.json.JSONObject;
import org.json.JSONArray;


public class h2toJson {

    static String jdbcUrl = "jdbc:h2:~/api";  // H2 DB 경로
    static String user = "sa";
    static String password = "";

    public static void main(String[] args) {
        String[] tables = {
                "BUILDING", "BUS", "CAMPUS_MAP", "COLLEGE", "DEPARTMENT",
                "DEU_INFO", "INFO_SQUARE", "NOTICE", "PROFESSOR",
                "SCHEDULE", "SERVICE", "TUTION", "USER_LINK"
        };

        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password)) {
            for (String table : tables) {
                exportTableToJson(conn, table);
            }
            System.out.println("모든 테이블 JSON 파일로 저장 완료!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportTableToJson(Connection conn, String tableName) throws Exception {
        String query = "SELECT * FROM " + tableName;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        JSONArray jsonArray = new JSONArray();

        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
            for (int i = 1; i <= columnCount; i++) {
                String colName = meta.getColumnName(i);
                Object val = rs.getObject(i);
                jsonObj.put(colName, val);
            }
            jsonArray.put(jsonObj);
        }

        // JSON 배열을 파일에 저장
        try (FileWriter file = new FileWriter(tableName + ".json")) {
            file.write(jsonArray.toString(2)); // pretty print
        }

        System.out.println(tableName + ".json 생성 완료!");
    }
}
