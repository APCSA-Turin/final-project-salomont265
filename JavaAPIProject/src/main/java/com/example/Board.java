package com.example;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.print.DocFlavor.STRING;

public class Board {
    //defines basic attirubtes of the board in both the players scores, the array for the board and the string that showcase the ai move
    private Piece[][] arr;
    private int p1Score = 0;
    private int p2Score = 0;
    private String moveGame;

    
    public Board(){
        arr = new Piece[10][10];

    }

    public static void main(String[] args) {
        //this setups a board object and gives the player choices for what to play
        Board b = new Board();
        System.out.println("welcome to chess, please select an option");
        System.out.println(" Would you like to play a person (enter 1)");
        System.out.println(" Would you like to play an AI (enter 2)");
        System.out.println(" Would you like to read the info from a game (enter 3)");
        System.out.println(" Would you like to end the program(enter 4)");
        //basic loop that runs untill the player stops the program
        boolean loopRun = true;
        while(loopRun){
        Scanner scan = new Scanner(System.in);
        int response = scan.nextInt();
            //runs the basic two players game response
        if(response == 1){
            b.setUp();
            b.playPlayer();
            break;

        }
        //runs the ai game
        else if(response == 2){
            b.setUp();
            b.playAI();
            break;

        }
        //allows the player to read from file of preivous games
        else if(response == 3){
            System.out.println("Please enter the game id");
            scan.nextLine();
            String id =  scan.nextLine();


            try{
            data.readFile(id + ".json");
            }catch(Exception e){
                e.printStackTrace();;
            }
            break;
        }else if(response == 4){
            System.out.println("Thanks for using this program");
            loopRun = false;
        }
        else{
            System.out.println("Please choose another number");
        }
    }
    }

