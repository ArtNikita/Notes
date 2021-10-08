package ru.nikky.notes.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import ru.nikky.notes.R;
import ru.nikky.notes.domain.NoteEntity;

public class MainActivity extends AppCompatActivity implements NotesListFragment.Contract, EditNoteFragment.Contract {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.notes_list_fragment_container, new NotesListFragment())
                    .commit();
        }
    }

    @Override
    public void addNoteMenuItemPressed() {
        launchEditNoteActivity(null);
    }

    @Override
    public void noteItemPressed(NoteEntity noteEntity) {
        launchEditNoteActivity(noteEntity);
    }

    private void launchEditNoteActivity(NoteEntity noteEntity) {
        closeEditNoteFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.edit_note_fragment_container, EditNoteFragment.newInstance(noteEntity))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void saveResult(NoteEntity noteEntity) {
        NotesListFragment notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentById(R.id.notes_list_fragment_container);
        if (notesListFragment != null) notesListFragment.receiveNoteEntity(noteEntity);
        closeEditNoteFragment();
        hideKeyboard();
    }

    private void closeEditNoteFragment() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
