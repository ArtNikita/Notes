package ru.nikky.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import ru.nikky.notes.R;
import ru.nikky.notes.domain.NoteEntity;

public class EditNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText detailEditText;

    public static final String KEY_NOTE_ENTITY = "KEY_NOTE_ENTITY";
    private boolean isNewNote;
    private NoteEntity inputNoteEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        initViews();
        processInputIntent();
    }

    private void initViews() {
        titleEditText = findViewById(R.id.title_edit_text);
        detailEditText = findViewById(R.id.detail_edit_text);
    }

    private void processInputIntent() {
        Intent inputIntent = getIntent();
        inputNoteEntity = inputIntent.getParcelableExtra(KEY_NOTE_ENTITY);
        isNewNote = inputNoteEntity == null;
        if (!isNewNote){
            titleEditText.setText(inputNoteEntity.getTitle());
            detailEditText.setText(inputNoteEntity.getDetail());
        }
    }

    @Override
    public void onBackPressed() {
        processOutputIntent();
        super.onBackPressed();
    }

    private void processOutputIntent() {
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
        Intent outputIntent = new Intent();
        outputIntent.putExtra(KEY_NOTE_ENTITY, outputNoteEntity);
        setResult(RESULT_OK, outputIntent);
    }
}
