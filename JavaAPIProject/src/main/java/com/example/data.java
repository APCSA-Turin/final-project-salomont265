package com.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
/*
 * This method logs all the game data from any game
 * @param gameID String this is the game id that represents the game in which this info was recorded
 * @param moves String this is a string that shows the list of moves in the game
 * @param timeW long this is a long to store larger numbers that show case the time left for white in milliseconds
 * @param timeB long this is a long to store larger numbers that show case the time left for black in milliseconds
 * @param moveCount double this is a count of how many moves made
 */
public class data {
    public static void  dataLog(String gameID,String moves,long timeW, long timeB, double moveCount){
        //statistic calcuations
        
        double avgTimePMoveW = moveCount / ((300000 - timeW) /1000);
        timeW = (timeW / 1000 );
        timeB = (timeB / 1000) ;

        double avgTimePMoveB = moveCount / ((300000 - timeB) /1000);

        //this puts the info into a new json object
        JSONObject data = new JSONObject();
        data.put("gameID", gameID);
        data.put("moves", moves);
        data.put("WhiteTime",timeW);
        data.put("BlackTime",timeB);
        data.put("avgTimePW",avgTimePMoveW );
        data.put("avgTimePB",avgTimePMoveB );
        data.put("moveCount",moveCount);


        //write to file
         try {
      FileWriter myWriter = new FileWriter(gameID +".json");
      myWriter.write(data.toString(1));
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    }
    /*
     * this method reads a given file
     * @param path String this is the path to the file
     */
    public static void readFile(String path)throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(path));
        //this creates a stringbuilder object first to read the info form th file
        StringBuilder info = new StringBuilder();
        try{
            String line = "";
            while((line =reader.readLine()) !=null){
                info.append(line);
               
            }
        }catch(Exception e){
                e.printStackTrace();
            }
        //turn into json
        JSONObject json = new JSONObject(info.toString());

        //get input
        Scanner scan = new Scanner(System.in);

        System.out.println("To read the info please input what you'd like to see. Each name will have a key, please enter the key");
        System.out.println("Moves:'moves' , GameId:'gameID',Time spent by white:'WhiteTime',Time spent by black: 'BlackTime',Avg move time for white:'avgTimePW', Avg move time for black:'avgTimePB',movecount:'moveCount', end to stop ");

        String input = scan.nextLine();
        while(!input.equals("end")){
            System.out.println(json.get(input));
            System.out.println("What else would you like to see?");
            input = scan.nextLine();
        }
        }


        
    
}
