package Logic;

public class ClientMessage {
    public ClientMessage(int authToken,int targetId,MessageType messageType,String context){
        this.authToken=authToken;
        this.context=context;
        this.type=messageType;
        this.targetId=targetId;
    }
    protected int authToken;
    protected MessageType type;
    protected String context;

    protected  int targetId;

    public String getContext() {
        return context;
    }

    public MessageType getType() {
        return type;
    }

    public int getAuthToken() {
        return authToken;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
}
