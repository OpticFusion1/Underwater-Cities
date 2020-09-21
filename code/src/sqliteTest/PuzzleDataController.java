package sqliteTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PuzzleDataController {
	private Connection conn;
	private Puzzle currentPuzzle;
	private ArrayList<Item> requiredItems;

	public PuzzleDataController() {

		this.requiredItems = new ArrayList<>();
		

	}
	
	

	//returns puzzle sds
	public boolean getPuzzle(int roomId, String saveName) {
		boolean puzz = false;
		Puzzle newPuzzle = new Puzzle(-6,false,null,null,null,null,-6);
		
		try {

			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement creationStatement = this.conn.createStatement();

			String firstQuery = "SELECT * FROM Puzzle WHERE roomId = "+roomId;


			ResultSet firstQueryReturn = creationStatement.executeQuery(firstQuery);
			


			 newPuzzle = new Puzzle(firstQueryReturn.getInt("puzzleId"), firstQueryReturn.getBoolean("puzzleNeedsItem")
						, firstQueryReturn.getString("puzzleHelpText"), firstQueryReturn.getString("puzzleCompletionCommand")
						, firstQueryReturn.getString("puzzleCompletionText"), firstQueryReturn.getString("description")
						, firstQueryReturn.getInt("roomId"));

				//accounts for the potential of a puzzle needing a equipped item.
			if (firstQueryReturn.getBoolean("puzzleNeedsItem")) {

				String secondQuery = "SELECT itemId FROM PuzzleItem WHERE puzzleId = " + firstQueryReturn.getString("puzzleId");

				ResultSet secondQueryReturn = creationStatement.executeQuery(secondQuery);
					
				
				
				newPuzzle.internalSetItemId(secondQueryReturn.getInt("itemId"));
			}

				puzz = true;
				
				this.currentPuzzle = newPuzzle;
				
				this.conn.close();
				System.out.println(this.currentPuzzle.getDescription());
				generateRequired(saveName);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}



		this.currentPuzzle = newPuzzle;
		return puzz;
	}


	//sets the puzzle to completed.
	//returns false if set failed
	public boolean setComplete(int puzzleId, String saveName) {


		try {

			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement updateStatement = this.conn.createStatement();

			String updateText = "UPDATE PlayerPuzzle SET puzzleComplete = 1 WHERE saveName = '"+saveName+"' AND puzzleId"
					+ " = '"+puzzleId+"';";

			//System.out.println(updateText);
			
			updateStatement.execute(updateText);

			this.conn.close();
			
			return true;

		}catch(SQLException e) {
			e.printStackTrace();
		}


		return false;
	}

	//given the puzzles puzzleId returns whether the puzzle is completed for a given save.
	// will return false additionally on a failure.
	public boolean isComplete(int roomId, String saveName) {

		try {
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement queryStatement = this.conn.createStatement();

			String textQuery = "SELECT puzzleComplete, Puzzle.roomId FROM PlayerPuzzle, Puzzle "
					+ "WHERE Puzzle.puzzleId = PlayerPuzzle.puzzleId AND roomId ="+roomId
					+ " AND saveName = '"+saveName+"';";

			//System.out.println(textQuery);
			
			ResultSet queryReturn = queryStatement.executeQuery(textQuery);

			boolean returnBool = queryReturn.getBoolean("puzzleComplete");
			
			this.conn.close();
			
			return returnBool;


		}catch(SQLException e){
			//e.printStackTrace();
		}


		




		return false;
	}

	public Puzzle getCurrentPuzzle(){
		return currentPuzzle;
	}

	
	
	public boolean isEquip() {
		
		try{
			
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement equipStatement = this.conn.createStatement();
			
			String equipQuery = "SELECT isUsed FROM PuzzleItem WHERE puzzleId = "+this.currentPuzzle.getPuzzleId()+";";
			
			ResultSet returnQuery = equipStatement.executeQuery(equipQuery);
			
			boolean returnBool = returnQuery.getBoolean("isUsed");
			
			this.conn.close();
			
			return returnBool;
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	
	private void generateRequired(String saveName) {
		
		try {
			
			this.requiredItems.clear();
			
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement queryStatement = this.conn.createStatement();
			
			String puzzleQuery = "SELECT Item.itemName, Item.itemId, Item.itemDescription FROM "
					+ "Item, PlayerPuzzleItem, PuzzleItem WHERE Item.itemId = PuzzleItem.itemId AND PlayerPuzzleItem.puzzleItemId = PuzzleItem.puzzleItemId"
					+ " AND PlayerPuzzleItem.saveName = '"+saveName+"' AND PlayerPuzzleItem.isDone = 0"
							+ " AND PuzzleItem.puzzleId = "+this.currentPuzzle.getPuzzleId()+";";
			
			
			//System.out.println(puzzleQuery);
			
			ResultSet queryReturn = queryStatement.executeQuery(puzzleQuery);
					
			while(queryReturn.next()) this.requiredItems.add(new Item(queryReturn.getString("itemName"),
					2,queryReturn.getInt("itemId"), queryReturn.getString("itemDescription")));
					
			this.conn.close();
			
		//	System.out.println(this.requiredItems.toString());
		}catch(SQLException e) {
			//e.printStackTrace();
		}
		
		
		
	}
	
	public ArrayList<Item> getRequired(){
		return this.requiredItems;
	}
	
	
	
	public void itemComplete(int itemId, String saveName) {
		
		try {
			
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			String modifyQuery = "UPDATE PlayerPuzzleItem SET isDone = 1"
					+" WHERE PlayerPuzzleItem.saveName = '"+saveName+"'"
					+" and exists (select 'x' from PuzzleItem where PuzzleItem.itemId = "+itemId+" AND 	PlayerPuzzleItem.puzzleItemId = PuzzleItem.puzzleItemId);";

			//System.out.println(modifyQuery);
			
			this.conn.createStatement().execute(modifyQuery);
			
			
			this.conn.close();
			
		}catch(SQLException e) {
			//e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
