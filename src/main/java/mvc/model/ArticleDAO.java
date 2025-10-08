package mvc.model;


import mvc.controller.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Handles all database operations related to Article objects.
 * This includes inserting, retrieving, and managing article data
 * from the MySQL database.
 */

public class ArticleDAO {

    public void saveArticles(String authorId, List<Article> articles) {
        String sql = "INSERT INTO articles (author_id, title, authors, publication_date, link, keywords, cited_by) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement db = conn.prepareStatement(sql)) {

            for (Article article : articles) {
                db.setString(1, authorId);
                db.setString(2, article.title());
                db.setString(3, article.authors());
                db.setString(4, article.publicationDate());
                db.setString(5, article.link());
                db.setString(6, article.keywords());
                db.setInt(7, article.citedBy());
                db.executeUpdate();
            }

            System.out.println("Articles successfully saved for author " + authorId);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}