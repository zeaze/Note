package zeaze.com.note.editNote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import zeaze.com.note.R;
import zeaze.com.note.DB.Note;

public class EditNote extends AppCompatActivity {
    private View arrow;
    private TextView date,edit_note,save,notSave;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Note note;
    private String TAG="zeaze2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        note=(Note)bundle.get("note");
        if (note==null) {
            note = new Note();
        }
        if (note.getCreateAt()==null){
            note.setCreateAt(new Date());
        }
        note.setDate(new Date());

        arrow=findViewById(R.id.arrow);
        date=findViewById(R.id.date);
        edit_note=findViewById(R.id.note);
        save=findViewById(R.id.save);
        notSave=findViewById(R.id.not_save);

        save.setVisibility(View.INVISIBLE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        notSave.setVisibility(View.INVISIBLE);
        notSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        date.setHint(formatter.format(note.getDate()));

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edit_note.setText(note.getNote());

        edit_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edit_note.getText().toString().equals("")&&save.getVisibility()==View.INVISIBLE){
                    save.setVisibility(View.VISIBLE);
                    notSave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        note.setNote(edit_note.getText().toString());
        bundle.putSerializable("note",note);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}
