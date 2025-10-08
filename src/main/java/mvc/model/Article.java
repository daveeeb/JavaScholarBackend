package mvc.model;
/**
 * Represents an academic article retrieved from Google Scholar.
 * Each Article object stores metadata about a publication such as
 * title, authors, publication date, citation count, and link.
 */

public record Article(
        String title,
        String authors,
        String publicationDate,
        String link,
        String keywords,
        int citedBy
) {}
