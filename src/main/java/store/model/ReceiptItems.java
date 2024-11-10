package store.model;

import java.util.ArrayList;
import java.util.List;

public class ReceiptItems {
    private final List<ReceiptItem> receiptItems;

    public ReceiptItems() {
        receiptItems = new ArrayList<>();
    }

    public void add(final ReceiptItem receiptItem) {
        receiptItems.add(receiptItem);
    }

    public List<ReceiptItem> getSelectedList() {
        return receiptItems;
    }
}
