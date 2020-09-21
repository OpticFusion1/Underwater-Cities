package sqliteTest;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryDataController {

	private Connection conn;


	public InventoryDataController() {
		
		
		

	}
	
	//returns a arrayList of all items in the users inventory
	public ArrayList<Item> getInventory(String saveName){
		
		try {
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			Statement sqlItemQuery = conn.createStatement();

			String sqlQueryText = "SELECT PlayerItem.itemId, PlayerItem.roomId, Item.itemName, Item.itemDescription FROM PlayerItem JOIN Item "
					+ "ON PlayerItem.itemId = Item.itemId WHERE inInventory = 1 AND PlayerItem.saveName = '"+saveName+"';";

			ResultSet itemReturnQuery=sqlItemQuery.executeQuery(sqlQueryText);

			ArrayList<Item> Inventory = new ArrayList<>();
			
			while(itemReturnQuery.next()) Inventory.add(new Item(itemReturnQuery.getString("itemName"),
					itemReturnQuery.getInt("roomId"),itemReturnQuery.getInt("itemId"), itemReturnQuery.getString("itemDescription")));
			this.conn.close();
			return Inventory;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return  null;
	}
	
	//returns a given items usage text, for when it is used in the correct room
	public String getItemUsageText(int itemId) {
		
		try {
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			Statement queryStatement = this.conn.createStatement();

			String textQuery = "SELECT itemUsageText FROM Item WHERE itemId ="+itemId;

			ResultSet queryReturn = queryStatement.executeQuery(textQuery);

			String returnString = queryReturn.getString("itemUsageText");
			
			this.conn.close();
			
			return returnString;


		}catch(SQLException e){
			//e.printStackTrace();
		}

		return null;



	}


	//returns a arrayList of all items inside a given room
	public ArrayList<Item> getRoomInventory(int roomId, String saveName){


		try {
			
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement queryStatement = this.conn.createStatement();

			String sqlQueryText = "SELECT PlayerItem.itemId, Item.itemName, Item.itemDescription FROM PlayerItem JOIN Item "
					+ "ON PlayerItem.itemId = Item.itemId WHERE PlayerItem.roomId = "+roomId+" AND PlayerItem.inInventory = 0"
					+" AND PlayerItem.saveName = '"+saveName+"';";

			ResultSet queryReturn = queryStatement.executeQuery(sqlQueryText);

			ArrayList<Item> roomInventory = new ArrayList<>();
			
			while(queryReturn.next()) roomInventory.add(new Item(queryReturn.getString("itemName"),
					roomId,queryReturn.getInt("itemId"), queryReturn.getString("itemDescription")));

			this.conn.close();
			
			return roomInventory;
			
			
		}catch(SQLException e){
			System.out.println("you look around the room... \n you don't see anything here...");
		}
		
		return null;

	}
	
	
	
	//returns a specific items failure text


	//adds a item to player inventory, and returns true if success false if failure.
	public boolean pickupItem(int itemId,String saveName){

		try {

			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement updateStatement = this.conn.createStatement();

			String updateText = "UPDATE PlayerItem SET inInventory = 1 WHERE saveName = '"+saveName+"' AND itemId"
					+ " = "+itemId+";";

			System.out.println(updateText);
			updateStatement.execute(updateText);

			this.conn.close();
			
			
			return true;

		}catch(SQLException e) {
			//e.printStackTrace();
		}





		return false;
	}
	//gets the item name and returns a string
	public int itemNameToId(ArrayList<Item> items,String name){
		int neededID =0;
		for(Item item: items){
			if(item.getItemName().equalsIgnoreCase(name)){
				neededID = item.getItemId();
			}
			else{
				neededID = -1;
			}

		}
		return neededID;
	}

	//equips the given item on the given save file.
	//returns true if successful, and false if it failed.
	public boolean equipItem(int itemId, Game currentGame){

		try {

			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			String saveName = currentGame.getSaveName();

			Statement updateStatement = this.conn.createStatement();

			String updateText = "UPDATE SaveFile SET itemId = '"+itemId+"' WHERE saveName = '"+saveName+"'";

			updateStatement.execute(updateText);

			currentGame.setEquippedItem(itemId);

			this.conn.close();
			
			return true;

		}catch(SQLException e) {
			//e.printStackTrace();
		}






		return false;
	}





	public String getItemFailureText(int itemId) {

		try {
			
			this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
			
			Statement queryStatement = this.conn.createStatement();

			String textQuery = "SELECT itemFailureText FROM Item WHERE itemId ="+itemId;

			ResultSet queryReturn = queryStatement.executeQuery(textQuery);

			String returnString = queryReturn.getString("itemFailureText");
			
			this.conn.close();
			
			return returnString;


		}catch(SQLException e){
			//e.printStackTrace();
		}

		return null;

	}







}
