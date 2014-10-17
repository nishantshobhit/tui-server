package tui.dao;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tui.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class TuiDao {

    @Autowired
    private JdbcQueryExecutor queryExecutor;

    public User createUser(String email, String mobile) {
        String id = RandomStringUtils.randomAlphanumeric(8);
        queryExecutor.executeWrite(DatabaseConstants.INSERT_USER, id, email, mobile);
        return new User(id);
    }

    public Feed getEntireFeed() {
        return getFeed(DatabaseConstants.GET_FEED);
    }

    public Feed getFeed() {
        return getFeed(DatabaseConstants.GET_FEED_LAST_WEEK);
    }

    protected Feed getFeed(String query) {
        List<Map<String, Object>> records = queryExecutor.executeRead(query);
        List<Item> items = new ArrayList<>();
        for (Map<String, Object> record : records) {
            Date createdOn = (Date)record.get("created_on");
            items.add(new Item((String)record.get("item"), createdOn));
        }
        return new Feed(items);
    }

    public Report createReport(String userId, String desc, String picture_id, String location) {
        String id = RandomStringUtils.randomAlphanumeric(8);
        queryExecutor.executeWrite(DatabaseConstants.INSERT_REPORT, id, userId, location, desc,
                picture_id, "PENDING");
        return new Report(id, userId, location, desc, picture_id, "PENDING", new Date());
    }

    public void recordParticipation(String userId, String spotfixId) {
        queryExecutor.executeWrite(DatabaseConstants.RECORD_PARTICIPATION, spotfixId, userId);
    }

    public List<Spotfix> getSpotFixesInvolvingMe(String userId) {
        List<Map<String, Object>> records = queryExecutor.executeRead(DatabaseConstants.GET_MY_SPOTFIXES, userId);
        return toSpotfixes(records);
    }

    public List<Spotfix> getAllSpotfixes() {
        List<Map<String, Object>> records = queryExecutor.executeRead(DatabaseConstants.GET_ALL_SPOTFIXES);
        return toSpotfixes(records);
    }

    /**
     * Spotfixes start off as SCHEDULED and can move to "REJECT" or "COMPLETE"
     * @param spotfixId
     * @param state
     */
    public void updateSpotfix(String spotfixId, String state) {
        List<Map<String, Object>> records = queryExecutor.executeRead(DatabaseConstants.GET_SPOTFIX, spotfixId);
        Spotfix sf = toSpotfixes(records).get(0);
        if ("CONFIRMED".equals(state)) {
            queryExecutor.executeWrite(DatabaseConstants.CREATE_ITEM_IN_FEED,
                    "New spotfix confirmed on " + sf.getScheduledOn() + ". Click to view more!");
        } else if ("COMPLETE".equals(state)) {
            queryExecutor.executeWrite(DatabaseConstants.COMPLETE_SPOTFIX, "COMPLETE", spotfixId);
        } else {
            queryExecutor.executeWrite(DatabaseConstants.UPDATE_SPOTFIX, state, spotfixId);
        }
    }

    public List<Report> getAllReports() {
        List<Map<String, Object>> reports = queryExecutor.executeRead(DatabaseConstants.GET_ALL_REPORTS);
        return toReports(reports);
    }

    /**
     * Reports start off as "PENDING" and can move to "REJECT" or "APPROVE". If approved it upgrades to a spotfix
     * @param reportId
     * @param state
     */
    public void updateReport(String reportId, String state, Date scheduledOn) {
        if ("REJECT".equals(state)) {
            queryExecutor.executeWrite(DatabaseConstants.UPDATE_REPORT, state, reportId);
        } else if ("APPROVE".equals(state)) {
            List<Map<String, Object>> reports = queryExecutor.executeRead(DatabaseConstants.GET_REPORT, reportId);
            Report report = toReports(reports).get(0);
            queryExecutor.executeWrite(DatabaseConstants.INSERT_SPOTFIX, RandomStringUtils.randomAlphanumeric(8),
                    report.getUserId(), report.getLocation(), report.getDesc(), report.getPictureId(),
                    "SCHEDULED", new java.sql.Date(scheduledOn.getTime()));
            queryExecutor.executeWrite(DatabaseConstants.DELETE_REPORT, reportId);
        }
    }

    protected List<Spotfix> toSpotfixes(List<Map<String, Object>> records) {
        List<Spotfix> spotfixes = new ArrayList<>();
        for (Map<String, Object> r : records) {
            spotfixes.add(toSpotfix(r));
        }
        return spotfixes;
    }

    protected List<Report> toReports(List<Map<String, Object>> records) {
        List<Spotfix> spotfixes = toSpotfixes(records);
        List<Report> reports = new ArrayList<>();
        for (Spotfix s : spotfixes) {
            reports.add(s);
        }
        return reports;
    }

    protected Spotfix toSpotfix(Map<String, Object> r) {
        return new Spotfix((String) r.get("id"),
                (String) r.get("user_id"),
                (String) r.get("location"),
                (String) r.get("description"),
                (String) r.get("picture_id"), (String) r.get("state"),
                (Date) r.get("created_on"),
                (Date) r.get("scheduled_on"),
                (Date) r.get("executed_on"));
    }

    public void insertItem(String body) {
        queryExecutor.executeWrite(DatabaseConstants.CREATE_ITEM_IN_FEED, body);
    }

    public Spotfix getSpotfix(String id) {
        Map<String, Object> rec = get(DatabaseConstants.GET_SPOTFIX, id);
        Spotfix sf = toSpotfix(rec);
        Map<String, Object> countRow = queryExecutor.executeRead(DatabaseConstants.GET_VOLUNTEERS, id).get(0);
        sf.setVolunteers((Long)countRow.get("C"));
        return sf;
    }

    public Report getReport(String id) {
        Map<String, Object> rec = get(DatabaseConstants.GET_REPORT, id);
        return toSpotfix(rec);
    }

    public Map<String, Object> get(String sql, String id) {
        List<Map<String, Object>> records = queryExecutor.executeRead(sql, id);
        if (records == null || records.size() == 0) {
            return null;
        }
        return records.get(0);
    }
}
