package ru.nikky.notes.ui.pages.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.nikky.notes.App;
import ru.nikky.notes.R;
import ru.nikky.notes.databinding.FragmentNotesListBinding;
import ru.nikky.notes.domain.NoteEntity;
import ru.nikky.notes.domain.NotesRepo;

public class NotesListFragment extends Fragment {

    private final NotesAdapter.OnItemClickListener listener = new NotesAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(NoteEntity noteEntity) {
            getContractActivity().noteItemPressed(noteEntity);
        }

        @Override
        public void onItemLongClick(NoteEntity noteEntity, View anchorView) {
            getContractActivity().noteItemPressedLong(noteEntity, anchorView);
        }
    };
    private FragmentNotesListBinding binding;
    private NotesAdapter adapter;
    private NotesRepo notesRepo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof Contract)) {
            throw new IllegalStateException("Launcher activity must implement NotesListFragment.Contract");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesListBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolBar();
        setupAddNoteFloatingActionButton();
        initNotesRepo();
        initRecyclerView();
    }

    private void initToolBar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.notesListActivityToolbar);
    }

    private void setupAddNoteFloatingActionButton() {
        binding.addNoteFloatingActionButton.setOnClickListener(v -> addNoteButtonPressed());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notes_list_activity_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu) {
            settingsButtonPressed();
            return true;
        }
        if (item.getItemId() == R.id.about_menu) {
            aboutButtonPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void receiveNoteEntity(NoteEntity noteEntity) {
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

    private void addNoteButtonPressed() {
        getContractActivity().addNoteButtonPressed();
    }

    private void settingsButtonPressed() {
        getContractActivity().settingsButtonPressed();
    }

    private void aboutButtonPressed() {
        getContractActivity().aboutButtonPressed();
    }

    private void initNotesRepo() {
        notesRepo = getApp().getNotesRepo();
    }

    private App getApp() {
        return (App) requireActivity().getApplication();
    }

    private void initRecyclerView() {
        binding.notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NotesAdapter();
        adapter.setOnItemClickListener(listener);
        binding.notesRecyclerView.setAdapter(adapter);
        adapter.setData(notesRepo.getNotes());
    }

    private Contract getContractActivity() {
        return (Contract) getActivity();
    }

    public interface Contract {
        void addNoteButtonPressed();

        void noteItemPressed(NoteEntity noteEntity);

        void settingsButtonPressed();

        void aboutButtonPressed();

        void noteItemPressedLong(NoteEntity noteEntity, View anchorView);
    }
}