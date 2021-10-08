package ru.nikky.notes.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.nikky.notes.R;
import ru.nikky.notes.domain.NoteEntity;

public class EditNoteFragment extends Fragment {

    private EditText titleEditText;
    private EditText detailEditText;
    private MaterialButton saveButton;

    public static final String KEY_NOTE_ENTITY = "KEY_NOTE_ENTITY";
    private boolean isNewNote;
    private NoteEntity inputNoteEntity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof EditNoteFragment.Contract)){
            throw new IllegalStateException("Launcher activity must implement EditNoteFragment.Contract");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupListeners();
        processInputArguments(getArguments());
    }

    private void initViews(View view) {
        titleEditText = view.findViewById(R.id.title_edit_text);
        detailEditText = view.findViewById(R.id.detail_edit_text);
        saveButton = view.findViewById(R.id.save_note_button);
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> {
            getContractActivity().saveResult(getNote());
        });
    }

    private NoteEntity getNote() {
        String titleText = titleEditText.getText().toString().trim();
        String detailText = detailEditText.getText().toString().trim();
        NoteEntity outputNoteEntity;
        if (isNewNote){
            outputNoteEntity = new NoteEntity(titleText, detailText);
        } else {
            inputNoteEntity.setTitle(titleText);
            inputNoteEntity.setDetail(detailText);
            outputNoteEntity = inputNoteEntity;
        }
        return outputNoteEntity;
    }

    private void processInputArguments(Bundle arguments) {
        inputNoteEntity = (NoteEntity) arguments.get(KEY_NOTE_ENTITY);
        isNewNote = inputNoteEntity == null;
        if (!isNewNote){
            titleEditText.setText(inputNoteEntity.getTitle());
            detailEditText.setText(inputNoteEntity.getDetail());
        }
    }

    public static EditNoteFragment newInstance(NoteEntity noteEntity){
        EditNoteFragment editNoteFragment = new EditNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_NOTE_ENTITY, noteEntity);
        editNoteFragment.setArguments(bundle);
        return editNoteFragment;
    }

    public interface Contract{
        void saveResult(NoteEntity noteEntity);
    }

    private Contract getContractActivity(){
        return (EditNoteFragment.Contract) getActivity();
    }
}
