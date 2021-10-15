package ru.nikky.notes.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import ru.nikky.notes.R;
import ru.nikky.notes.domain.NoteEntity;
import ru.nikky.notes.ui.pages.about.AboutFragment;
import ru.nikky.notes.ui.pages.edit.EditNoteFragment;
import ru.nikky.notes.ui.pages.list.NotesListFragment;
import ru.nikky.notes.ui.pages.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NotesListFragment.Contract, EditNoteFragment.Contract {

    private static final int VIBRATION_TIME = 40;

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
    public void addNoteButtonPressed() {
        launchEditNoteFragment(null);
    }

    @Override
    public void noteItemPressed(NoteEntity noteEntity) {
        launchEditNoteFragment(noteEntity);
    }

    @Override
    public void noteItemPressedLong(NoteEntity noteEntity, View view) {
        vibrate();
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.notes_list_popup_menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            noteEntity.setTitle("");
            noteEntity.setDetail("");
            deleteNoteEntity(noteEntity);
            return true;
        });
        popupMenu.show();
    }

    private void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATION_TIME);
    }

    @Override
    public void settingsButtonPressed() {
        popNotEditNoteFragmentFromBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.edit_note_fragment_container, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void aboutButtonPressed() {
        popNotEditNoteFragmentFromBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.edit_note_fragment_container, new AboutFragment())
                .addToBackStack(null)
                .commit();
    }

    private void popNotEditNoteFragmentFromBackStack(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (!fragments.isEmpty() &&
                !(fragments.get(fragments.size() - 1) instanceof EditNoteFragment)){
            getSupportFragmentManager().popBackStack();
        }
    }

    private void launchEditNoteFragment(NoteEntity noteEntity) {
        closeEditNoteFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.edit_note_fragment_container, EditNoteFragment.newInstance(noteEntity))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void saveResult(NoteEntity noteEntity) {
        deleteNoteEntity(noteEntity);
        closeEditNoteFragment();
        hideKeyboard();
    }

    private void deleteNoteEntity(NoteEntity noteEntity){
        NotesListFragment notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentById(R.id.notes_list_fragment_container);
        if (notesListFragment != null) notesListFragment.receiveNoteEntity(noteEntity);
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
