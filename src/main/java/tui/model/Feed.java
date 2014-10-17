package tui.model;

import java.util.List;

public class Feed {

    List<Item> items;

    public Feed(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}
