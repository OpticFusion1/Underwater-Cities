package sqliteTest;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class WelcomeView {

    private Scanner sc;
    
    public WelcomeView(){
    	
    	this.sc = new Scanner(System.in);
    }
    
    

    //Displays the welcome message and returns the int of the choice to the start controller
    // TODO Create a splashscreen method for the game start
    public int welcomeText(){
    	
        System.out.println("---------------------------------");
        System.out.println("Welcome to Underwater Cities!");
        System.out.println("---------------------------------");
        System.out.println("Choose the number of an option below\n");
        System.out.println("1. New\n2. Load\n3. Exit ");
        System.out.println("---------------------------------");
        try {
            int choice = this.sc.nextInt();
            this.sc.nextLine();
            return choice;
        }
        catch (InputMismatchException ex){
            //if the input is wrong, return a number that can be read by main to break out of the loop.
            this.sc.next();
            this.sc.nextLine();
            return -1;
        }

    }

    public String playerInfo(){
        System.out.println("What is your name?");
        return this.sc.nextLine();

    }

    public String saveInfo(){
        System.out.println("What name do you want to save your information under?");
        return  this.sc.nextLine();

    }

    public String loadInfo(ArrayList<String> saveList){
        System.out.println("Load info");
        System.out.println("Please choose your selected saveFile from the list :");
        System.out.println("To go back, type 'back' ");
        System.out.println("---------------------------------");
        for(String s: saveList) System.out.println(s);
        System.out.println("---------------------------------");

            String testString = this.sc.nextLine();
            return testString;

    }

    public int exit(){
        System.out.println("Do you want to save your game? If yes, 1. If no, 2.");
        int intVal = sc.nextInt();
        this.sc.nextLine();
        return intVal;
    }

}
