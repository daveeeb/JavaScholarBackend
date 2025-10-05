package mvc.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class GoogleScholarAPI {
    private final String API_KEY;
    private static final String ENDPOINT = "https://serpapi.com/search.json";

    public GoogleScholarAPI() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            properties.load(input);
            this.API_KEY = properties.getProperty("serpapi.api_key");
        }
    }

    public Author getAuthorProfile(String authorId) throws IOException, ApiException {
        String url = String.format("%s?engine=google_scholar_author&author_id=%s&api_key=%s", ENDPOINT, authorId, API_KEY);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != 200) {
                    throw new ApiException("API returned status code: " + statusCode);
                }

                String jsonResponse = EntityUtils.toString(response.getEntity());
                return parseAuthor(jsonResponse);
            }
        }
    }

    private Author parseAuthor(String jsonResponse) throws ApiException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        // Verifica si el objeto 'author' existe y no es nulo
        if (!jsonObject.has("author") || jsonObject.get("author").isJsonNull()) {
            throw new ApiException("The JSON response does not contain a valid 'author' object. Check your API key or the author ID.");
        }
        JsonObject authorData = jsonObject.getAsJsonObject("author");

        // Extrae la información del autor de forma segura
        String name = safeGetString(authorData, "name");
        String affiliations = safeGetString(authorData, "affiliations");
        String email = safeGetString(authorData, "email");
        List<Interest> interests = safeGetInterests(authorData, "interests");
        String thumbnail = safeGetString(authorData, "thumbnail");

        return new Author(name, affiliations,email, interests, thumbnail);
    }

    /**
     * Helper method to safely get a String from a JsonObject.
     */
    private String safeGetString(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsString();
        }
        return "N/A";
    }

    private List<Interest> safeGetInterests(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonArray()) {
            return jsonObject.getAsJsonArray(key).asList().stream()
                    .map(jsonElement -> {
                        JsonObject interestObject = jsonElement.getAsJsonObject();
                        String title = safeGetString(interestObject, "title");
                        String link = safeGetString(interestObject, "link");
                        String serpapiLink = safeGetString(interestObject, "serpapi_link");
                        return new Interest(title, link, serpapiLink);
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(); // Retorna una lista vacía si no hay intereses.
    }

}