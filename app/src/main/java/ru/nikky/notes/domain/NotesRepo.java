package ru.nikky.notes.domain;

import java.util.List;

public interface NotesRepo {

    List<NoteEntity> getNotes();

    void add(String title, String detail);

    NoteEntity get(int id);

    void update(int id, String newTitle, String newDetail);

    void delete(int id);
}
