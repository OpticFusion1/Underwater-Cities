package sqliteTest;

import java.sql.Statement;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


//This controller MUST be instantiated first within the MainController

public class SaveFileController {

	private Connection conn;
	
	
	
	public SaveFileController() {
		
		establishDatabase();
		
	}
	
	//used for abstracting the database connection for other controllers
	//public Connection getConn() {
	//	return this.conn;
	//}
	
	
	
	
	
	
	
	
	//Checks to see if tables exist, and generates constant values for tables if non existent
	private void establishDatabase() {
	
		
		//checks to see if any tables exist
		try {
			/*
			 * Statement checkStatement = this.conn.createStatement(); boolean result =
			 * checkStatement.
			 * execute("select max(case when table_name = 'Room' then 1 else 0 end) as TableExists\r\n"
			 * + "from information_schema.tables;");
			 */
		
			
			//runs sqlGenerationCode.sql to generate tables, then runs insertion script
		
				
				this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
				
		
		
			
				
			
			
				Scanner fileScan = new Scanner((new File("sqlGenerationCode.txt")));
				
				String sqlGenerationCommand = fileScan.nextLine();
				
				//System.out.println("this is : "+sqlGenerationCommand);
				//System.out.println(fileScan.nextLine());
				
				Statement tableGenerationStatement = this.conn.createStatement();
				
				while(fileScan.hasNextLine()) {
					sqlGenerationCommand=sqlGenerationCommand + fileScan.nextLine();
					
					if(sqlGenerationCommand.contains(";")) {
					
						tableGenerationStatement.execute(sqlGenerationCommand);
						sqlGenerationCommand = "";
					}
				}
				
				//System.out.println("test"+sqlGenerationCommand);
				
				
				//System.out.println(tableGenerationStatement.isClosed());
				
				fileScan = new Scanner((new File("sqlInsertionCode.txt")));
				sqlGenerationCommand = fileScan.nextLine();
				while(fileScan.hasNextLine()) {
					sqlGenerationCommand=sqlGenerationCommand + fileScan.nextLine();
					
					if(sqlGenerationCommand.contains(";")) {
					
						tableGenerationStatement.execute(sqlGenerationCommand);
						sqlGenerationCommand = "";
					}
				}
				
				
				
				

				
		
				tableGenerationStatement.close();
				this.conn.close();
			
			
		
		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Live database found, connection established.");
			
		}
		
	
		
		
	}
	
