package com.example;
//import needed info
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import java.util.concurrent.atomic.AtomicReference;

import javax.sound.sampled.Line;

    
public class apicalls {
    //all the key info i need to call to the api
    private static String token = "lip_XVSgAjF1VSNx2cpssyyd";
    private static String baseUrl = "https://lichess.org";
    private static String  gameId = "";
    //some variables i use when writing to file the data
    private static long whiteT = 0;
    private static long blackT = 0;
    private static int resCode = 0;
    private static String status = "";
    private static String moves = "";
    static AtomicReference<String> lastAIMove = new AtomicReference<>(null);
        
    /*
    This method sends the inital challnege to the api, which only confirms the game 
     @param1 endpoint to send to
     @param2 the secutiy api toke
     @param3 the color of the player to tell the ai what side to play
     */
    public static JSONObject sendAIChallenge(String endpoint, String token,String pColor) throws Exception {
        /*endpoint is a url (string) that you get from an API website*/
        URL url = new URL(endpoint);
        /*connect to the URL*/
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // this creates a post request with an authorization token
        connection.setRequestMethod("POST");
         connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //the data, mainly the color of the player has to be added here and the time is set to a standard 5 min
String data = "level=5&color=" + pColor.toLowerCase() + "&clock.limit=300&clock.increment=0";
//this gets the output info and setsup up code to read it in
        connection.getOutputStream().write(data.getBytes());
        InputStream errorStream = connection.getErrorStream();
        /* When you read data from the server, it wil be in bytes, the InputStreamReader will convert it to text. 
        The BufferedReader wraps the text in a buffer so we can read it line by line*/
        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;//variable to store text, line by line
        /*A string builder is similar to a string object but faster for larger strings, 
        you can concatenate to it and build a larger string. Loop through the buffer 
        (read line by line). Add it to the stringbuilder */
        StringBuilder content = new StringBuilder();
        while ((inputLine = buff.readLine()) != null) {
            content.append(inputLine);
        }
        String stringError = "";

        if(errorStream != null){
            StringBuilder error = new StringBuilder();
            BufferedReader errorRead = new BufferedReader(new InputStreamReader(errorStream));
        while ((stringError = errorRead.readLine()) != null) {
                error.append(stringError);
        }
        errorRead.close();
                System.out.println(stringError);

    }
        buff.close(); //close the bufferreader
        connection.disconnect(); //disconnect from server 

        return new JSONObject(content.toString()); //return the content as a string
    }
        public static String getMoves(){
            return moves;
        }
   /*
    * This method is to specfically resign from the game
    @param1 id this is the game id to resign from
    */
    public static void sendResign(String id) throws Exception{
        //sets up the specfic url we need to send to, and sets up the connections
        URL url = new URL(baseUrl +"/api/board/game/" + id + "/resign");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

         // creates a post request to send the resign info
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        resCode = connection.getResponseCode();
        InputStream errorStream = connection.getErrorStream();
        /* When you read data from the server, it wil be in bytes, the InputStreamReader will convert it to text. 
        The BufferedReader wraps the text in a buffer so we can read it line by line*/
        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;//variable to store text, line by line
        /*A string builder is similar to a string object but faster for larger strings, 
        you can concatenate to it and build a larger string. Loop through the buffer 
        (read line by line). Add it to the stringbuilder */
        StringBuilder content = new StringBuilder();
        while ((inputLine = buff.readLine()) != null) {
            content.append(inputLine);
            //this is to check the status code so that it can be updated to check for errors in the main game class
                    resCode = connection.getResponseCode();

           
        }
        String stringError = "";

        if(errorStream != null){
                    resCode = connection.getResponseCode();

            StringBuilder error = new StringBuilder();
            BufferedReader errorRead = new BufferedReader(new InputStreamReader(errorStream));
        while ((stringError = errorRead.readLine()) != null) {
                error.append(stringError);
        }
        errorRead.close();
                System.out.println(stringError);
                System.out.println(error);

    }
        buff.close(); //close the bufferreader
        connection.disconnect(); //disconnect from server 
        System.out.println(content.toString()); //return the content as a string
  
        
    

    }

