package org.example;

import mvc.controller.AuthorController;
import mvc.model.GoogleScholarAPI;
import mvc.view.AuthorView;

import java.io.IOException;

/**
 * @author Javier David Barraza Ureña ID 3303
 * Controller class in the MVC pattern.
 * The main entry point of the application.
 * This class sets up the MVC components and initiates the author search process.
 */

public class Main {
    /**
     * The main method where the application execution begins.
     * It initializes the view, model, and controller, and starts a search for a predefined author ID.
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        String[] authorIds = {"LSsXyncAAAAJ", "04GHe_kAAAAJ"};

        try {
            AuthorView view = new AuthorView();
            GoogleScholarAPI model = new GoogleScholarAPI();
            AuthorController controller = new AuthorController(view, model);

            for (String authorId : authorIds) {
                System.out.println("Looking author with ID: " + authorId);
                controller.searchAuthor(authorId);
            }

        } catch (IOException e) {
            System.err.println("ERROR: Could not start application due to I/O problem");
            e.printStackTrace();
        }
    }

}