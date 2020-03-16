package hieubd.jdbc;

public class JDBCObject {
    private String serverName;
    private String userId;
    private String password;
    private String database;
    private String sClass;
    private String port;

    public JDBCObject(String serverName, String userId, String password, String database, String port) {
        this.serverName = serverName;
        this.userId = userId;
        this.password = password;
        this.database = database;
        this.sClass = "net.sourceforge.jtds.jdbc.Driver";
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