    /* 
     * this method is a method run in a thread, which fetches the information throughout the runtime of the program, its a thread as the method will not finsih untill the program is closed, so it needs to be run in parallel to be able to retrieve the moves from the ai and send the posts of the player moves
     @param1 id this is the game id to get the stream from
     */
    public  static void getStream(String ID) throws Exception{
        //this is run in a thread because it would otherwise take up the entire main thread and not allow player to send information, its run sepreatly so that the main code can proceed whiel still fetchign the key info
        new Thread(()-> {
            try{
        //the url to connect to the game strmea
        URL url = new URL(baseUrl +"/api/board/game/stream/" + ID );
        /*connect to the URL*/
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        /*creates a GET request to the API.. Asking the server to retrieve information for our program*/
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        resCode = connection.getResponseCode();
        InputStream errorStream = connection.getErrorStream();
        /* When you read data from the server, it wil be in bytes, the InputStreamReader will convert it to text. 
        The BufferedReader wraps the text in a buffer so we can read it line by line*/
        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;//variable to store text, line by line
        /*A string builder is similar to a string object but faster for larger strings, 
        you can concatenate to it and build a larger string. Loop through the buffer 
        (read line by line). Add it to the stringbuilder */
        StringBuilder content = new StringBuilder();
        while ((inputLine = buff.readLine()) != null) {
            //this gets the response code which is vital ot checking if a move is valid when sent by the player
            resCode = connection.getResponseCode();
            //this line of code is because the stream is setup sometimes before the game can fully begin due to the time the api takes, so to avoid issues it waits untill there is info to send
            if (inputLine.isEmpty()) continue;
            JSONObject info = new JSONObject(inputLine);
            //extra provision to ensure the game has started, and there is game specifci info to be fetched
            if (info.has("type") && info.getString("type").equals("gameState")){
            //this gets the status of the game, an extra to check could remove
            status = info.getString("status");
            //this is very IMPORTANT, as this line of code updates the list of moves. Since this method doesnt stop running, it cannot return the moves but rather updates a static variable with all the moves to allow the board to get the mvoes in real time

            //this is some basci rpocessing of the move list sent by the stream
            moves = info.getString("moves");
            String move = info.getString("moves");
            String[] allMoves = move.split(" ");
            String lastMove = allMoves[allMoves.length - 1];
            //this cals the setter method to update the last move
            lastAIMove.set(lastMove);

            //this is to keep updating the time left according to the stream
            if(info.has("wtime")){
            whiteT = info.getLong("wtime");
            }
            if(info.has("btime")){
            blackT = info.getLong("btime");
            }
          
            System.out.println(inputLine);

            }
        }
        String stringError = "";

        if(errorStream != null){
            StringBuilder error = new StringBuilder();
            BufferedReader errorRead = new BufferedReader(new InputStreamReader(errorStream));
            while ((stringError = errorRead.readLine()) != null) {
                            resCode = connection.getResponseCode();

                error.append(stringError);
                System.out.println(error);
            }
            errorRead.close();
            System.out.println(stringError);
    }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        

    }
    //returns the id from the game
    public static String getID(){
        return gameId;
    }
    //returns the current time left for white
    public static long getWT(){
        return whiteT;
    }
        //returns the current time left for black
    public static long getBT(){
        return blackT;
    }
    //returns the status code
    public static int getCode(){
        return resCode;
    }
    //returns the last move made, allows the board acsess to the last move made by the ai
    public static AtomicReference<String> getAIString(){
        return lastAIMove;
    }
    //returns the status code
    public static String getStatus(){
        return status;
    }

    /*
     * This method does the inital setup process to start a game with the ai
     @param1 color this is a string that represent what color the player is playing as
     */
    public static void makeCallAi(String color)throws Exception{
        try{               
            //sets up the jsonobject to repesent the repsonse from the gaem setup
        JSONObject res = sendAIChallenge("https://lichess.org/api/challenge/ai", token,color);
        System.out.println(res);

        //updates the gameid with the current game
        gameId = res.getString("fullId");
     
      
    }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    /*
     * this method sends the player's move the ai and updates the status code to ensure the mvoe was legal
     @param1 move this is a string of the player's move written in standard chess notation
    */
    
    public static StringBuilder sendMoveToAI(String move) throws Exception {
        /*endpoint is a url (string) that you get from an API website*/

        URL url = new URL(baseUrl + "/api/board/game/" + gameId + "/move/" + move);
        /*connect to the URL*/
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        /*creates a GET request to the API.. Asking the server to retrieve information for our program*/
        connection.setRequestMethod("POST");
         connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        //get code to check if move is calid
        resCode = connection.getResponseCode();
       
        InputStream errorStream = connection.getErrorStream();
        /* When you read data from the server, it wil be in bytes, the InputStreamReader will convert it to text. 
        The BufferedReader wraps the text in a buffer so we can read it line by line*/
        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;//variable to store text, line by line
        /*A string builder is similar to a string object but faster for larger strings, 
        you can concatenate to it and build a larger string. Loop through the buffer 
        (read line by line). Add it to the stringbuilder */
        StringBuilder content = new StringBuilder();
        while ((inputLine = buff.readLine()) != null) {
            content.append(inputLine);
                    resCode = connection.getResponseCode();

        }
        String stringError = "";

        if(errorStream != null){
            //updates code
                    resCode = connection.getResponseCode();

            StringBuilder error = new StringBuilder();
            BufferedReader errorRead = new BufferedReader(new InputStreamReader(errorStream));
        while ((stringError = errorRead.readLine()) != null) {
                error.append(stringError);
        }
        errorRead.close();
                System.out.println(stringError);
                return error;

    }
        buff.close(); //close the bufferreader
        connection.disconnect(); //disconnect from server 
        System.out.println(content.toString()); //return the content as a string
        return content;
        
    
    }
     
}



