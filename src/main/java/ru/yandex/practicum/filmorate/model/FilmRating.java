package ru.yandex.practicum.filmorate.model;

public enum FilmRating {
    G ("G"),
    PG ("PG"),
    PG13 ("PG-13"),
    R ("R"),
    NC17 ("NC-17");

    private String ratingName;

    FilmRating(String ratingName) {
        this.ratingName = ratingName;
    }

    public String getRatingName() {
        return ratingName;
    }

    @Override
    public String toString() {
        return "FilmRating{" +
                "ratingName='" + ratingName + '\'' +
                '}';
    }
}
