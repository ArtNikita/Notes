package ru.nikky.notes.ui.pages.list;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nikky.notes.databinding.RecyclerViewNoteItemBinding;
import ru.nikky.notes.domain.NoteEntity;

public class NoteVH extends RecyclerView.ViewHolder {
    private final RecyclerViewNoteItemBinding binding;
    private NoteEntity note;

    public NoteVH(@NonNull ViewGroup parent, NotesAdapter.OnItemClickListener listener, RecyclerViewNoteItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        itemView.setOnClickListener(v -> listener.onItemClick(note));
        itemView.setOnLongClickListener(view -> {
            listener.onItemLongClick(note, view);
            return true;
        });
    }

    public void bind(NoteEntity note) {
        this.note = note;
        binding.titleTextView.setText(note.getTitle());
        binding.detailTextView.setText(note.getDetail());
    }
}
