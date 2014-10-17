package tui.model;

import java.util.Date;

public class Report {

    private String id;
    private String userId;
    private String location;
    private String desc;
    private String pictureId;
    private String state;
    private Date createdOn;

    public Report(String id, String userId, String location, String desc, String pictureId, String state,
                   Date createdOn) {
        this.id = id;
        this.userId = userId;
        this.location = location;
        this.desc = desc;
        this.pictureId = pictureId;
        this.state = state;
        this.createdOn = createdOn;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getLocation() {
        return location;
    }

    public String getDesc() {
        return desc;
    }

    public String getPictureId() {
        return pictureId;
    }

    public String getState() {
        return state;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
}
