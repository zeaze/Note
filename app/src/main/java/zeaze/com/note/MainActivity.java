package zeaze.com.note;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zeaze.com.note.base.pullExtendLayoutForRecyclerView.ExtendListHeader;
import zeaze.com.note.base.pullExtendLayoutForRecyclerView.PullExtendLayoutForRecyclerView;
import zeaze.com.note.DB.Note;
import zeaze.com.note.editNote.EditNote;
import zeaze.com.note.note.present.NotePresent;
import zeaze.com.note.note.view.NoteAdapter;
import zeaze.com.note.note.view.NoteView;

public class MainActivity extends AppCompatActivity implements NoteView {
    String TAG="zeaze2";
    RecyclerView recyclerView;
    TextView delete,recover;
    Spinner title;
    String[] titleArr = {"便签","回收站"};
    NotePresent present;
    NoteAdapter adapter;
    List<Note> notes,allNotes,deleteNotes;
    ImageView build,buildBg;
    ConstraintLayout deleteConstraintLayout;
    View arrow;

    RecyclerView mRecyclerView;
    RecyclerView listHeader;

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

        ArrayAdapter<String> jiachengAdapter = new ArrayAdapter<String>(this, R.layout.adapter_titlespiner, titleArr);
        jiachengAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(jiachengAdapter);
        title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    notes=allNotes;
                    delete.setText("删除");
                    adapter.setNoteStatus(0);
                    adapter.setNotes(allNotes);
                    build.setVisibility(View.VISIBLE);
                    buildBg.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                if (position==1){
                    notes=deleteNotes;
                    delete.setText("彻底删除");
                    adapter.setNoteStatus(1);
                    adapter.setNotes(deleteNotes);
                    build.setVisibility(View.INVISIBLE);
                    buildBg.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        title.setSelection(0, true);

        allNotes=LitePal.where("isDeleted = ?", "0").find(Note.class);
        deleteNotes=LitePal.where("isDeleted = ?", "1").find(Note.class);
        notes=allNotes;

        adapter=new NoteAdapter(this,this,notes);
        RecyclerView.LayoutManager manager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        build=findViewById(R.id.build);
        buildBg=findViewById(R.id.build_bg);
        buildBg.setOnClickListener(new View.OnClickListener() {
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

        recover=findViewById(R.id.recover);
        recover.setVisibility(View.INVISIBLE);
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Note>selectNotes=new ArrayList<>();
                for (Note note:notes){
                    if (note.getIsDeleted()==adapter.getNoteStatus()+1){
                        note.setIsDeleted(0);
                        note.setDate(new Date());
                        selectNotes.add(note);
                        allNotes.add(note);
                        note.delete();
                        note.save();
                    }
                }
                notes.removeAll(selectNotes);
                adapter.isLongClick=false;
                adapter.notifyDataSetChanged();
                recover.setVisibility(View.INVISIBLE);
                deleteConstraintLayout.setVisibility(View.INVISIBLE);
                title.setVisibility(View.VISIBLE);
            }
        });

        deleteConstraintLayout=findViewById(R.id.delete_constraintLayout);
        deleteConstraintLayout.setVisibility(View.INVISIBLE);
        arrow=findViewById(R.id.arrow);
        delete=findViewById(R.id.delete);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isLongClick){
                    onBackPressed();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Note>selectNotes=new ArrayList<>();
                for (Note note:notes){
                    if (note.getIsDeleted()==adapter.getNoteStatus()+1){
                        selectNotes.add(note);
                        if (adapter.getNoteStatus()==0) {
                            note.setDate(new Date());
                            note.delete();
                            note.save();
                            selectNotes.add(note);
                            deleteNotes.add(note);
                        }
                        else {
                            note.delete();
                        }
                    }
                }
                notes.removeAll(selectNotes);
                deleteConstraintLayout.setVisibility(View.INVISIBLE);
                title.setVisibility(View.VISIBLE);
                build.setVisibility(View.VISIBLE);
                buildBg.setVisibility(View.VISIBLE);
                adapter.isLongClick=false;
                adapter.notifyDataSetChanged();
            }
        });

        initSliding();
        allThePermission();
    }
    private void allThePermission(){//获取权限
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.INTERNET);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:{
                for(int result :grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        App.toast("同意权限才能使用天气功能");
                        return;
                    }
                }
            }
            default:
        }
    }

    void initSliding(){

        PullExtendLayoutForRecyclerView pullExtendLayoutForRecyclerView = findViewById(R.id.pull_extend);
        pullExtendLayoutForRecyclerView.setPullLoadEnabled(false);
        ExtendListHeader mPullNewHeader = findViewById(R.id.extend_header);
        mPullNewHeader.init();

    }
    @Override
    public void notifyLongClick() {
        deleteConstraintLayout.setVisibility(View.VISIBLE);
        title.setVisibility(View.INVISIBLE);
        build.setVisibility(View.INVISIBLE);
        buildBg.setVisibility(View.INVISIBLE);
        if (adapter.getNoteStatus()==1){
            recover.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (adapter.isLongClick) {
            adapter.isLongClick=false;
            for (Note note:notes){
                note.setIsDeleted(adapter.getNoteStatus());
            }
            deleteConstraintLayout.setVisibility(View.INVISIBLE);
            title.setVisibility(View.VISIBLE);
            if (adapter.getNoteStatus()==0) {
                build.setVisibility(View.VISIBLE);
                buildBg.setVisibility(View.VISIBLE);
            }
            else {
                build.setVisibility(View.INVISIBLE);
                buildBg.setVisibility(View.INVISIBLE);
            }
            recover.setVisibility(View.INVISIBLE);
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
                        note.save();
                        adapter.notifyDataSetChanged();
                    }
                }
                else if (!notes.get(requestCode).getNote().equals(note.getNote())) {
                    notes.get(requestCode).setDeletedAt(new Date());
                    notes.get(requestCode).setIsDeleted(1);
                    notes.get(requestCode).save();
                    deleteNotes.add(notes.get(requestCode));
                    notes.remove(requestCode);
                    if (!note.getNote().equals("")) {
                        notes.add(note);
                        note.save();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }
}
