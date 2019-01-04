package zeaze.com.note.Bmob;

import cn.bmob.v3.BmobObject;

public class NoteData extends BmobObject {
    String noteJson ="";
    String password="";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNoteJson() {
        return noteJson;
    }

    public void setNoteJson(String noteJson) {
        this.noteJson = noteJson;
    }
}
