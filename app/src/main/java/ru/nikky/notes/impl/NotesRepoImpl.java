package ru.nikky.notes.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.nikky.notes.domain.NoteEntity;
import ru.nikky.notes.domain.NotesRepo;

public class NotesRepoImpl implements NotesRepo {

    private LinkedList<NoteEntity> notesArrayList;

    private int noteId;
    public NotesRepoImpl() {
        notesArrayList = new LinkedList<>();
        noteId = 0;
    }

    public NotesRepoImpl(ArrayList<NoteEntity> notesArrayList) {
        this.notesArrayList = new LinkedList<>(notesArrayList);
        noteId = getNotUsedId(notesArrayList);
    }

    @Override
    public void add(String title, String detail) {
        NoteEntity newNote = new NoteEntity(title, detail);
        newNote.setId(noteId++);
        notesArrayList.add(0, newNote);
    }

    @Override
    public NoteEntity get(int id) {
        NoteEntity result = null;
        for (int i = 0; i < notesArrayList.size(); i++) {
            if (notesArrayList.get(i).getId() == id){
                result = notesArrayList.get(i);
                break;
            }
        }
        return result;
    }

    @Override
    public void update(int id, String newTitle, String newDetail) {
        NoteEntity targetNote = get(id);
        targetNote.setTitle(newTitle);
        targetNote.setDetail(newDetail);
        delete(targetNote.getId());
        notesArrayList.add(0, targetNote);
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < notesArrayList.size(); i++) {
            if (notesArrayList.get(i).getId() == id){
                notesArrayList.remove(i);
                return;
            }
        }
    }

    private int getNotUsedId(ArrayList<NoteEntity> notesArrayList) {
        int resultId = -1;
        for (NoteEntity note : notesArrayList) {
            if (note.getId() > resultId) resultId = note.getId();
        }
        return resultId + 1;
    }

    public List<NoteEntity> getNotes() {
        return new ArrayList<>(notesArrayList);
    }
}
