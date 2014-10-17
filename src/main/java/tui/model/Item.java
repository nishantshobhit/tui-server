package tui.model;

import java.util.Date;

public class Item {

    private String content;
    private Date createdOn;

    public Item(String content, Date createdOn) {
        this.content = content;
        this.createdOn = createdOn;
    }

    public String getContent() {
        return this.content;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
}
