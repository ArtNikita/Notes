package ru.nikky.notes.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import ru.nikky.notes.App;
import ru.nikky.notes.R;
import ru.nikky.notes.databinding.ActivityMainBinding;
import ru.nikky.notes.domain.NoteEntity;
import ru.nikky.notes.ui.pages.about.AboutFragment;
import ru.nikky.notes.ui.pages.edit.EditNoteFragment;
import ru.nikky.notes.ui.pages.list.NotesListFragment;
import ru.nikky.notes.ui.pages.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NotesListFragment.Contract, EditNoteFragment.Contract {

    private ActivityMainBinding binding;
    private static final int VIBRATION_TIME = 40;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.notesListFragmentContainer.getId(), new NotesListFragment())
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
            NoteEntity justDeletedNoteEntity = new NoteEntity(noteEntity.getTitle(), noteEntity.getDetail());
            closeEditNoteFragmentIfItContainsNoteToDelete(noteEntity.getId());
            deleteNoteEntity(noteEntity);
            notifyUserThatNoteWasDeleted(justDeletedNoteEntity);
            return true;
        });
        popupMenu.show();
    }

    private void closeEditNoteFragmentIfItContainsNoteToDelete(int noteId) {
        Fragment currentNoteEditFragment = getSupportFragmentManager().findFragmentById(binding.editNoteFragmentContainer.getId());
        if (currentNoteEditFragment instanceof EditNoteFragment){
            if (((EditNoteFragment) currentNoteEditFragment).getCurrentNoteId() == noteId){
                closeEditNoteFragment();
            }
        }
    }

    private void notifyUserThatNoteWasDeleted(NoteEntity noteEntity) {
        Snackbar.make(findViewById(R.id.add_note_floating_action_button), getString(R.string.note_was_deleted_snackbar_text), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_snackbar_action_text, v -> notifyNoteEntityChanged(noteEntity))
                .setBackgroundTint(getColor(R.color.yellow))
                .setTextColor(getColor(R.color.pink))
                .setActionTextColor(getColor(R.color.pink))
                .show();
    }

    private void deleteNoteEntity(NoteEntity noteEntity){
        noteEntity.setTitle("");
        noteEntity.setDetail("");
        notifyNoteEntityChanged(noteEntity);
    }

    private void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATION_TIME);
    }

    @Override
    public void settingsButtonPressed() {
        new SettingsFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    public void aboutButtonPressed() {
        new AboutFragment().show(getSupportFragmentManager(), null);
    }

    private void launchEditNoteFragment(NoteEntity noteEntity) {
        closeEditNoteFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.editNoteFragmentContainer.getId(), EditNoteFragment.newInstance(noteEntity))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void saveResult(NoteEntity noteEntity) {
        notifyNoteEntityChanged(noteEntity);
        closeEditNoteFragment();
        hideKeyboard();
    }

    private void notifyNoteEntityChanged(NoteEntity noteEntity){
        NotesListFragment notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentById(binding.notesListFragmentContainer.getId());
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

    @Override
    protected void onPause() {
        super.onPause();
        saveNotesRepo();
    }

    private void saveNotesRepo() {
        ((App) getApplication()).saveNotesRepo();
    }
}
