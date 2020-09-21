package sqliteTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RoomDataController {

	private Connection conn;
	//maybe current room stored here
	private Room currentRoom;
	private ArrayList<Integer> connectedRoom;
	private ArrayList<String> connectionCommands;
	private PuzzleDataController puzzleController;
	private Room prevRoom;
	
	public RoomDataController(int initialRoomId, PuzzleDataController puzzleController) {
		
		
	
		this.connectedRoom = new ArrayList<>();
		this.connectionCommands= new ArrayList<>();
		this.puzzleController = puzzleController;
		this.currentRoom = findRoom(initialRoomId);
		loadPaths();
		
	}
	
	
	
	
	/* This can be removed because of getCurrentRoom() 
	 * public boolean getHasPuzzle(int roomId) {
	 * 
	 * 
	 * 
	 * 
	 * return false; }
	 * 
	 * public String getRoomDescription(int roomId) {
	 * 
	 * 
	 * return null; }
	 * 
	 * public String getRoomName(int roomId) {
	 * 
	 * 
	 * return null; }
	 */
	
	/*
	 * public String help(int ID){ //call the help text for a room. return null; }
	 */
	
	public String getDirections() {
		
		String returnString = "";
		for(String s: this.connectionCommands) returnString += s+"\n";
		
		return returnString;
		
	}
	
	
	
	
	//checks to see if what the player want to interact with exists
	public int interact(int decision,int itemID, int roomID, String savename){
		//asks the user for input and puzzle or item? (done)
		//if puzzle, checks if there's a puzzle in the room with an sql query (resultset not void) (checks if already complete too)
		int what = 0;

		if (decision ==1){

			if(currentRoom.isHasPuzzle()) {
				if (!puzzleController.isComplete(roomID, savename))
					what =1;
				else
					what =-1;
			}
			//return 1 to make the main go through the puzzle game flow
		}

		else if(decision ==2){
			what = 2;

		}
			return what;

		}


	
	//modified to return false if command not recognized, and true if room changed.
	public boolean move(String command, Game currentGame){
		
		this.prevRoom = this.currentRoom;
		
		if(this.connectionCommands.contains(command)) {
			setCurrentVisitFlag(currentGame);
			this.currentRoom = findRoom(this.connectedRoom.get(this.connectionCommands.indexOf(command)));
		    loadPaths();
			currentGame.setCurrentRoomId(this.currentRoom.getRoomId());
			
			return true;
		}
			else return false;
	}
	
	public boolean forcedMove(int roomId, Game currentGame){
		
		
		
			this.prevRoom = this.currentRoom;
			setCurrentVisitFlag(currentGame);
			this.currentRoom = findRoom(roomId);
		    loadPaths();
			currentGame.setCurrentRoomId(this.currentRoom.getRoomId());
			
			
		
			return true;
	}
	
	
	

	public Room getCurrentRoom(){
		return this.currentRoom;
	}
	
	//regenerates the roomId list, and the connectionCommand list
	// the format of connectedRoom in the db is a string with this format  0,1,2,3,4,  any number but unique
	private void loadPaths() {
	
		this.connectedRoom.clear();
		String roomString = this.currentRoom.getConnectedRoom();
		int strIndex = 0;
		String holderString = "";
		// handles refilling room Id list
		while(!roomString.isEmpty()) {
			
			strIndex = roomString.indexOf(",");
			//System.out.println("-"+roomString.substring(0,strIndex)+"-");
			holderString = roomString.substring(0,strIndex);
			this.connectedRoom.add(Integer.parseInt(holderString.trim()));
			roomString = roomString.substring(strIndex+1);
		}
		
		this.connectionCommands.clear();
		String commandString = this.currentRoom.getConnectionCommands();
		strIndex=0;
		//handles refilling room transition command list
		
		while(!commandString.isEmpty()) {
			//System.out.println(this.currentRoom.getConnectionCommands());
			strIndex = commandString.indexOf(",");
			this.connectionCommands.add(commandString.substring(0,strIndex));
			commandString = commandString.substring(strIndex+1);
		}
		
	}
	
	
	
	private Room findRoom(int roomId) {
		
		try {
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			Statement roomQueryState = this.conn.createStatement();
			
			String roomQuery = "SELECT * FROM `Room` WHERE Room.roomId = "+roomId;
			
			//System.out.println("HAHAHH "+roomQuery);
			
			ResultSet roomReturn = roomQueryState.executeQuery(roomQuery);
			
			
			
			Room returnRoom = new Room(roomReturn.getInt("roomId"),roomReturn.getString("roomName"),roomReturn.getBoolean("hasPuzzle")
					,roomReturn.getString("roomDescription"),roomReturn.getString("helpText")
					,roomReturn.getString("connectedRoom"), roomReturn.getString("connectionCommands"));
			
			this.conn.close();
			
			return returnRoom;
			
			
		}catch(SQLException e) {
		
			e.printStackTrace();
		
		}
		
		return null;
	}
	
	
	public boolean getCurrentVisitFlag(String saveName) {
		
		try {
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement flagQuery = this.conn.createStatement();
			
			String flagQueryText = "SELECT roomVisited FROM PlayerRoom WHERE roomId = "+this.currentRoom.getRoomId()
				+ " AND saveName = '"+ saveName+"';";
			
			ResultSet flagReturn = flagQuery.executeQuery(flagQueryText);
			
			boolean returnVisited = flagReturn.getBoolean("roomVisited");
			
			this.conn.close();
			
			return returnVisited;
			
		}catch(SQLException e) {
		e.printStackTrace();
	}
		
		return false;
		
	}


	private void setCurrentVisitFlag(Game currentGame) {
		
		try {
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			Statement changeFlagState = this.conn.createStatement();
			String changeQuery = "UPDATE PlayerRoom SET roomVisited = 1 WHERE saveName = '"
			+currentGame.getSaveName()+"' AND roomId = "+this.currentRoom.getRoomId()+";";
			changeFlagState.execute(changeQuery);
			this.conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public Room getPreviousRoom() {
		
		return this.prevRoom;
	}
	
	
	
	
	
	
	
}
