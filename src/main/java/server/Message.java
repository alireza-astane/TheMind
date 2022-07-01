package server;

public class Message {
    private MessageType type;
    private String Text;

    public Message(MessageType type, String text) {
        this.type = type;
        Text = text;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
