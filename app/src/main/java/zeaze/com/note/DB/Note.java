package zeaze.com.note.DB;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note extends LitePalSupport implements Serializable {
    String note="";
    Date date =new Date(),deletedAt=null,createAt;
    int isDeleted=0;         //0为未删除状态，1为删除状态或未删除状态但被选中将要删除，2表示删除状态但被选中将要彻底删除，数据库不会存储2状态，只能存0和1,2是在操作过程中的临时状态

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }


}