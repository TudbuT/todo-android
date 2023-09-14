package de.tudbut.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import kotlin.Unit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<View> fields = new ArrayList<>();
    ArrayList<CheckBox> boxes = new ArrayList<>();
    Button createBtn, reloadBtn;
    TableLayout listLyt;
    View dividerVw;

    long lastCBClick = 0;
    View lastCBClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.populate(getApplicationContext().getApplicationInfo().dataDir);
        setContentView(R.layout.activity_main);
        createBtn = findViewById(R.id.create_btn);
        reloadBtn = findViewById(R.id.reload_btn);
        listLyt = findViewById(R.id.main_list);
        dividerVw = findViewById(R.id.divider);
        createBtn.setOnClickListener(this);
        reloadBtn.setOnClickListener(this);

        reload();
    }

    private void reload() {
        listLyt.removeAllViews();
        fields.clear();
        boxes.clear();
        ToDoList list = Data.getList();
        for (int i = 0; i < list.size(); i++) {
            ToDoItem item = list.get(i);
            TableRow row = new TableRow(this);
            TextView tit = new TextView(this);
            tit.setText(item.title);
            tit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            row.addView(tit);
            CheckBox box = new CheckBox(this);
            box.setChecked(item.done);
            row.addView(box);
            listLyt.addView(row);

            row.setOnClickListener(this);
            fields.add(row);
            box.setOnClickListener(this);
            boxes.add(box);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == createBtn) {
            EditActivity.editType = EditActivity.Type.CREATE;
            startActivity(new Intent(this, EditActivity.class));
            return;
        }
        if (view == reloadBtn) {
            reload();
            return;
        }
        {
            int idx = fields.indexOf(view);
            if (idx != -1) {
                EditActivity.editType = EditActivity.Type.EDIT;
                EditActivity.editIndex = idx;
                startActivity(new Intent(this, EditActivity.class));
                return;
            }
        }
        if (view instanceof CheckBox) {
            int idx = boxes.indexOf(view);
            if (idx != -1) {
                ToDoList list = Data.getList();
                boolean checked = ((CheckBox) view).isChecked();
                list.get(idx).done = checked;
                if (checked) {
                    // move to bottom
                    boxes.add(boxes.remove(idx));
                    fields.add(fields.remove(idx));
                    list.add(list.remove(idx));
                } else {
                    // move to top
                    boxes.add(0, boxes.remove(idx));
                    fields.add(0, fields.remove(idx));
                    list.add(0, list.remove(idx));
                }
                Data.save();
                if (System.currentTimeMillis() - lastCBClick < 300 && lastCBClicked == view) {
                    reload();
                }
                lastCBClicked = view;
                lastCBClick = System.currentTimeMillis();
                return;
            }
        }
        throw new RuntimeException("Illegal state: Button clicked, but button does not exist.");
    }
}