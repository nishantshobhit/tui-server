package tui.model;

import java.util.Date;

public class Spotfix extends Report {

    private Date scheduledOn;
    private Date executedOn;
    private long volunteers;

    public Spotfix(String id, String userId, String location, String desc, String pictureId, String state,
                Date createdOn, Date scheduledOn, Date executedOn) {
        super(id, userId, location, desc, pictureId, state, createdOn);
        this.scheduledOn = scheduledOn;
        this.executedOn = executedOn;
    }

    public Date getScheduledOn() {
        return scheduledOn;
    }

    public Date getExecutedOn() {
        return executedOn;
    }

    public void setVolunteers(long volunteers) {
        this.volunteers = volunteers;
    }

    public long getVolunteers() {
        return volunteers;
    }
}