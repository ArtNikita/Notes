package ru.nikky.notes.ui;

import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.nikky.notes.domain.NoteEntity;

public class NotesAdapter extends RecyclerView.Adapter<NoteVH>{

    private ArrayList<NoteEntity> notesArrayList;
    private OnItemClickListener clickListener;

    public void setData(ArrayList<NoteEntity> notesArrayList){
        this.notesArrayList = new ArrayList<>(notesArrayList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteVH(parent, clickListener);
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

    interface OnItemClickListener{
        void onItemClick(NoteEntity item);
    }
}
