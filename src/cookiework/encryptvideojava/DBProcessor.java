package cookiework.encryptvideojava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/04/27.
 */
public class DBProcessor {
    private String dbAddress;
    private String dbUsername;
    private String dbPassword;

    public DBProcessor(String dbAddress, String dbUsername, String dbPassword) {
        this.dbAddress = dbAddress;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public String getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(String dbAddress) {
        this.dbAddress = dbAddress;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public void execute(int videoId) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement query = conn.prepareStatement("UPDATE storedvideos SET status=? WHERE id=?");
        query.setString(1, "vod");
        query.setInt(2, videoId);
        query.executeUpdate();
    }

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("bad Driver: " + e);
        }
        return DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
    }
}
