package zeaze.com.note;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import zeaze.com.note.Bmob.NoteData;
import zeaze.com.note.base.pullExtendLayoutForRecyclerView.ExtendListHeader;
import zeaze.com.note.base.pullExtendLayoutForRecyclerView.PullExtendLayoutForRecyclerView;
import zeaze.com.note.DB.Note;
import zeaze.com.note.editNote.EditNote;
import zeaze.com.note.note.present.NotePresent;
import zeaze.com.note.note.view.NoteAdapter;
import zeaze.com.note.note.view.NoteView;

public class MainActivity extends AppCompatActivity implements NoteView {
    String TAG="zeaze2";
    private RecyclerView recyclerView;
    private TextView delete,recover,backups;
    private Spinner title,viewMode;
    private String[] titleArr = {"便签","回收站"},viewModeArr = {"单列","双列"};
    private NotePresent present;
    private NoteAdapter adapter;
    private List<Note> notes,allNotes,deleteNotes;
    private ImageView build,buildBg;
    private ConstraintLayout deleteConstraintLayout;
    private View arrow;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

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

        initBackups();

        recyclerView=findViewById(R.id.recyclerView);
        title=findViewById(R.id.title);
        viewMode=findViewById(R.id.view_mode);
        pref = getSharedPreferences("note",MODE_PRIVATE);
        editor = pref.edit();

        present=new NotePresent(this);

        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this, R.layout.adapter_titlespiner, titleArr);
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(titleAdapter);
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
                    backups.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                if (position==1){
                    notes=deleteNotes;
                    delete.setText("彻底删除");
                    adapter.setNoteStatus(1);
                    adapter.setNotes(deleteNotes);
                    build.setVisibility(View.INVISIBLE);
                    buildBg.setVisibility(View.INVISIBLE);
                    backups.setVisibility(View.INVISIBLE);
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
        recyclerView.setAdapter(adapter);
        if (pref.getInt("viewMode",1)==0) {
            RecyclerView.LayoutManager manager=new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(manager);
        }
        else {
            RecyclerView.LayoutManager manager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
        }
        adapter.notifyDataSetChanged();

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, R.layout.adapter_modespiner, viewModeArr);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewMode.setAdapter(modeAdapter);
        viewMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    if (pref.getInt("viewMode",1)!=0) {
                        editor.putInt("viewMode", 0);
                        editor.apply();
                        RecyclerView.LayoutManager manager=new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(manager);
                    }
                }
                if (position==1){
                    if (pref.getInt("viewMode",1)!=1) {
                        editor.putInt("viewMode", 1);
                        editor.apply();
                        RecyclerView.LayoutManager manager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(manager);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewMode.setSelection(pref.getInt("viewMode",1), true);

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
                viewMode.setVisibility(View.VISIBLE);
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
                viewMode.setVisibility(View.VISIBLE);
                build.setVisibility(View.VISIBLE);
                buildBg.setVisibility(View.VISIBLE);
                if (adapter.getNoteStatus()==0) {
                    backups.setVisibility(View.VISIBLE);
                }
                adapter.isLongClick=false;
                adapter.notifyDataSetChanged();
            }
        });

        initSliding();
        allThePermission();
    }

    private void initBackups(){
        backups=findViewById(R.id.backups);
        backups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.backups, null);
                builder.setView(view);
                AlertDialog dialog = builder.show();
                final EditText password;
                TextView upload,download;
                password=view.findViewById(R.id.password);
                upload=view.findViewById(R.id.upload);
                download=view.findViewById(R.id.download);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (password.getText().toString().equals("")){
                            App.toast("请输入密码");
                        }
                        else {
                            final NoteData noteData=new NoteData();
                            noteData.setPassword(password.getText().toString());
                            noteData.setNoteJson(JSON.toJSONString(allNotes));
                            noteData.save(new SaveListener<String>() {
                                @Override
                                public void done(String objectId, BmobException e) {
                                    if (e == null) {
                                        App.toast("备份成功");
                                    } else {
                                        if (e.getErrorCode() == 401) {
                                            BmobQuery query = new BmobQuery("NoteData");
                                            query.addWhereEqualTo("password", noteData.getPassword());
                                            query.setLimit(1);
                                            query.order("createdAt");
                                            query.findObjectsByTable(new QueryListener<JSONArray>() {
                                                @Override
                                                public void done(JSONArray ary, BmobException e) {
                                                    if (e == null) {
                                                        final List<NoteData> noteDatas = JSON.parseArray(ary.toString(), NoteData.class);
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                        builder.setTitle("备份失败")
                                                                .setMessage("数据库已经有该密码的备份，点击确认则覆盖数据库数据")
                                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        noteData.setObjectId(noteDatas.get(0).getObjectId());
                                                                        noteData.update(new UpdateListener() {
                                                                            @Override
                                                                            public void done(BmobException e) {
                                                                                if (e == null) {
                                                                                    App.toast("备份成功");
                                                                                } else {
                                                                                    App.toast("备份失败,原因：" + e.getMessage() + e.getErrorCode());
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                })
                                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {

                                                                    }
                                                                })
                                                                .create()
                                                                .show();
                                                    } else {
                                                        App.toast("备份失败,原因：" + e.getMessage() + e.getErrorCode());
                                                    }
                                                }
                                            });
                                        } else {
                                            App.toast("备份失败,原因：" + e.getMessage() + e.getErrorCode());
                                        }
                                    }
                                }
                            });
                        }
                    }
                });

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (password.getText().toString().equals("")){
                            App.toast("请输入密码");
                        }
                        else {
                            BmobQuery<NoteData> categoryBmobQuery = new BmobQuery<>();
                            categoryBmobQuery.addWhereEqualTo("password", password.getText().toString());
                            categoryBmobQuery.findObjects(new FindListener<NoteData>() {
                                @Override
                                public void done(List<NoteData> object, BmobException e) {
                                    if (e == null) {
                                        if (object.size()==0){
                                            App.toast("数据库不存在该备份");;
                                        }
                                        else {
                                            String noteJson=object.get(0).getNoteJson();
                                            List<Note> downloadNote=JSON.parseArray(noteJson,Note.class);
                                            for (Note note:downloadNote){
                                                boolean b=true;
                                                for (Note note1:allNotes){
                                                    if (note.getNote().equals(note1.getNote())){
                                                        b=false;
                                                        break;
                                                    }
                                                }
                                                if (b){
                                                    note.save();
                                                    allNotes.add(note);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                            App.toast("下载成功");
                                        }
                                    } else {
                                        App.toast("下载失败,原因：" + e.getMessage() + e.getErrorCode());
                                    }
                                }
                            });

                        }
                    }
                });

            }
        });
    }

    private void allThePermission(){
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
        viewMode.setVisibility(View.INVISIBLE);
        build.setVisibility(View.INVISIBLE);
        buildBg.setVisibility(View.INVISIBLE);
        if (adapter.getNoteStatus()==1){
            recover.setVisibility(View.VISIBLE);
        }
        backups.setVisibility(View.INVISIBLE);
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
            viewMode.setVisibility(View.VISIBLE);
            if (adapter.getNoteStatus()==0) {
                build.setVisibility(View.VISIBLE);
                buildBg.setVisibility(View.VISIBLE);
                backups.setVisibility(View.VISIBLE);
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
