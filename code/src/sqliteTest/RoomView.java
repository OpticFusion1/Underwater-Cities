package sqliteTest;

import java.util.Scanner;

public class RoomView {
    private Scanner sc;

    //Displays the descriptions of the room and gets the next command.

    
    public RoomView() {
    	
    	sc = new Scanner(System.in);
    	
    	
    }
    
    
    
    
    public int displayRoomDescription(String description, String name, boolean visited) {
        System.out.println("You are in " + name);
        System.out.print(description);
        System.out.println("");
        if(visited) System.out.println("you have visited this place before");
        else System.out.println("It feels like you've never been here before");
        System.out.println("----------------------------");
        System.out.println("What do you want to do now? Choose 1, 2, 3, 4 or 5\n");
        System.out.println("1. Ask for help\n2. Interact\n3. Teleport\n4. Use item\n5. Exit");
        System.out.println("----------------------------");
        try {
            int hold = Integer.parseInt(sc.nextLine());
            return hold;
        }
        catch(NumberFormatException ex) {
            System.out.println("You cant do that. You can only choose between 1 and 5");
            return  -1;
        }

    }
   //Get the direction the player wishes to move in.

    public String moveRoomCommand(String Directions) {
        System.out.println("----------------------------");
        System.out.println("Where do you want to teleport? Choose a direction.");
        
        System.out.println(Directions);
        
        System.out.println("----------------------------");


        return sc.nextLine();

    }

    public void help(String helpText){
        //commands that all rooms have go here

        //commands for a particular room here. Comes from roomdatacontroller.
        System.out.println(helpText);
    }

    public int interact(){
        System.out.println("Do you want to interact with a puzzle or an item?\n");
        System.out.println("1 for puzzle. 2 for item");
        //TODO validate this input
        int returnVal = sc.nextInt();
        sc.nextLine();
        return returnVal;
    }
    public void telefail(){
        System.out.println("The energies of the teleport disperse rapidly. It failed.");
        System.out.println("|\n|\n|\n----------------------------");

    }

}
