package ru.nikky.notes.ui.pages.edit;

import android.content.Context;
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
import ru.nikky.notes.databinding.FragmentEditNoteBinding;
import ru.nikky.notes.domain.NoteEntity;

public class EditNoteFragment extends Fragment {

    private FragmentEditNoteBinding binding;
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
        binding = FragmentEditNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
        processInputArguments(getArguments());
    }

    private void setupListeners() {
        binding.saveNoteButton.setOnClickListener(v -> getContractActivity().saveResult(getNote()));
    }

    private NoteEntity getNote() {
        String titleText = binding.titleEditText.getText().toString().trim();
        String detailText = binding.detailEditText.getText().toString().trim();
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
            binding.titleEditText.setText(inputNoteEntity.getTitle());
            binding.detailEditText.setText(inputNoteEntity.getDetail());
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
