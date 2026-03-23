package com.example.moviedatabase;

public class Movie {
    private String title;
    private Integer year;
    private String genre;
    private String posterName;

    public Movie() {
        this.title = "Unknown Title";
        this.year = null;
        this.genre = "Unknown Genre";
        this.posterName = "";
    }

    public Movie(String title, Integer year, String genre, String posterName) {
        setTitle(title);
        setYear(year);
        setGenre(genre);
        setPosterName(posterName);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            this.title = "Unknown Title";
        } else {
            this.title = title.trim();
        }
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        if (year == null || year <= 0) {
            this.year = null;
        } else {
            this.year = year;
        }
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            this.genre = "Unknown Genre";
        } else {
            this.genre = genre.trim();
        }
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        if (posterName == null || posterName.trim().isEmpty()) {
            this.posterName = "";
        } else {
            this.posterName = posterName.trim();
        }
    }

    public String getYearText() {
        return year == null ? "Unknown Year" : String.valueOf(year);
    }
}