package com.example.moviedatabase;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static class MovieLoadResult {
        private final List<Movie> movies;
        private final String errorMessage;

        public MovieLoadResult(List<Movie> movies, String errorMessage) {
            this.movies = movies;
            this.errorMessage = errorMessage == null ? "" : errorMessage;
        }

        public List<Movie> getMovies() {
            return movies;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static MovieLoadResult loadMoviesFromJson(Context context) {
        List<Movie> movies = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open("movies.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            inputStream.close();

            JSONArray jsonArray = new JSONArray(builder.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.optJSONObject(i);
                    if (obj == null) {
                        errors.add("Item " + (i + 1) + ": invalid object.");
                        continue;
                    }

                    String title = parseTitle(obj);
                    Integer year = parseYear(obj);
                    String genre = parseGenre(obj);
                    String poster = parsePoster(obj);

                    boolean validMovie = !title.equals("Unknown Title") || year != null || !genre.equals("Unknown Genre") || !poster.isEmpty();

                    if (!validMovie) {
                        errors.add("Item " + (i + 1) + ": skipped because all fields are missing or invalid.");
                        continue;
                    }

                    if (title.equals("Unknown Title")) {
                        errors.add("Item " + (i + 1) + ": missing or invalid title.");
                    }
                    if (year == null) {
                        errors.add("Item " + (i + 1) + ": missing or invalid year.");
                    }
                    if (genre.equals("Unknown Genre")) {
                        errors.add("Item " + (i + 1) + ": missing or invalid genre.");
                    }
                    if (poster.isEmpty()) {
                        errors.add("Item " + (i + 1) + ": missing or invalid poster.");
                    }

                    movies.add(new Movie(title, year, genre, poster));
                } catch (Exception e) {
                    errors.add("Item " + (i + 1) + ": parsing failed.");
                }
            }

            if (movies.isEmpty() && errors.isEmpty()) {
                return new MovieLoadResult(movies, "No movies found.");
            }

            return new MovieLoadResult(movies, buildErrorMessage(errors));

        } catch (java.io.FileNotFoundException e) {
            return new MovieLoadResult(movies, "Error: movies.json file not found.");
        } catch (org.json.JSONException e) {
            return new MovieLoadResult(movies, "Error: invalid JSON format.");
        } catch (Exception e) {
            return new MovieLoadResult(movies, "Error reading movie data.");
        }
    }

    private static String parseTitle(JSONObject obj) {
        if (!obj.has("title") || obj.isNull("title")) {
            return "Unknown Title";
        }
        Object value = obj.opt("title");
        if (!(value instanceof String)) {
            return "Unknown Title";
        }
        String title = ((String) value).trim();
        return title.isEmpty() ? "Unknown Title" : title;
    }

    private static Integer parseYear(JSONObject obj) {
        if (!obj.has("year") || obj.isNull("year")) {
            return null;
        }

        Object value = obj.opt("year");

        if (value instanceof Integer) {
            int year = (Integer) value;
            return year > 0 ? year : null;
        }

        if (value instanceof Long) {
            long year = (Long) value;
            return year > 0 && year <= Integer.MAX_VALUE ? (int) year : null;
        }

        if (value instanceof Double) {
            double year = (Double) value;
            if (year > 0 && year == Math.floor(year)) {
                return (int) year;
            }
            return null;
        }

        if (value instanceof String) {
            try {
                int parsedYear = Integer.parseInt(((String) value).trim());
                return parsedYear > 0 ? parsedYear : null;
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    private static String parseGenre(JSONObject obj) {
        if (!obj.has("genre") || obj.isNull("genre")) {
            return "Unknown Genre";
        }
        Object value = obj.opt("genre");
        if (!(value instanceof String)) {
            return "Unknown Genre";
        }
        String genre = ((String) value).trim();
        return genre.isEmpty() ? "Unknown Genre" : genre;
    }

    private static String parsePoster(JSONObject obj) {
        if (!obj.has("poster") || obj.isNull("poster")) {
            return "";
        }
        Object value = obj.opt("poster");
        if (!(value instanceof String)) {
            return "";
        }
        String poster = ((String) value).trim();
        return poster.isEmpty() ? "" : poster;
    }

    private static String buildErrorMessage(List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder("Some data issues were found:\n");
        int limit = Math.min(errors.size(), 6);

        for (int i = 0; i < limit; i++) {
            builder.append("• ").append(errors.get(i)).append("\n");
        }

        if (errors.size() > limit) {
            builder.append("• More issues found: ").append(errors.size() - limit);
        }

        return builder.toString().trim();
    }
}