package zeaze.com.note.note.present;

import zeaze.com.note.note.modle.NoteListener;
import zeaze.com.note.note.modle.NoteModle;
import zeaze.com.note.note.view.NoteView;

public class NotePresent implements NoteListener {
    NoteModle modle;
    NoteView view;

    public NotePresent(NoteView view){
        modle=new NoteModle(this);
        this.view=view;
    }


}
