package mvc.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * The GoogleScholarAPI class provides methods to interact with the SerpApi Google Scholar API
 * It allows fetching author profiles and their published articles by using the author's Google Scholar ID
 *
 * This class handles HTTP requests, JSON parsing (using Gson), and converts API responses into
 * Java model objects such as Author, Article, and Interest
 *
 * Usage Example:
 *     GoogleScholarAPI api = new GoogleScholarAPI();
 *     Author author = api.getAuthorProfile("LSsXyncAAAAJ");
 *     List<Article> articles = api.getArticles("LSsXyncAAAAJ");
 *
 * Note:
 * You must provide your SerpApi key inside the config.properties file
 * under the property name "serpapi.api_key".
 */

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

    /**
     * Parses a JSON response from the SerpApi endpoint and converts it into an {@link Author} object.
     *
     * @param jsonResponse The raw JSON response string returned by the API.
     * @return An {@link Author} object containing parsed author data.
     * @throws ApiException If the JSON does not contain a valid "author" object.
     */
    private Author parseAuthor(String jsonResponse) throws ApiException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        // Verify if the object 'author' exists and is not null
        if (!jsonObject.has("author") || jsonObject.get("author").isJsonNull()) {
            throw new ApiException("The JSON response does not contain a valid 'author' object. Check your API key or the author ID.");
        }
        JsonObject authorData = jsonObject.getAsJsonObject("author");

        // getting the author info in a safe way
        String name = safeGetString(authorData, "name");
        String affiliations = safeGetString(authorData, "affiliations");
        String email = safeGetString(authorData, "email");
        List<Interest> interests = safeGetInterests(authorData, "interests");
        String thumbnail = safeGetString(authorData, "thumbnail");

        return new Author(name, affiliations,email, interests, thumbnail);
    }

    /**
     * Safely retrieves a string value from a {@link JsonObject}.
     * Returns "N/A" if the key is missing or null.
     *
     * @param jsonObject The JSON object to read from.
     * @param key        The field name to extract.
     * @return The string value or "N/A" if not found.
     */

    private String safeGetString(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsString();
        }
        return "N/A";
    }

    /**
     * Safely retrieves a list of {@link Interest} objects from the JSON data.
     *
     * @param jsonObject The JSON object containing the interests.
     * @param key        The JSON key associated with the interests array.
     * @return A list of {@link Interest} objects or an empty list if not found.
     */

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
        return new ArrayList<>(); // Return an empty list if there is not Interest.
    }

    /**
     * Retrieves a list of articles published by a specific author.
     *
     * @param authorId The unique Google Scholar author ID.
     * @return A list of {@link Article} objects containing publication details.
     * @throws IOException  If a network or I/O error occurs.
     * @throws ApiException If the API returns an invalid response or missing article data.
     */

    public List<Article> getArticles(String authorId) throws IOException, ApiException {
        String url = String.format("%s?engine=google_scholar_author&author_id=%s&api_key=%s", ENDPOINT, authorId, API_KEY);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != 200) {
                    throw new ApiException("API returned status code: " + statusCode);
                }

                String jsonResponse = EntityUtils.toString(response.getEntity());
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

                if (!jsonObject.has("articles") || jsonObject.get("articles").isJsonNull()) {
                    throw new ApiException("No 'articles' found in response");
                }

                JsonArray articlesArray = jsonObject.getAsJsonArray("articles");
                List<Article> articles = new ArrayList<>();

                for (JsonElement elem : articlesArray) {
                    JsonObject art = elem.getAsJsonObject();
                    String title = safeGetString(art, "title");
                    String authors = safeGetString(art, "authors");
                    String publication = safeGetString(art, "publication");
                    String link = safeGetString(art, "link");
                    int citedBy = art.has("cited_by") && art.getAsJsonObject("cited_by").has("value")
                            ? art.getAsJsonObject("cited_by").get("value").getAsInt()
                            : 0;

                    articles.add(new Article(title, authors, publication, link, "", citedBy));
                }

                return articles;
            }
        }
    }

}