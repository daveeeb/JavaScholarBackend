package mvc.model;

import java.util.List;

public record Author(String name, String affiliations, String email, List<Interest> interests, String thumbnail) {
}

