package mvc.view;

import mvc.model.Author;
import mvc.model.Interest;

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
    public void displayError(String message) {
        System.err.println(" ERROR: " + message);
    }
}