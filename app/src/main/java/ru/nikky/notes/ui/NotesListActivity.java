package ru.nikky.notes.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.nikky.notes.R;
import ru.nikky.notes.domain.NoteEntity;
import ru.nikky.notes.domain.NotesRepo;
import ru.nikky.notes.impl.NotesRepoImpl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView notesRecyclerView;
    private NotesAdapter adapter;

    private NotesRepo notesRepo;

    private ActivityResultLauncher<Intent> editNoteActivityLauncher;

    private final static String KEY_NOTES_ARRAY = "KEY_NOTES_ARRAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        initToolBar();
        initNotesRepo(savedInstanceState);
        initActivityResultLaunchers();
        initRecyclerView();
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.notes_list_activity_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_list_activity_toolbar_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_NOTES_ARRAY, new ArrayList<>(notesRepo.getNotes()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_note_menu) {
            addNoteMenuItemPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActivityResultLaunchers() {
        editNoteActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent returnedIntent = result.getData();
                if (returnedIntent != null) {
                    NoteEntity returnedNoteEntity = returnedIntent.getParcelableExtra(EditNoteActivity.KEY_NOTE_ENTITY);
                    if (returnedNoteEntity.getId() == NoteEntity.NEW_NOTE_ENTITY_ID) {
                        processNewEditedEntity(returnedNoteEntity);
                    } else {
                        processEditedEntity(returnedNoteEntity);
                    }
                }
            }
        });
    }

    private void processNewEditedEntity(NoteEntity noteEntity) {
        if (noteEntity.isEmpty()) return;
        notesRepo.add(noteEntity.getTitle(), noteEntity.getDetail());
        adapter.setData(notesRepo.getNotes());
    }

    private void processEditedEntity(NoteEntity noteEntity) {
        if (noteEntity.isEmpty()) {
            notesRepo.delete(noteEntity.getId());
        } else {
            notesRepo.update(noteEntity.getId(), noteEntity.getTitle(), noteEntity.getDetail());
        }
        adapter.setData(notesRepo.getNotes());
    }

    private void addNoteMenuItemPressed() {
        openEditNoteActivity(null);
    }

    private void openEditNoteActivity(NoteEntity noteEntity) {
        Intent editNoteActivityIntent = new Intent(this, EditNoteActivity.class);
        editNoteActivityIntent.putExtra(EditNoteActivity.KEY_NOTE_ENTITY, noteEntity);
        editNoteActivityLauncher.launch(editNoteActivityIntent);
    }

    private void initNotesRepo(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_NOTES_ARRAY)){
            notesRepo = new NotesRepoImpl((ArrayList<NoteEntity>) savedInstanceState.get(KEY_NOTES_ARRAY));
        } else {
            notesRepo = new NotesRepoImpl();
        }
    }

    private void initRecyclerView() {
        notesRecyclerView = findViewById(R.id.notes_recycler_view);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter();
        adapter.setOnItemClickListener(this::onItemClick);
        notesRecyclerView.setAdapter(adapter);
        adapter.setData(notesRepo.getNotes());
    }

    private void onItemClick(NoteEntity noteEntity) {
        openEditNoteActivity(noteEntity);
    }
}