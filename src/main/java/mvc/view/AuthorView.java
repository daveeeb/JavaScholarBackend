package mvc.view;

import mvc.model.Article;
import mvc.model.Author;
import mvc.model.Interest;
import java.util.List;

/**
 * @author Javier David Barraza Ureña ID 3303
 * The AuthorView class is responsible for displaying author and article information
 * to the user in a formatted and readable way through the console.
 */

public class AuthorView {
    public void displayAuthorDetails(Author author) {
        System.out.println("----------------------------------------");
        System.out.println("          Author Details                ");
        System.out.println("----------------------------------------");
        System.out.println("Name: " + author.name());
        System.out.println("Affiliations: " + author.affiliations());
        System.out.println("Email: " + author.email());
        // Imprimir los intereses uno por uno
        System.out.println("Interests: ");
        if (author.interests().isEmpty()) {
            System.out.println("  - There are not interests");
        } else {
            for (Interest interest : author.interests()) {
                System.out.println("  - Titlle: " + interest.title());
                System.out.println("    Link: " + interest.link());
            }
        }
        System.out.println("Thumbnail: " + author.thumbnail());
    }
    /**
     * Displays a list of the author’s published articles.
     *
     * @param articles The list of articles written by the author.
     */
        public void displayArticles(List<Article> articles) {
            System.out.println("----------------------------------------");
            System.out.println("          Articles by Author            ");
            System.out.println("----------------------------------------");

            for (Article article : articles) {
                System.out.println("Title: " + article.title());
                System.out.println("Authors: " + article.authors());
                System.out.println("Publication Date: " + article.publicationDate());
                System.out.println("Link: " + article.link());
                System.out.println("Cited by: " + article.citedBy());
                System.out.println("Keywords: " + article.keywords());
                System.out.println("----------------------------------------");
            }
        }
    public void displayError(String message) {
        System.err.println(" ERROR: " + message);
    }
}