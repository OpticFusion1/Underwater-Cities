package sqliteTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ItemView {
    private int i;
    private Scanner sc;
    int answer;
    
    public ItemView() {
    	
    	sc = new Scanner(System.in);
    	answer = -1;
    	i = 1;
    }
    
    
    //displays the items the player currently has and return the command number of selection to the main controller
    //takes in the array of items a player current has picked up through the interact menu.
    public int itemOptions(ArrayList<Item> items) {
    	answer =-1;
    	i=1;
        System.out.println("---------------------------------");
        System.out.println("-------------INVENTORY-----------");
        System.out.println("---------------------------------");
        System.out.print("\n");
        //iterates through the list and applies a integer as a counter
        for (Item item : items) {
            String number = Integer.toString(i);
            System.out.println(number + " : " + item.getItemName()+"   " +item.getItemDescription());
            i = i + 1;
        }
        System.out.println("\n");
        //this may have to be implemented in the main controller but it helps to have it here to reduce clutter
        while(answer==-1) {
            System.out.println("Choose the number of what you want to do");
            System.out.println("1. Equip Item\n 2. Use Item\n 3. Leave Inventory");
            System.out.println("---------------------------------\n");
            try {
                answer = sc.nextInt();
                sc.nextLine();
                //checks to see if the input matches the options given.
                if(!Arrays.asList(1,2,3).contains(answer)){
                    System.out.println("This is not an option. Try again.");
                    answer =-1;
                }
            } catch (java.util.InputMismatchException ex) {
                System.out.println("Make sure to enter a number. Try again");
                //clears the buffer in case of incorrect input
                sc.next();
            }
        }
        return answer;
    }
    //prints the item use text if item is used in the right room(Controller will determine that)
    public void useItemSuccessOrFailure(String useText){
        System.out.println(useText);
    }

    //equip item text gets the item the player wanted to equip
    public String equipItem(){
        System.out.println("What item do you want to equip?");
        return sc.nextLine();
    }
    //equip success takes in the name compared and displays success
    public void equipSuccess(){
        System.out.println("Equipped");
    }
    //equip failure displays the failure text
    public void equipFailure(){
        System.out.println("The item equip failed");
        //TODO have to figure out how to call these methods in a good loop withing maincontroller
    }

    public void leaveInventory(){
        System.out.println("Leaving the inventory...");
    }
    //used in the main controller. If 1 is returned, main returns the item to pick up. iF 2, EXIT.

    public int pickup(Item itemForPickup){
        System.out.println("Do you want to pick up " + itemForPickup.getItemName()+" ?");
        //System.out.println("It seems like this: " + itemForPickup.getItemDescription()); This needs to be fixed.

        System.out.println("Choose 1 for yes. Choose 2 for no.");
        int held = sc.nextInt();
        sc.nextLine();
        return held;

        //TODO format this text

    }
    //returns the text to pickup or if it fails
    public void pickText(boolean text){
        if(text){
            System.out.println("Pickup success");
        }
        else{
            System.out.println("Pickup failure");
        }



    }

    public String Use(){
        System.out.println("What item do you want to use");
        return sc.nextLine();
    }

    
    public void noItem() {
    	
    	System.out.println("you don't see anything in the room.... \n");
    	
    }
    
    
    
    
    
}
