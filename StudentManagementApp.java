import java.sql.*;
import java.util.Scanner;

public class StudentManagementApp {
    private static final String DB_URL = "jdbc:sqlite:students.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                createTable(conn);
                runMenu(conn);
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "age INTEGER," +
                "grade TEXT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void runMenu(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addStudent(conn, sc);
                case 2 -> viewStudents(conn);
                case 3 -> updateStudent(conn, sc);
                case 4 -> deleteStudent(conn, sc);
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addStudent(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Enter grade: ");
        String grade = sc.nextLine();

        String sql = "INSERT INTO students(name, age, grade) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, grade);
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");
        }
    }

    private static void viewStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Student List ---");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getInt("age") + " | " +
                                rs.getString("grade"));
            }
        }
    }

    private static void updateStudent(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter student ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new grade: ");
        String grade = sc.nextLine();

        String sql = "UPDATE students SET name = ?, age = ?, grade = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, grade);
            pstmt.setInt(4, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found.");
            }
        }
    }

    private static void deleteStudent(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter student ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found.");
            }
        }
    }
}
