package sqliteTest;

import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class MainController {
    private Scanner sc;
    private Connection con;

    private ItemView itemView;
    private PuzzleView puzzleView;
    private RoomView roomView;
    private WelcomeView welcomeView;
    private InventoryDataController itemInventory;
    private PuzzleDataController puzzleData;
    private RoomDataController roomData;
    private SaveFileController save;
    private Game game;
    private String playerName;
    private int equippedItem;
    private int currentRoomId;
    private String savename;

    private int score;
    private int health;

    private boolean run;
    
    public MainController( String playerName){
    	save = new SaveFileController();
    	sc = new Scanner(System.in);
        health = 3;
        run = true;
    	score = 0 ;
    	welcomeView = new WelcomeView();
    	roomView = new RoomView();
    	puzzleView = new PuzzleView();
    	itemView = new ItemView();
        this.playerName = playerName;
        itemInventory = new InventoryDataController();
        puzzleData = new PuzzleDataController();
        

    }


    public void startGame() {

        boolean go = true;
        roomData = new RoomDataController(this.game.getCurrentRoomId(),puzzleData);
        this.score = (this.save.getPlayerScore(this.game.getSaveName()));
        
        do {
            String name = roomData.getCurrentRoom().getRoomName();
            String description = roomData.getCurrentRoom().getRoomDescription();
            int decision = roomView.displayRoomDescription(description, name,this.roomData.getCurrentVisitFlag(this.game.getSaveName()));
            if (decision != -1) {
            this.currentRoomId =roomData.getCurrentRoom().getRoomId();
            
            switch (decision) {
                case 1:
                    String text = roomData.getCurrentRoom().getHelpText();
                    roomView.help(text);
                    break;
                case 2:
                    int actanswer = roomView.interact();

                        int isPuzzleOrItem = roomData.interact(actanswer, equippedItem, currentRoomId, this.game.getSaveName());


                        if (isPuzzleOrItem == -1) {
                            System.out.println("The puzzle has been completed ");
                        } else if (isPuzzleOrItem == 0) {
                            System.out.println("Not found here...");
                        }
                        //puzzle
                        else if (isPuzzleOrItem == 1 || isPuzzleOrItem == 2) {
                            //gets the current room
                            //int roomNow = roomData.getCurrentRoom().getRoomId();

                            if (isPuzzleOrItem == 1) {

                                boolean inside = puzzleData.getPuzzle(currentRoomId, this.game.getSaveName()); //method for puzzle in room
                                if (inside) {
                                    
                                	
                                	
                                	if (puzzleData.getCurrentPuzzle().getItemId() == this.game.getEquippedItem() && !this.puzzleData.isEquip() //make it work for quip v use
                                    		|| !puzzleData.getCurrentPuzzle().isPuzzleNeedsItem()) {
                                	
                                	
                                	String puzzleD = puzzleData.getCurrentPuzzle().getDescription(); //method for puzzle description
                                    int decisionPuzzle = puzzleView.puzzleText(puzzleD);

                                    switch (decisionPuzzle) {
                                        case 2:
                                            String help = puzzleData.getCurrentPuzzle().getHelpText(); //method to get the help text in the puzzle that was found
                                            puzzleView.puzzleHelp(help);
                                        case 1:
                                            //maybe a loop here to keep getting an answer. Score?
                                           
                                            //method to get the description for the puzzle
                                           
                                               
                                            
                                                
                                            	
                                            	String answerPuzzle = puzzleView.puzzleAnswer(puzzleData.getCurrentPuzzle().getDescription());
                                            	//System.out.println(this.puzzleData.getCurrentPuzzle().getCompletionCommand());
                                            	
                                            	boolean pdResponse = answerPuzzle.contains(this.puzzleData.getCurrentPuzzle().getCompletionCommand()); //method that accepts an answer to the puzzle
                                               
                                            	if (pdResponse) {
                                                
                                            		puzzleView.puzzleSuccess();
                                                 
                                            		puzzleView.completion(puzzleData.getCurrentPuzzle().getCompletionText());
                                                   
                                            		puzzleData.setComplete(puzzleData.getCurrentPuzzle().getPuzzleId(), this.game.getSaveName());
                                           
                                            		this.score += 100;
                                                    
                                            		break;
                                            		
                                            	} else {
                                                     
                                            		puzzleView.puzzleFail();
                                                    if(this.health-1>0) {  
                                            		health--;
                                                    }
                                                    else {
                                                    	System.out.println("looks like you ran out of health, lets give you back one...");
                                                    }
                                                  
                                            	}
                                           //else here maybe not sure I remember what it was for....
                                            
                                            
                                            
                                        case 3:
                                            System.out.println("You black out, the next thing you know you are back in the last room you were in... \n");
                                            health--;
                                            
                                            this.currentRoomId = this.roomData.getPreviousRoom().getRoomId();
                                            this.roomData.forcedMove(this.roomData.getPreviousRoom().getRoomId(), this.game);
                                            
                                            break;
                                    }

                                }else if(this.puzzleData.getCurrentPuzzle().isPuzzleNeedsItem()) {
                                	// for when item is not equipped but needed ( there is never a time when you use, and answer
                                	//this.puzzleData.getPuzzle(this.currentRoomId, this.game.getSaveName());
                                	
                                	ArrayList<Item> requirements = new ArrayList<>();
                                	
                                	requirements = this.puzzleData.getRequired();
                                	
                                	//System.out.println(requirements);
                                	
                                	if(requirements.isEmpty()) {
                                		
                                		this.puzzleView.puzzleSuccess();
                                		this.puzzleView.completion(puzzleData.getCurrentPuzzle().getCompletionText());
                                		this.puzzleData.setComplete(puzzleData.getCurrentPuzzle().getPuzzleId(), this.game.getSaveName());
                                		this.score+=100;
                                		
                                	}else {
                                		
                                		this.puzzleView.displayItemPuzzle(puzzleData.getCurrentPuzzle().getDescription());
                                		
                                		
                                	}
                                	
                                	
                                }else
                                    System.out.println("No Puzzle found here");
                                break;
                                }

                            
                            }
                            //to the roomItems Item names.
                                if (isPuzzleOrItem == 2) {
                                
                            	ArrayList<Item> roomItems = itemInventory.getRoomInventory(currentRoomId,this.game.getSaveName());
                            	
                            	if(!roomItems.isEmpty()) {
                            		//System.out.println("item found, Id : " + roomItems.get(0).getItemId());
                            		//System.out.println("saveName found : "+this.game.getSaveName());
                            		boolean response = itemInventory.pickupItem(roomItems.get(0).getItemId(), this.game.getSaveName()); //placeholder*
                            		
                            		//System.out.println(this.itemInventory.getInventory(this.game.getSaveName()).toString());
                            		
                            		itemView.pickText(response);
                            	}
                            	else itemView.noItem();
                            	break;
                            
                                }
                                
                            
                            
                            }
                        //item

                    case 3:
                        System.out.println("Teleporting...");
                        String location;
                        location = roomView.moveRoomCommand(this.roomData.getDirections());


                        boolean canMove = roomData.move(location, this.game); //
                        if (canMove) {
                            System.out.println("Successfully teleported!");
                            this.currentRoomId = this.game.getCurrentRoomId();
                        }
                        else {
                            roomView.telefail();
                        }
                        break;
                    //should return text if a move can happen. ie: you need to use an item to move forward and you need to finish the puzzle first
                    case 4:
                    	System.out.println(this.game.getSaveName());
                    	//this.puzzleData.getPuzzle(this.currentRoomId, this.game.getSaveName());
                        System.out.println("Opening Inventory");
                        ArrayList<Item> inventory = itemInventory.getInventory(this.game.getSaveName());
                        //if no items are found
                        if (inventory.isEmpty()) {
                            System.out.println("You must have items before you do this.");
                            break;
                        }
                        int answer = itemView.itemOptions(inventory); //asks if the player wants to use an item, equip, or leave
                        if (answer == 1) {
                            String item = itemView.equipItem();
                            Item choosenItem = inventory.get(Integer.parseInt(item.trim())-1);
                            //stores the equippedItem in a global variable to be used by other functions.
                            
                            //true if the item was equipped
                            boolean equip = itemInventory.equipItem(choosenItem.getItemId(), this.game);

                            if (equip) {
                                itemView.equipSuccess();
                                this.game.setEquippedItem(choosenItem.getItemId());
                            }
                            else
                                itemView.equipFailure();
                        } else if (answer == 2) {
                            String message;
                            
                            
                            
                            String anToUse = itemView.Use();
                            
                            Item choosenItemy = inventory.get(Integer.parseInt(anToUse.trim())-1);
                            
                               // System.out.println("cqc test + "+itemInventory.getItemUsageText(choosenItem.getItemId()));
                            	boolean loopCheck = false;
                            	
                            	this.puzzleData.getPuzzle(this.game.getCurrentRoomId(), this.game.getSaveName());
                            	
                            	//System.out.println(this.puzzleData.getRequired().toString());
                            	
                            	for(Item i : this.puzzleData.getRequired()) if(i.getItemId() == choosenItemy.getItemId()) loopCheck = true;
                            	
                            	//System.out.println("curr puz "+this.puzzleData.getCurrentPuzzle().getRoomId()+" this room " + this.game.getCurrentRoomId());
                            	
                            	//System.out.println(loopCheck);
                            	
                            
                            	
                            	//System.out.println(this.puzzleData.getCurrentPuzzle().getItemId());
                                
                            	boolean canUse = this.puzzleData.getCurrentPuzzle().getRoomId() == this.currentRoomId 
                                		&&  loopCheck;
                                
                                if (canUse) {
                                    message = itemInventory.getItemUsageText(choosenItemy.getItemId()); //placeholder** need item id
                                    this.puzzleData.itemComplete(choosenItemy.getItemId(),this.game.getSaveName());
                                }else {
                                    message = itemInventory.getItemFailureText(choosenItemy.getItemId()); //need item id
                                }
                                System.out.println(message + "\n");
                            
                        } else if (answer == 3) {
                            System.out.println("Leaving inventory..");


                        }
                        break;

                    case 5://exit condition.
                        System.out.println("Leaving the game");
                        go = false;
                        exitFromGame();
                        break;
                    default:
                        System.out.println("Your powers seem to peter out\nTry to do something valid.");
                        break;


                }

            }
        }
        while (go);

    }

    //multiple loops for every input
    public void run() {
        this.playerName = welcomeView.playerInfo();

        while (run) {

            if(this.playerName.equals("")){
                System.out.println("Not a valid name");
            }
            else {
                int answer = welcomeView.welcomeText();

                switch (answer) {
                    case 1:
                        System.out.println("Game starting");
                        this.save.saveGame(this.playerName + "_autoSave", this.playerName);
                        this.game = this.save.loadGame(this.playerName + "_autoSave");
                        startGame();
                        break;

                    case 2:
                        System.out.println("Loading game...");


                        String saveName = welcomeView.loadInfo(this.save.getAllSaves());
                        if(saveName.contentEquals("back")){
                            System.out.println("Going back to the Main Menu");
                            break;
                        }

                        this.game = save.loadGame(saveName);
                        startGame();
                        System.exit(0);
                    case 3:
                        System.out.println("Goodbye");
                        run = false;
                    default:
                        System.out.println("Impossible action, try again");

                }


            }
        }
    }

    public void exitFromGame() {
        boolean go;
    do {

        int decide = welcomeView.exit();
        if (decide == 1) {
            System.out.println("Saving game...");
            this.savename = welcomeView.saveInfo();
            game.setCurrentRoomId(currentRoomId);
            game.setEquippedItem(equippedItem);
            game.setPlayerHealth(health);
            game.setPlayerName(playerName);
            game.setPlayerScore(score);
            save.saveGame(this.game, savename);
            System.out.println("Goodbye");
            go = false;
            break;
        }
        else if (decide == 2) {
            go = false;
            System.out.println("Goodbye");
            break;
        }
        else {
            System.out.println("You can't do this, try again.");
            go = true;
        }
    }
        while(go);
}



}
