package zeaze.com.note.Bmob;

import cn.bmob.v3.BmobObject;

public class NoteData extends BmobObject {      //备份的时候上传的数据
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
