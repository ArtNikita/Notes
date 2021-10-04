package ru.nikky.notes.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteEntity implements Parcelable {

    public static final int NEW_NOTE_ENTITY_ID = -1;

    private int id;
    private String title;
    private String detail;

    public NoteEntity(String title, String detail) {
        id = NEW_NOTE_ENTITY_ID;
        this.title = title;
        this.detail = detail;
    }

    public boolean isEmpty(){
        return title.equals("") && detail.equals("");
    }

    protected NoteEntity(Parcel in) {
        id = in.readInt();
        title = in.readString();
        detail = in.readString();
    }

    public static final Creator<NoteEntity> CREATOR = new Creator<NoteEntity>() {
        @Override
        public NoteEntity createFromParcel(Parcel in) {
            return new NoteEntity(in);
        }

        @Override
        public NoteEntity[] newArray(int size) {
            return new NoteEntity[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(detail);
    }
}
