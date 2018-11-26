package zeaze.com.note.note.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import zeaze.com.note.MainActivity;
import zeaze.com.note.R;
import zeaze.com.note.data.Note;
import zeaze.com.note.editNote.EditNote;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context context;
    List<Note>notes;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public boolean isLongClick;
    String TAG="zeaze2";

    public NoteAdapter(Context context, List<Note>notes){
        this.context=context;
        this.notes=notes;
        isLongClick=false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_note,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final int ii=notes.size()-i-1;
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        if (!isLongClick){
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
            viewHolder.checkBox.setChecked(false);
            viewHolder.item.setBackgroundResource(R.drawable.note_bg);
        }
        else {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            if (notes.get(ii).getIsDeleted()==1){
                viewHolder.checkBox.setChecked(true);
                viewHolder.item.setBackgroundResource(R.drawable.note_bg_select);
            }
            else {
                viewHolder.checkBox.setChecked(false);
                viewHolder.item.setBackgroundResource(R.drawable.note_bg);
            }
            viewHolder.checkBox.refreshDrawableState();
        }
        viewHolder.note.setText(notes.get(ii).getNote());
        viewHolder.date.setHint(formatter.format(notes.get(ii).getDate()));

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    notes.get(ii).setIsDeleted(1);
                    viewHolder.item.setBackgroundResource(R.drawable.note_bg_select);
                }
                else {
                    notes.get(ii).setIsDeleted(0);
                    viewHolder.item.setBackgroundResource(R.drawable.note_bg);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isLongClick) {
                    isLongClick=true;
                    notes.get(ii).setIsDeleted(1);
                    viewHolder.checkBox.setChecked(true);
                    notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLongClick){
                    if (viewHolder.checkBox.isChecked()){
                        viewHolder.checkBox.setChecked(false);
                    }
                    else {
                        viewHolder.checkBox.setChecked(true);
                    }
                }
                else {
                    Intent intent=new Intent(context,EditNote.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("note",notes.get(ii));
                    intent.putExtras(bundle);
                    ((AppCompatActivity)context).startActivityForResult(intent,ii);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView note,date;
        public CheckBox checkBox;
        public ConstraintLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            note=itemView.findViewById(R.id.note);
            date=itemView.findViewById(R.id.date);
            checkBox=itemView.findViewById(R.id.check_box);
            item=itemView.findViewById(R.id.item);

        }
    }
}