	//returns all saveNames, of all saves inside of the database
	public ArrayList<String> getAllSaves(){
		
		
		
		try {
			
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement querySaveNamesStatement = this.conn.createStatement();
			ResultSet queryData = querySaveNamesStatement.executeQuery("SELECT `saveName`, playerScore FROM `SaveFile`;");
			
			ArrayList<String> returnQuery = new ArrayList<>();
			while(queryData.next()) returnQuery.add(" Save Name : "+queryData.getString("saveName")+"  |  Score :  "+queryData.getInt("playerScore"));
			
			this.conn.close();
			
			return returnQuery;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return null;
	}
	
	
	public Game  loadGame(String saveName) {
		
		try {
			
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement queryLoadGameStatement = this.conn.createStatement();
			
			String sqlQuery = "SELECT * FROM `SaveFile` WHERE `saveName`='"+saveName+"';";
			ResultSet queryData = queryLoadGameStatement.executeQuery(sqlQuery);
			//System.out.println(queryData.isClosed()+" and "+saveName);
			Game returnQuery = new Game(queryData.getString("saveName"),queryData.getString("playerName"),
					queryData.getInt("playerScore"),queryData.getInt("playerHealth"),queryData.getInt("roomId")
					,queryData.getInt("itemId"));
			
			this.conn.close();
			return returnQuery;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		return null;
	}
	
	
	//Creates a Save from a existing game, returns true if successful, and false if not
	public boolean saveGame(Game currentGame, String saveName){
		
		
		try {
			
			
			setPlayerScore(saveName,currentGame.getPlayerScore());
			setPlayerHealth(saveName,currentGame.getPlayerHealth());
			
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			Statement insertionStatement = this.conn.createStatement();
			
			//Query to attempt to generate from a existing save.
			
			String sqlInsertionQuery = "INSERT INTO SaveFile (saveName,playerName,playerHealth,playerScore,roomId,"
					+ "itemId) VALUES ('"+saveName+"','"+currentGame.getPlayerName()+"',"+
					currentGame.getPlayerHealth()+","+currentGame.getPlayerScore()
					+","+currentGame.getCurrentRoomId()+","+currentGame.getEquippedItem()+");";
			
			//runs query
			insertionStatement.execute(sqlInsertionQuery);
			
			
			//duplicates game metaData
			
			String sqlRoomMetaInsertion = "INSERT INTO `PlayerRoom` (roomId, saveName, roomVisited)"
					+ " SELECT roomId, '"+saveName+"', roomVisited  FROM playerRoom WHERE saveName = '"
					+currentGame.getSaveName()+"';";
			
			insertionStatement.execute(sqlRoomMetaInsertion);
			
			
			String sqlItemMetaInsertion = "INSERT INTO `PlayerItem` (itemId, saveName, inInventory, roomId)"
					+ " SELECT itemId, '"+saveName+"', inInventory, roomId  FROM playerItem WHERE saveName = '"
					+currentGame.getSaveName()+"';";
			
			insertionStatement.execute(sqlItemMetaInsertion);
			
			String sqlPuzzleMetaInsertion = "INSERT INTO `PlayerPuzzle` (saveName, puzzleId, puzzleComplete)"
					+ " SELECT '"+saveName+"', puzzleId, puzzleComplete FROM playerPuzzle WHERE saveName = '"
					+currentGame.getSaveName()+"';";
			
			insertionStatement.execute(sqlPuzzleMetaInsertion);
			
			String sqlMetaMetaInsertion = "INSERT INTO PlayerPuzzleItem (puzzleItemId, saveName, isDone)"
					+"SELECT puzzleItemId, '"+saveName+"', isDone FROM PlayerPuzzleItem WHERE saveName = '"
					+currentGame.getSaveName()+"';";
			
			insertionStatement.execute(sqlMetaMetaInsertion);
			
			
			this.conn.close();
			
			
			return true;
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	//Saves a players game
	
	//(resolved)
	//TODO for some reason this is not actually making a safe file
	// this is neccesary for game functionality priority 1 right here
	// currently suffering from a database is busy error. 
	
	public boolean saveGame(String saveName, String playerName) {
	
		//System.out.println("it ran");
		
		try {
			//create Save Game Entry
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			Statement insertionStatement = this.conn.createStatement();
		
			String sqlInsertionQuery = "INSERT INTO SaveFile (saveName,playerName,playerScore,playerHealth,roomId,"
					+ "itemId) VALUES (\'"+saveName+"\', \'"+playerName+"\', 0, 10, 1,null);";
			//System.out.println(sqlInsertionQuery);
			insertionStatement.execute(sqlInsertionQuery);
			
			
			//create Meta Data
			
			String sqlRoomMetaInsertion = "INSERT INTO `PlayerRoom` (roomId, saveName, roomVisited)"
					+ " SELECT roomId, '"+saveName+"', 0  FROM Room;";
			
			insertionStatement.execute(sqlRoomMetaInsertion);
			
			String sqlItemMetaInsertion = "INSERT INTO `PlayerItem` (itemId, saveName, inInventory, roomId)"
					+ " SELECT itemId, '"+saveName+"', 0, initialRoomId  FROM Item;";
			
			insertionStatement.execute(sqlItemMetaInsertion);
			
			
			String sqlPuzzleMetaInsertion = "INSERT INTO `PlayerPuzzle` (saveName, puzzleId, puzzleComplete)"
					+ " SELECT '"+saveName+"', puzzleId, FALSE FROM Puzzle;";
			
			
			insertionStatement.execute(sqlPuzzleMetaInsertion);
			
			
			String sqlMetaMetaInsetion = "INSERT INTO PlayerPuzzleItem (puzzleItemId, saveName, isDone)"
					+"SELECT puzzleItemId, '"+saveName+"', 0 FROM PuzzleItem;";
			
			insertionStatement.execute(sqlMetaMetaInsetion);
			
			
			insertionStatement.close();
			this.conn.close();
			
			//System.out.println("it finished");
			return true;
			
		}catch(SQLException e) {
			
		}
		
		
		return false;
	}
	
	
	  
	  
	  
	  //Returns the player's score returns -66 in case of error. 
	  public int getPlayerScore(String saveName) {
	  
	  try { 
		
		
		  this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
		  
		  Statement queryStatement = this.conn.createStatement();
	  
		  String textQuery ="SELECT playerScore FROM SaveFile WHERE saveName = '"+saveName+"';";
	  
		  //System.out.println(textQuery);
		  
		  ResultSet queryReturn = queryStatement.executeQuery(textQuery);
	  
		  int Score = queryReturn.getInt("playerScore");
		  
		  
		  this.conn.close();
		  
		  return Score;
	  
	  
	  }catch(SQLException e){ e.printStackTrace(); }
	  
	  return -66;
	  
	  
	  
	  }
	  
	  //sets the player's score, returns true if successful, and false else. public
	  private boolean setPlayerScore(String saveName, int newScore) {
	  
	  try {
	  
		  this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
		  
		  Statement updateStatement = this.conn.createStatement();
	  
		  String updateText = "UPDATE SaveFile SET playerScore = "+newScore+" WHERE saveName = '"+saveName+"'";
	  
		//  System.out.println(updateText);
		  
		  updateStatement.execute(updateText);
	  
		  this.conn.close();
		  
		  return true;
	  
	  }catch(SQLException e) { e.printStackTrace(); }
	  
	  
	  return false; }
	  
	  //returns the player's health, returns -66 if fails 
	  public int getPlayerHealth(String saveName) {
	  
	  try { 
		  
		  this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
		  
		  Statement queryStatement = this.conn.createStatement();
	  
		  String textQuery ="SELECT playerHealth FROM SaveFile WHERE saveName ="+"'"+saveName+"';";
	  
		  ResultSet queryReturn = queryStatement.executeQuery(textQuery);
	  
		  this.conn.close();
		  
		  return queryReturn.getInt("playerHealth");
	  
	  
	  }catch(SQLException e){ e.printStackTrace(); }
	  
	  return -66;
	  
	  }
	  
	  
	  
	  //sets the player's health value to a given value, returns true if successful  else false. 
	  private boolean setPlayerHealth(String saveName, int newHealth) {
	  
	  try {
	  
		  
		  this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
		  
		  Statement updateStatement = this.conn.createStatement();
	  
		  String updateText = "UPDATE SaveFile SET playerHealth = "+newHealth+" WHERE saveName = '"
				  +saveName+"'";
	  
		  updateStatement.execute(updateText);
	  
		  this.conn.close();
		  
		  return true;
	  
	  }catch(SQLException e) { e.printStackTrace(); }
	  
	  
	  return false; }
	  
	  
	 
	
	
	
	
	
	
	
	
	
	
	
	
}
