package ru.yandex.practicum.filmorate.storage.mpa.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class MPADbStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getAll() {
        String sqlQuery = "SELECT MPA_ID, MPA_NAME FROM MPA;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    @Override
    public MPA getGenreById(Integer id) {
        String sqlQuery = "SELECT MPA_ID, MPA_NAME FROM MPA WHERE MPA_ID = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPA, id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Произошло исключение! Такого MPA не существует.");
            throw new NotFoundException("Такого MPA не существует.");
        }
    }

    private MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
    }
}
