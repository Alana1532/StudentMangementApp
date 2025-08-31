import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:students.db";  // this will create students.db file

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("✅ Connected to SQLite successfully!");

                // Create the "students" table if it doesn’t exist
                String sql = "CREATE TABLE IF NOT EXISTS students (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "age INTEGER," +
                        "grade TEXT)";

                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                System.out.println("✅ Students table is ready.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


