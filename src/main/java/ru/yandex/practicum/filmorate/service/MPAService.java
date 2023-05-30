package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.List;

@Service
public class MPAService {
    private final MPAStorage mpaStorage;

    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MPA> getAll() {
        return mpaStorage.getAll();
    }

    public MPA getGenreById(Integer id) {
        return mpaStorage.getGenreById(id);
    }
}
