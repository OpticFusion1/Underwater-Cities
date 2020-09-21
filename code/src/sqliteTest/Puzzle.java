package sqliteTest;

public class Puzzle {

	private boolean puzzleNeedsItem;
	private String helpText;
	private String completionCommand;
	private String completionText;
	private String description;
	private int roomId;
	private int puzzleId;
	private int itemId;
	
	public Puzzle(int puzzleId,boolean puzzleNeedsItem, String helpText, String completionCommand, String completionText,
			String description, int roomId) {
		
		this.puzzleId = puzzleId;
		this.puzzleNeedsItem = puzzleNeedsItem;
		this.helpText = helpText;
		this.completionCommand = completionCommand;
		this.completionText = completionText;
		this.description = description;
		this.roomId = roomId;
		//-66 implies null in this
		this.itemId = -66;
	}


	public boolean isPuzzleNeedsItem() {
		return puzzleNeedsItem;
	}


	public String getHelpText() {
		return helpText;
	}


	public String getCompletionCommand() {
		return completionCommand;
	}


	public String getCompletionText() {
		return completionText;
	}


	public String getDescription() {
		return description;
	}


	public int getRoomId() {
		return roomId;
	}
	
	
	//this is only used for a small coupling with PuzzleDataController
	
	 public void internalSetItemId(int itemId) {
	 
	  this.itemId = itemId;
	 
	}

	public int getItemId() {
		return itemId;
	}


	public int getPuzzleId() {
		return puzzleId;
	}
}
