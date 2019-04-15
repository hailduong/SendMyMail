package ly.entity;

public class SMTPServer {
    private Number _port;
    private String _username;
    private String _password;
    private String _server;
    private static SMTPServer _instance = new SMTPServer();

    public static SMTPServer getInstance() {
        return _instance;
    }

    public void setData(Number port, String server, String username, String password) {
        this._port = port;
        this._server = server;
        this._username = username;
        this._password = password;
    }

    public void setPort(Number port) {
        this._port = port;
    }

    public void setServer(String server) {
        this._server = server;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public Number getPort() {
        return this._port;
    }

    public String getServer() {
        return this._server;
    }

    public String getUsername() {
        return this._username;
    }

    public String getPassword() {
        return this._password;
    }
}
