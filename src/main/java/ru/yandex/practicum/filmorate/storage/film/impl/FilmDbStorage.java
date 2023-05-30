package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.util.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Repository
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    private final String sqlQueryFilm = "SELECT F.FILM_ID, " +
                                        "F.FILM_NAME, " +
                                        "F.DESCRIPTION, " +
                                        "F.RELEASE_DATE, " +
                                        "F.DURATION, " +
                                        "F.MPA_ID, " +
                                        "M.MPA_NAME " +
                                        "FROM FILMS AS F " +
                                        "LEFT OUTER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID";

    public FilmDbStorage(JdbcTemplate jdbcTemplate, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }
    @Override
    public List<Film> getAll() {
        String sqlQuery = sqlQueryFilm + ";";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);

        for (Film film : films) {
            film.setGenres(new HashSet<>(getFilmGenres(film.getId())));
        }
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        String sqlQuery = sqlQueryFilm + " WHERE F.FILM_ID = ?;";

        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
            if (film != null) {
                film.setGenres(new HashSet<>(getFilmGenres(film.getId())));
            }
            return film;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void create(Film film) {
        String sqlQuery = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                          "VALUES (?, ?, ?, ?, ?);";

        if (getFilmById(film.getId()) != null) {
            log.error("Произошло исключение! Такой фильм уже существует.");
            throw new AlreadyExistException("Такой фильм уже существует.");
        }
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId());
        insertGenres(film);
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?;";
        String sqlQueryDeleteGenres = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?;";

        if (getFilmById(film.getId()) == null) {
            log.error("Произошло исключение! Такого фильма не существует.");
            throw new NotFoundException("Такого фильма не существует.");
        }
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                            film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());
        insertGenres(film);
    }

    @Override
    public void deleteById(Long id) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?;";

        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addLike(Long id, User user) {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?);";

        if (getFilmById(id) == null || userStorage.getUserById(user.getId()) == null) {
            log.error("Произошло исключение! Такого фильма или пользователя не существует.");
            throw new NotFoundException("Такого фильма или пользователя не существует.");
        }
        jdbcTemplate.update(sqlQuery, id, user.getId());
    }

    @Override
    public void removeLike(Long id, User user) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?;";

        if (getFilmById(id) == null || user == null) {
            log.error("Произошло исключение! Такого фильма или пользователя не существует.");
            throw new NotFoundException("Такого фильма или пользователя не существует.");
        }
        jdbcTemplate.update(sqlQuery, id, user.getId());
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = sqlQueryFilm + " LEFT OUTER JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(L.USER_ID) DESC " +
                "LIMIT ?;";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);

        for (Film film : films) {
            film.setGenres(new HashSet<>(getFilmGenres(film.getId())));
        }
        return films;
    }

    private void insertGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String sqlQueryGenre = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) " +
                        "VALUES (?, ?);";
                jdbcTemplate.update(sqlQueryGenre, film.getId(), genre.getId());
            }
        }
    }

    private List<Genre> getFilmGenres(Long id) {
        String sqlQueryFilmsGenres = "SELECT FG.GENRE_ID, G.GENRE_NAME " +
                "FROM FILMS_GENRES AS FG " +
                "LEFT OUTER JOIN GENRES AS G ON FG.GENRE_ID = G.GENRE_ID " +
                "WHERE FILM_ID = ?;";
        return jdbcTemplate.query(sqlQueryFilmsGenres, this::mapRowToGenre, id);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(MPA.builder()
                        .id(resultSet.getInt("MPA_ID"))
                        .name(resultSet.getString("MPA_NAME"))
                        .build())
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}
