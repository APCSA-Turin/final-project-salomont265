package com.example;
import java.util.Scanner;

public class Board {
    private Piece[][] arr;
    private int p1Score = 0;
    private int p2Score = 0;

    
    public Board(){
        arr = new Piece[10][10];

    }

    public static void main(String[] args) {
        Board b = new Board();
        b.setUp();
        b.printBoard();
        b.play();    
    }
    public void setUp(){
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
                    //setup paws for black
                    arr[i][j] = new Piece(j,i,0,"P",1);
                }
                if( i == 7 &&(j>0) &&(j<9)){
                    //setup pawns for white
                    arr[i][j] = new Piece(j,i,1,"P",1);
                }
              
            }
        }

    }
    public void play(){
        boolean fin = false;
        int count = 0;
        int moves = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to chess, p1 will be white and p2 will be black, please start the game!");
        while(!fin){
            System.out.println("White's score " + p1Score);
            System.out.println("Black's score " + p2Score);

            if(count % 2 == 0){
                System.out.println("White's move");
                
            }else{
                System.out.println("Black's move");

            }
                //movw logic
                String move = scan.nextLine().toUpperCase();
                //parse string for move
                System.out.println(move);
                String col = move.substring(0,1);
                System.out.println(col);
                int row = Integer.parseInt(move.substring(1, 2));
                String col2 = move.substring(2,3);
                int row2 = Integer.parseInt(move.substring(3));
                move(col,row,col2,row2);
            //print board
            printBoard();
        }
    }
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
            System.out.println(col);
            int newCol = Piece.convertToInt(rN)+1;
            System.out.println(9-c);
            Piece current = arr[9-c][col];
            System.out.println("Current piece" + current);
            //if not null add point
            if(arr[9-cN][newCol] != null){
            Piece next = arr[9-cN][newCol];
            
            System.out.println(next.getType());

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
  