    /*
     * this method setups the basic chess board, putting all the piece objects into the right starting place
     */
    public void setUp(){
        //these loops setup the board correctly based on standard chess notation
        for(int i =0;i<10;i++){
            for(int j= 0;j<10;j++){
                
                if( i==1){
                    //setup first row for black

                    if(j ==1 || j==8){
                        arr[i][j] = new Piece(j, i, 0, "R",5);
                    }
                    if(j==2||j==7){
                        arr[i][j] = new Piece(j, i, 0, "N",3);

                    }
                    if(j==3||j==6){
                        arr[i][j] = new Piece(j, i, 0, "B",3);

                    }
                    if(j==4){
                        arr[i][j] = new Piece(j, i, 0, "Q",9);
                    }
                    if(j==5){
                        arr[i][j] = new Piece(j, i, 0, "K",65);
                    }
                }
                 

                 if( i==8){
                    //setup first row for white

                    if(j ==1 || j==8){
                        arr[i][j] = new Piece(j, i, 1, "R",5);
                    }
                    if(j==2||j==7){
                        arr[i][j] = new Piece(j, i, 1, "N",3);

                    }
                    if(j==3||j==6){
                        arr[i][j] = new Piece(j, i, 1, "B",3);

                    }
                    if(j==4){
                        arr[i][j] = new Piece(j, i, 1, "Q",9);
                    }
                    if(j==5){
                        arr[i][j] = new Piece(j, i, 1, "K",65);
                    }
                }
                if( i==2 && (j>0) &&(j<9) ){
                    //setup pawns for black
                    arr[i][j] = new Piece(j,i,0,"P",1);
                }
                if( i == 7 &&(j>0) &&(j<9)){
                    //setup pawns for white
                    arr[i][j] = new Piece(j,i,1,"P",1);
                }
              
            }
        }

    }
    /*
     * this is the method that runs the first option of the game, the one where the player plays a second player 
     */
    public void playPlayer(){
          boolean fin = false;
        int count = 0;
        int moves = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to chess, p1 will be white and p2 will be black, please start the game!");
        while(!fin){
        
            System.out.println(apicalls.getAIString());
            System.out.println("White's score " + p1Score);
            System.out.println("Black's score " + p2Score);
            
            if(count % 2 == 0){
                System.out.println("White's move");
                
            }else{
                System.out.println("Black's move");

            }
                //movw logic
                String move = scan.nextLine().toUpperCase();
                if(move.equals("resign")){
                    fin = true;
                    System.out.println("game is over");
                }
                //parse string for move
                String col = move.substring(0,1);
                int row = Integer.parseInt(move.substring(1, 2));
                String col2 = move.substring(2,3);
                int row2 = Integer.parseInt(move.substring(3));
                move(col,row,col2,row2);
            //print board
            printBoard();
            count+=1;
        }
    }
    public void playAI(){
        //setup the boolean for the loop and varibale for move count
        boolean fin = false;
        double moveCount = 0.0;
        
        //setup input for the player to choose a color
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to chess, you will be playing the ai today, please input if you want to be white or black!");
        String choice = scan.nextLine().toUpperCase();

        //starts the game with the ai based on user choice and puts it into try incase of error
        try{
        apicalls.makeCallAi(choice);
        }catch (Exception e){
            e.printStackTrace();
        }
       //stars up the strema and seperate thread, but gives fivesecond pause to allow the ai to fully setup and start sending game related info
        try{
        apicalls.getStream(apicalls.getID());
        TimeUnit.SECONDS.sleep(5);
        } catch(Exception e){
            Thread.currentThread().interrupt();
        }
   
        System.out.println(choice);
        //cahneg loop depending on chocie
        if(choice.equals("WHITE")){

            //oplayer is white so they go first, setup the while loop for the game
            while(!fin){
                //check status to ensure the game isnt over, adn if so end it
                if(apicalls.getStatus().equals("mate")){
                    fin = true;
                    System.out.println("game over");
                    break;
                }
                //resets the board to proper window, and also prints the basic board
                 System.out.print("\033[H\033[2J");
                System.out.flush();
                printBoard();
               
                //allows player to move
                System.out.println("whites move");
                String moveP = scan.nextLine();
                String movePlayer =  moveP.toUpperCase();

                //move decision

                //checks for resignation
                if(moveP.equals("resign")){
                    try{
                        //sends resign and ensurre it works
                    apicalls.sendResign(apicalls.getID());
                    fin = true;
                    //keeps track of how many moves made to give to the file data
                    moveGame = apicalls.getMoves();
                    moveCount = (moveGame.length()/10.0);

                    //writes data
                    data.dataLog(apicalls.getID(), moveGame, apicalls.getWT(), apicalls.getBT(),moveCount);

                        
                    System.out.println("game over");
                    break;
                
                 
                    }catch(Exception e){
                        Thread.currentThread().interrupt();
                    }
                }
                //parse string for regular move
               
                
             
                try{
                    //sends the move to the api and gets the code in order to check that the move is legal
                StringBuilder res = apicalls.sendMoveToAI(moveP);
                System.out.println(apicalls.getCode());
             
                }catch (Exception e) {

                    System.out.println(e);
                    System.out.println(apicalls.getCode());
                    e.printStackTrace();
                }
                //cheks for legality of move, if not has user go again
                if(apicalls.getCode() == 200){

                //parses the user's move to allow the board to change the pieces
                String colP = movePlayer.substring(0,1);
                int rowP = Integer.parseInt(movePlayer.substring(1, 2));
                String col2P = movePlayer.substring(2,3);
                int row2P = Integer.parseInt(movePlayer.substring(3));
                //executes move on the array for the board
                move(colP,rowP,col2P,row2P); 
                //prints board again
                printBoard();
                System.out.print("\033[H\033[2J");
                System.out.flush();

                //then  pause to give ai time
                 try{
                TimeUnit.SECONDS.sleep(5);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            //ai move
            
            String moveAI = apicalls.getAIString().toString().toUpperCase();    
           //checks the move isnt null
            if(moveAI.length() == 4 && !(moveAI.equals("NULL"))){
                String col = moveAI.substring(0,1);
            int row = Integer.parseInt( moveAI.substring(1, 2));
            String col2 = moveAI.substring(2,3);
            int row2 = Integer.parseInt(moveAI.substring(3));
            move(col,row,col2,row2);
            System.out.println("AI move info" + apicalls.getAIString());   

           
                }
                
            }
            else{

                    System.out.println("Invalid move please go again");
                }
                   

            }
        }else{
            //if the player chooses to be black instead
        while(!fin){
             //checks if the game is already over
              if(apicalls.getStatus().equals("mate")){
                    try{
                    moveGame = apicalls.getMoves();
                        fin = true;
                        System.out.println(moveGame);
                    System.out.println("game over");
                    break;
                    }catch(Exception e){
                        Thread.currentThread().interrupt();
                    }
                }
            
              
            try{
                //five second pause to give the ai time to setup
                TimeUnit.SECONDS.sleep(5);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            //ai move comes first
            
            String moveAI = apicalls.getAIString().toString().toUpperCase();    
            System.out.println("AI move info" + apicalls.getAIString());   
            //parses ai move
            if(moveAI.length() == 4 && !(moveAI.equals("NULL"))){
                String col = moveAI.substring(0,1);
            int row = Integer.parseInt( moveAI.substring(1, 2));
            String col2 = moveAI.substring(2,3);
            int row2 = Integer.parseInt(moveAI.substring(3));
            //exectues the move and updates board
            move(col,row,col2,row2);
           System.out.print("\033[H\033[2J");
                System.out.flush();
                printBoard();
            }
                  

        
                //movw logic

                //get move from while loop
                


                String moveP = scan.nextLine();
                String movePlayer =  moveP.toUpperCase();
               if(moveP.equals("resign")){
                    try{
                    apicalls.sendResign(apicalls.getID());
                    fin = true;
                    moveGame = apicalls.getMoves();
                     moveCount = (moveGame.length()/10.0);
                    
                    //setup write
                    data.dataLog(apicalls.getID(), moveGame, apicalls.getWT(), apicalls.getBT(),moveCount);

                    
                    System.out.println("game over");
                    break;
                
                 
                    }catch(Exception e){
                        Thread.currentThread().interrupt();
                    }
                }
                   try{

                StringBuilder res = apicalls.sendMoveToAI(moveP);
                }catch (Exception e) {

                    System.out.println(e);
                    System.out.println(apicalls.getCode());

                    Thread.currentThread().interrupt();
                }
                if(apicalls.getCode() == 200){

                   String colP = movePlayer.substring(0,1);
                int rowP = Integer.parseInt(movePlayer.substring(1, 2));
                String col2P = movePlayer.substring(2,3);
                int row2P = Integer.parseInt(movePlayer.substring(3));
                move(colP,rowP,col2P,row2P); 
                printBoard();
                System.out.print("\033[H\033[2J");
                System.out.flush();
                //then get ai move
                System.out.println("stauts" + apicalls.getStatus());
                 
        }else{
            System.out.println("Invalid move");
        }
             
                
                    System.out.print("\033[H\033[2J");
    System.out.flush();
                printBoard();

            //print board
        }
    }
    }
    /*
     * This method prints the board array based on the location of each piece
     */
    public void printBoard(){
        for(int i =0;i<10;i++){
            for(int j=0;j<10;j++){
                if(arr[i][j] != null){
                int color = arr[i][j].getColor();
                String type = arr[i][j].getType();
                if( color == 1 ){
                
                    if(type.equals("P")){
                        System.out.print("[ ♟ ]");
                    }
                     if(type.equals("R")){
                        System.out.print("[ ♜ ]");
                    } if(type.equals("K")){
                        System.out.print("[ ♚ ]");
                    }
                     if(type.equals("B")){
                        System.out.print("[ ♝ ]");
                    } if(type.equals("N")){
                        System.out.print("[ ♞ ]");
                    }
                     if(type.equals("Q")){
                        System.out.print("[ ♛ ]");
                    }
                }else{
                      if(type.equals("P")){
                        System.out.print("[ ♙ ]");
                    }
                     if(type.equals("R")){
                        System.out.print("[ ♖ ]");
                    } if(type.equals("K")){
                        System.out.print("[ ♔ ]");
                    }
                     if(type.equals("B")){
                        System.out.print("[ ♗ ]");
                    } if(type.equals("N")){
                        System.out.print("[ ♘ ]");
                    }
                     if(type.equals("Q")){
                        System.out.print("[ ♕ ]");
                    }
                }
            }else{
                if(i==0 || i == 9){
                    if(j == 1){
                        System.out.print(" [ A ]");
                    }
                    if(j == 2){
                        System.out.print("[ B ]");
                    }
                    if(j == 3){
                        System.out.print("[ C ]");
                    }
                    if(j == 4){
                        System.out.print("[ D ]");
                    }
                    if(j == 5){
                        System.out.print("[ E ]");
                    }
                    if(j == 6){
                        System.out.print("[ F ]");
                    }
                    if(j == 7){
                        System.out.print("[ G ]");
                    }
                    if(j == 8){
                        System.out.print("[ H ]");
                    }
                }
                else if(j == 0 || j == 9){
                    if( i == 1){
                        System.out.print(9-i);
                    }
                    if( i == 2){
                        System.out.print(9-i);
                    } if( i == 3){
                        System.out.print(9-i);
                    } if( i == 4){
                        System.out.print(9-i);
                    } if( i == 5){
                        System.out.print(9-i);
                    } if( i == 6){
                        System.out.print(9-i);
                    } if( i == 7){
                        System.out.print(9-i);
                    }
                     if( i == 8){
                        System.out.print(9-i);
                    }
                }else{
                System.out.print("[   ]");
                }
            }
          }
                System.out.println();
        }
    }
    public void move(String r, int c, String rN, int cN){
            //get current piece
            int col = Piece.convertToInt(r) + 1;
            int newCol = Piece.convertToInt(rN)+1;
            Piece current = arr[9-c][col];
            //if not null add point
            if(arr[9-cN][newCol] != null){
            Piece next = arr[9-cN][newCol];
            

            //getpiec color
            //if black add point to white
            if(next.getColor() == 0){
                //pawm
            if(next.getType().equals("P")){
                p1Score++;
            }

            if(next.getType().equals("N")){
                p1Score +=3;

            }

            if(next.getType().equals("B")){
                p1Score +=3;
                
            }

            if(next.getType().equals("R")){
                p1Score +=5;

            }
            if(next.getType().equals("Q")){
                p1Score +=9;
                
            }

            }else{
            if(next.getType().equals("P")){
                p2Score++;
            }

            if(next.getType().equals("N")){
                p2Score +=3;

            }

            if(next.getType().equals("B")){
                p2Score +=3;
                
            }

            if(next.getType().equals("R")){
                p2Score +=5;

            }
            if(next.getType().equals("Q")){
                p2Score +=9;
                
            }
            }   
        }
            //check if piece


            arr[9-cN][newCol] = current;
            arr[9-c][col] = null;
           
        
    }
    public Piece[][] getBoard(){
        return arr;
    }

     
}
  
