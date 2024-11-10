package store.model;

import java.util.ArrayList;
import java.util.List;

public class SelectItems {
    private final List<SelectItem> selectItems;

    public SelectItems() {
        selectItems = new ArrayList<>();
    }

    public void add(SelectItem selectItem) {
        selectItems.add(selectItem);
    }

    public List<SelectItem> getSelectedList() {
        return selectItems;
    }
}
