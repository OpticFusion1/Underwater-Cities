package sqliteTest;

public class Room {
    private int roomId;
    private String roomName;
    private boolean hasPuzzle;
    private  String roomDescription;
    private String helpText;
    private String connectedRoom;
    private String connectionCommands;
//removed hasItem not neccesary for type of implementation, look at InventoryDataController
    public Room(int roomId, String roomName, boolean hasPuzzle, String roomDescription, String helpText,
    		String connectedRoom, String connectionCommands){
        this.roomId = roomId;
        this.roomName = roomName;
        this.hasPuzzle = hasPuzzle;
        this.roomDescription = roomDescription;
        this.helpText = helpText;
        this.connectedRoom = connectedRoom;
        this.connectionCommands = connectionCommands;
        
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean isHasPuzzle() {
        return hasPuzzle;
    }

    public void setHasPuzzle(boolean hasPuzzle) {
        this.hasPuzzle = hasPuzzle;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getConnectedRoom() {
		return connectedRoom;
	}

	public String getConnectionCommands() {
		return connectionCommands;
	}

	public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
