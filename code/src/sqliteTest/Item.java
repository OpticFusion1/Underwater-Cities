package sqliteTest;

public class Item {
	private String itemName;
	private int roomId;
	private int itemId;
	private String itemDescription;
	


	public Item(String itemName, int roomId, int itemId, String itemDescription) {

		this.itemName = itemName;
		this.roomId = roomId;
		this.itemId = itemId;
		this.itemDescription = itemDescription;




	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getItemName() {
		return itemName;
	}

	public int getItemId() {
		return itemId;
	}

	public String getItemDescription() {
		return itemDescription;
	}


}
