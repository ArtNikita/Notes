package ru.nikky.notes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nikky.notes.R;
import ru.nikky.notes.domain.NoteEntity;

public class NoteVH extends RecyclerView.ViewHolder {
    private final TextView titleTextView = itemView.findViewById(R.id.title_text_view);
    private final TextView detailTextView = itemView.findViewById(R.id.detail_text_view);
    private NoteEntity note;

    public NoteVH(@NonNull ViewGroup parent, NotesAdapter.OnItemClickListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_note_item, parent, false));
        itemView.setOnClickListener(v -> listener.onItemClick(note));
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(note, view);
                return true;
            }
        });
    }

    public void bind(NoteEntity note) {
        this.note = note;
        titleTextView.setText(note.getTitle());
        detailTextView.setText(note.getDetail());
    }
}
