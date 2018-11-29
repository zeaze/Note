package zeaze.com.note.DB;

import org.litepal.crud.LitePalSupport;

public class Weather extends LitePalSupport {
    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
