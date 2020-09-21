package sqliteTest;

public class Game {

	private String saveName;
	private String playerName;
	private int playerScore;
	private int playerHealth;
	private int currentRoomId;
	private int equippedItem;
	
	

	public Game(String saveName, String playerName, int playerScore, int playerHealth, int currentRoomId, int equippedItem) {
		
		this.saveName = saveName;
		this.playerName = playerName;
		this.playerScore = playerScore;
		this.playerHealth = playerHealth;
		this.currentRoomId = currentRoomId;
		this.equippedItem = equippedItem;
		
		
	}
//temp so main can work
	public Game(){

	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}
	public void setPlayerHealth(int playerHealth) {
		this.playerHealth = playerHealth;
	}
	public void setCurrentRoomId(int currentRoomId) {
		this.currentRoomId = currentRoomId;
	}
	public void setEquippedItem(int equippedItem) {
		this.equippedItem = equippedItem;
	}
	public String getSaveName() {
		return this.saveName;
	}
	public String getPlayerName() {
		return this.playerName;
	}
	public int getPlayerScore() {
		return this.playerScore;
	}
	public int getPlayerHealth() {
		return this.playerHealth;
	}
	public int getCurrentRoomId() {
		return this.currentRoomId;
	}
	public int getEquippedItem() {
		return this.equippedItem;
	}
	
	
	
	
	
	
	
}
