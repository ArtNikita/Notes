package ru.nikky.notes.domain;

import java.util.ArrayList;

public interface NotesRepo {

    ArrayList<NoteEntity> getNotes();

    void add(String title, String detail);

    NoteEntity get(int id);

    void update(int id, String newTitle, String newDetail);

    void delete(int id);
}
