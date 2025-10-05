package mvc.controller;

import mvc.model.Author;
import mvc.model.GoogleScholarAPI;
import mvc.view.AuthorView;
import java.io.IOException;
import mvc.model.ApiException;

/**
 * @author Javier David Barraza Ureña ID 3303
 * Controller class in the MVC pattern.
 * It handles the interaction between the `AuthorView` and `GoogleScholarAPI` model.
 * The controller's main responsibility is to process user requests (e.g., searching for an author)
 * and update the view with the results from the model.
 * @see mvc.view.AuthorView
 * @see mvc.model.GoogleScholarAPI
 */

public class AuthorController{
    private final AuthorView view;
    private final GoogleScholarAPI model;

    public AuthorController(AuthorView view, GoogleScholarAPI model) {
        this.view = view;
        this.model = model;
    }

    public void searchAuthor(String authorId) {
        try {
            Author author = model.getAuthorProfile(authorId);
            view.displayAuthorDetails(author);
        } catch (IOException | ApiException e) {
            view.displayError("Error Can not get author data: " + e.getMessage());
        }
    }
}