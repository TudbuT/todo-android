package de.tudbut.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    public static Type editType = Type.CREATE;
    public static Integer editIndex = null;

    ToDoItem editing = new ToDoItem("", "", false);

    Button confirmBtn, cancelBtn;
    EditText titleTxt, descriptionTxt;
    View deleteTitleBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        confirmBtn = findViewById(R.id.confirm_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        deleteTitleBtn = findViewById(R.id.delete_title);
        titleTxt = findViewById(R.id.edit_title);
        descriptionTxt = findViewById(R.id.edit_description);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        deleteTitleBtn.setOnClickListener(this);

        if(editType == Type.EDIT) {
            editing = Data.getList().get(editIndex);
        }
        titleTxt.getText().clear();
        titleTxt.getText().append(editing.title);
        descriptionTxt.getText().clear();
        descriptionTxt.getText().append(editing.description);
    }

    @Override
    public void onBackPressed() {
        onClick(cancelBtn);
    }

    @Override
    public void onClick(View view) {
        if(view == confirmBtn) {
            editing.title = titleTxt.getText().toString();
            editing.description = descriptionTxt.getText().toString();
            if (editType == Type.CREATE) {
                if (!editing.title.isEmpty()) {
                    Data.getList().add(0, editing);
                }
            } else if (editing.title.isEmpty()) {
                Data.getList().remove((int) editIndex);
            }
            Data.save();
            Toast.makeText(getApplicationContext(), editing.title.isEmpty() ? R.string.deleted : R.string.saved, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        if(view == cancelBtn) {
            if(!editing.title.equals(titleTxt.getText().toString()) || !editing.description.equals(descriptionTxt.getText().toString()))
                Toast.makeText(getApplicationContext(), R.string.discarded, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        if(view == deleteTitleBtn) {
            titleTxt.getText().clear();
            return;
        }
        throw new RuntimeException("Illegal state: Button clicked, but button does not exist.");
    }

    public enum Type {
        CREATE,
        EDIT,
    }
}
