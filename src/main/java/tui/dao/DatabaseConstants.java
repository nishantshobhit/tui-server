package tui.dao;

import org.springframework.stereotype.Component;

@Component
public class DatabaseConstants {

    public String driver = "com.mysql.jdbc.Driver";
    public String databaseUrl = "jdbc:mysql://<DB_HOST>/tui";
    public String user = "<DB_USER>;
    public String password = "<DB_PASSWORD>";

    // queries

    public static final String INSERT_USER =
            "insert into users(id, email, mobile) values(?, ?, ?)";

    public static final String GET_FEED_LAST_WEEK =
            "select * from feeds where TIMESTAMPDIFF(HOUR, created_on, now()) < 24*7 order by created_on desc";

    public static final String GET_FEED = "select * from feeds order by created_on desc";

    public static final String CREATE_ITEM_IN_FEED = "insert into feeds(item, created_on) VALUES (?, now())";

    public static final String INSERT_REPORT =
            "insert into reports(id, user_id, location, description, picture_id, state) VALUES (?,?,?,?,?,?)";

    public static final String INSERT_SPOTFIX =
            "insert into spotfixes(id, user_id, location, description, picture_id, state, scheduled_on)" +
                    "VALUES (?,?,?,?,?,?,?)";

    public static final String RECORD_PARTICIPATION =
            "insert into volunteers (spotfix_id, user_id) VALUES (?,?)";

    public static final String GET_MY_SPOTFIXES =
            "select S.* from spotfixes S, volunteers V where V.user_id = ? and S.id = V.spotfix_id";

    public static final String DELETE_REPORT = "delete from reports where id = ?";

    public static final String GET_ALL_SPOTFIXES = "select * from spotfixes where state != \"REJECT\"";

    public static final String GET_REPORT = "select * from reports where id = ?";

    public static final String GET_SPOTFIX = "select * from spotfixes where id = ?";

    public static final String GET_ALL_REPORTS = "select * from reports where state != \"REJECT\"";

    public static final String COMPLETE_SPOTFIX = "update spotfixes set state = ?, executed_on = now() where id = ?";

    public static final String UPDATE_SPOTFIX = "update spotfixes set state = ? where id = ?";

    public static final String UPDATE_REPORT = "update reports set state = ? where id = ?";

    public static final String GET_VOLUNTEERS = "select count(*) C from volunteers where spotfix_id = ?";
}
