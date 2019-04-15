package ly.entity;

public class MailMessage {

    private String _from;
    private String _message;
    private String _subject;
    private String _to;
    private static final MailMessage _instance = new MailMessage();
    public static MailMessage getInstance() {
        return _instance;
    }

    public void setData(String from, String to, String subject, String message) {
        this._from = from;
        this._message = message;
        this._subject = subject;
        this._to = to;
    }

    public String getFrom() {
        return this._from;
    }

    public String getMessage() {
        return this._message;
    }

    public String getSubject() {
        return this._subject;
    }

    public String getTo() {
        return this._to;
    }

    public void setFrom(String from) {
        this._from = from;
    }

    public void setMessage(String message) {
        this._message = message;
    }

    public void setSubject(String subject) {
        this._subject = subject;
    }

    public void setTo(String to) {
        this._to = to;
    }
}

