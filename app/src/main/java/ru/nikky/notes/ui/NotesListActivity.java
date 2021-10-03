package ru.nikky.notes.ui;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class NotesListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView notesRecyclerView;
    private NotesAdapter adapter;

    private NotesRepo notesRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        initToolBar();
        initNotesRepo();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_note_menu) {
            addNoteMenuItemPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNoteMenuItemPressed() {
        openEditNoteActivity();
    }

    private void openEditNoteActivity() {
        Intent editNoteActivityIntent = new Intent(this, EditNoteActivity.class);
        startActivity(editNoteActivityIntent);
    }

    private void initNotesRepo() {
        notesRepo = new NotesRepoImpl();
        fillNotesRepoWithTestValues();
    }

    private void fillNotesRepoWithTestValues() {
        notesRepo.add("1 Note", "Some text");
        notesRepo.add("2 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("3 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("4 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("5 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("6 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("7 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("8 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("9 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("11 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("12 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("13 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("14 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("15 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("16 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("17 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("18 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("19 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
        notesRepo.add("20 Note", "Some very long text ds;lfj sdfj dsfl;hjd dsf; dsf;j ;ds;fndsflkjdsl fds;lkjf ");
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
        openEditNoteActivity();
    }
}