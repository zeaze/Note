package zeaze.com.note.editNote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import zeaze.com.note.R;
import zeaze.com.note.data.Note;

public class EditNote extends AppCompatActivity {
    View arrow;
    TextView date,edit_note;
    ImageView image;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Note note;
    String TAG="zeaze2";

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

        image=findViewById(R.id.image);
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);

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

        date.setHint(formatter.format(note.getDate()));
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edit_note.setText(note.getNote());

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
