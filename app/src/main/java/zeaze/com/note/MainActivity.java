package zeaze.com.note;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zeaze.com.note.data.Note;
import zeaze.com.note.editNote.EditNote;
import zeaze.com.note.note.present.NotePresent;
import zeaze.com.note.note.view.NoteAdapter;
import zeaze.com.note.note.view.NoteView;

public class MainActivity extends AppCompatActivity implements NoteView {
    String TAG="zeaze2";
    RecyclerView recyclerView;
    TextView title;
    NotePresent present;
    NoteAdapter adapter;
    List<Note> notes;
    ImageView build;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        recyclerView=findViewById(R.id.recyclerView);
        title=findViewById(R.id.title);

        present=new NotePresent(this);

        title.setText("便签");

        notes=LitePal.where("isDeleted = ?", "0").find(Note.class);
        adapter=new NoteAdapter(this,notes);
        RecyclerView.LayoutManager manager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        build=findViewById(R.id.build_bg);
        build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note=null;
                Intent intent=new Intent(MainActivity.this,EditNote.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("note",note);
                intent.putExtras(bundle);
                startActivityForResult(intent,notes.size());
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (adapter.isLongClick) {
            adapter.isLongClick=false;
            for (Note note:notes){
                note.setIsDeleted(0);
            }
            adapter.notifyDataSetChanged();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: "+requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            Note note=(Note) bundle.get("note");
            if (note==null){
                Log.d(TAG, "onActivityResult: "+"note is null");
            }
            else {
                if (requestCode == notes.size()) {
                    if (!note.getNote().equals("")) {
                        notes.add(note);
                        if (note.save()) {
                            MyApplication.toast("存储成功");
                        } else {
                            MyApplication.toast("存储失败");
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else if (!notes.get(requestCode).getNote().equals(note.getNote())) {
                    notes.get(requestCode).setDeletedAt(new Date());
                    notes.get(requestCode).setIsDeleted(1);
                    if (notes.get(requestCode).save()) {
                        MyApplication.toast("删除成功");
                        Log.d(TAG, "onActivityResult: "+"删除成功");
                    } else {
                        MyApplication.toast("删除失败");
                        Log.d(TAG, "onActivityResult: "+"删除失败");
                    }
                    notes.remove(requestCode);
                    if (!note.getNote().equals("")) {
                        notes.add(note);
                        if (note.save()) {
                            MyApplication.toast("存储成功");
                            Log.d(TAG, "onActivityResult: "+"存储成功");
                        } else {
                            MyApplication.toast("存储失败");
                            Log.d(TAG, "onActivityResult: "+"存储失败");
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }
}
