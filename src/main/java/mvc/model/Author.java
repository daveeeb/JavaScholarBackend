package mvc.model;

import java.util.List;

     /**
     * Represents an academic author retrieved from Google Scholar.
     * Each author object stores metadata about a publication such as
     * name, affiliations, email, interests, thumbnail.
     */

public record Author(String name, String affiliations, String email, List<Interest> interests, String thumbnail) {
}

