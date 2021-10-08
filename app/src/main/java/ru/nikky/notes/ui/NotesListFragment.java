package ru.nikky.notes.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.nikky.notes.R;
import ru.nikky.notes.domain.NoteEntity;
import ru.nikky.notes.domain.NotesRepo;
import ru.nikky.notes.impl.NotesRepoImpl;

public class NotesListFragment extends Fragment {

    private final static String KEY_NOTES_ARRAY = "KEY_NOTES_ARRAY";
    private Toolbar toolbar;
    private RecyclerView notesRecyclerView;
    private NotesAdapter adapter;
    private NotesRepo notesRepo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof Contract)){
            throw new IllegalStateException("Launcher activity must implement NotesListFragment.Contract");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolBar(view);
        initNotesRepo(savedInstanceState);
        initRecyclerView(view);
    }

    private void initToolBar(View view) {
        toolbar = view.findViewById(R.id.notes_list_activity_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notes_list_activity_toolbar_menu, menu);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
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

    public void receiveNoteEntity(NoteEntity noteEntity){
        if (noteEntity.getId() == NoteEntity.NEW_NOTE_ENTITY_ID) {
            processNewEditedEntity(noteEntity);
        } else {
            processEditedEntity(noteEntity);
        }
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
        getContractActivity().addNoteMenuItemPressed();
    }

    private void initNotesRepo(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_NOTES_ARRAY)) {
            notesRepo = new NotesRepoImpl((ArrayList<NoteEntity>) savedInstanceState.get(KEY_NOTES_ARRAY));
        } else {
            notesRepo = new NotesRepoImpl();
        }
    }

    private void initRecyclerView(View view) {
        notesRecyclerView = view.findViewById(R.id.notes_recycler_view);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NotesAdapter();
        adapter.setOnItemClickListener(this::onItemClick);
        notesRecyclerView.setAdapter(adapter);
        adapter.setData(notesRepo.getNotes());
    }

    private void onItemClick(NoteEntity noteEntity) {
        getContractActivity().noteItemPressed(noteEntity);
    }

    public interface Contract{
        void addNoteMenuItemPressed();
        void noteItemPressed(NoteEntity noteEntity);
    }

    private Contract getContractActivity(){
        return (Contract) getActivity();
    }
}