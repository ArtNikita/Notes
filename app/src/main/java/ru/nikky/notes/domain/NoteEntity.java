package ru.nikky.notes.domain;

public class NoteEntity {
    private int id;
    private String title;
    private String detail;

    public NoteEntity(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
