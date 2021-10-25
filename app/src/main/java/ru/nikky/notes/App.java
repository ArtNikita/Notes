package ru.nikky.notes;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ru.nikky.notes.domain.NotesRepo;
import ru.nikky.notes.impl.NotesRepoImpl;

public class App extends Application {
    private final static String NOTES_REPO_SP_NAME = "NOTES_REPO_SP_NAME";
    private final static String NOTES_REPO_JSON_NAME = "NOTES_REPO_JSON_NAME";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private NotesRepo notesRepo;

    public NotesRepo getNotesRepo() {
        return notesRepo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSharedPreferences();
        loadNotesRepo();
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(NOTES_REPO_SP_NAME, MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    private void loadNotesRepo() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(NOTES_REPO_JSON_NAME, null);
        Type type = new TypeToken<NotesRepoImpl>() {
        }.getType();
        if (json != null) {
            notesRepo = gson.fromJson(json, type);
        } else {
            notesRepo = new NotesRepoImpl();
        }
    }

    public void saveNotesRepo() {
        Gson gson = new Gson();
        String json = gson.toJson(notesRepo);
        sharedPreferencesEditor.putString(NOTES_REPO_JSON_NAME, json);
        sharedPreferencesEditor.commit();
    }
}
