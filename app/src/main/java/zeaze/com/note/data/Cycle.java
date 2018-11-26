package zeaze.com.note.data;

import org.litepal.crud.LitePalSupport;

public class Cycle extends LitePalSupport {
    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
