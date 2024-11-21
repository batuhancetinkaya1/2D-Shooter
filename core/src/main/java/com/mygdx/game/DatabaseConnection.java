package com.mygdx.game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/test2";
    private static final String USER = "mrcn";
    private static final String PASSWORD = "1611";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public DatabaseConnection() {
        // Constructor can be empty or used for other initializations
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    public void insertScore(String username, int score) {
        String sql = "INSERT INTO leaderboard(username, score) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert score failed: " + e.getMessage());
        }
    }

    public List<String[]> getTopScores() {
        List<String[]> scores = new ArrayList<>();
        String sql = "SELECT username, score FROM leaderboard ORDER BY score DESC LIMIT 10";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                scores.add(new String[]{username, String.valueOf(score)});
            }
        } catch (SQLException e) {
            System.err.println("Error fetching scores: " + e.getMessage());
        }
        return scores;
    }
}

