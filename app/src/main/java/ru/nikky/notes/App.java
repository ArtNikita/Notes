package ru.nikky.notes;

import android.app.Application;

import ru.nikky.notes.domain.NotesRepo;
import ru.nikky.notes.impl.NotesRepoImpl;

public class App extends Application {
    private final NotesRepo notesRepo = new NotesRepoImpl();

    public NotesRepo getNotesRepo() {
        return notesRepo;
    }
}
