package Logic;

public class ServerMessage {
    public ServerMessage(MessageType messageType,String context){
        this.context=context;
        this.type=messageType;

    }
    protected MessageType type;
    protected String context;

    public MessageType getType() {
        return type;
    }

    public String getContext() {
        return context;
    }
}
