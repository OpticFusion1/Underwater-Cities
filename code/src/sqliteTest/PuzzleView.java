package sqliteTest;

import java.util.Scanner;
//you only get here when there is a puzzle that exists in the room
public class PuzzleView {
    
	private Scanner sc;
    
	
	public PuzzleView() {
		
		sc = new Scanner(System.in);
		
	}
    
    
    
    
    //gets and read the puzzle description and asks for input. Returns the answer
    public int puzzleText(String description){
        System.out.println(description);
        System.out.println("---------------------------------");
        System.out.println("What do you want to do?");
        System.out.println("1. Answer the question\n 2. Get Help\n 3. Exit");
        System.out.println("---------------------------------");
        int returnVal = sc.nextInt();
        sc.nextLine();
        return returnVal;

    }
    //gets the question from the maincontroller and displays it. Returns the answer to the controller
    public String puzzleAnswer(String puzzleQuestion){
        System.out.println(puzzleQuestion);
        System.out.println("What is your answer?");
        return sc.nextLine();
    }
    //displays the failure text
    public void puzzleHelp(String help){
       System.out.println(help);
    }
    public void puzzleSuccess(){

        System.out.println(" Puzzle complete!");
    }

    public void puzzleFail(){
        System.out.println("Oh no. That's wrong. Sorry, you lost a life.");
    }

    public void completion(String text){
        System.out.println(text);
    }
    //TODO maybe this needs to be more reflective of the game state




	public void displayItemPuzzle(String description) {
		
		System.out.println("----------------------------------- \n \n");
		System.out.println(description + "\n");
		System.out.println("It seems like you need to use something here, maybe come back after.... \n type anything to return... \n");
		
		sc.nextLine();
		
		System.out.println("---------------------------------- \n \n");
		
		
	}
}
