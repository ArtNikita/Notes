package ru.nikky.notes.ui.pages.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nikky.notes.databinding.RecyclerViewNoteItemBinding;
import ru.nikky.notes.domain.NoteEntity;

public class NotesAdapter extends RecyclerView.Adapter<NoteVH>{

    private ArrayList<NoteEntity> notesArrayList;
    private OnItemClickListener clickListener;

    public void setData(List<NoteEntity> notesArrayList){
        this.notesArrayList = new ArrayList<>(notesArrayList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerViewNoteItemBinding binding =
                RecyclerViewNoteItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false);
        return new NoteVH(parent, clickListener, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bind(getItem(position));
    }

    private NoteEntity getItem(int position){
        return notesArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(NoteEntity item);
        void onItemLongClick(NoteEntity item, View anchorView);
    }
}
