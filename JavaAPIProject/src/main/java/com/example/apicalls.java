package com.example;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import java.util.concurrent.atomic.AtomicReference;

import javax.sound.sampled.Line;

    
public class apicalls {
    private static String token = "lip_UKtDlpZrmyJlJfkpAd48";
    private static String baseUrl = "https://lichess.org";
    static AtomicReference<String> lastOpponentMove = new AtomicReference<>(null);


    public static JSONObject sendAIChallenge(String endpoint, String token) throws Exception {
        /*endpoint is a url (string) that you get from an API website*/
        URL url = new URL(endpoint);
        /*connect to the URL*/
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        /*creates a GET request to the API.. Asking the server to retrieve information for our program*/
        connection.setRequestMethod("POST");
         connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String data = "level=5&color=black&clock.limit=300&clock.increment=0";
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

    public  static void getStream(String ID) throws Exception{
        //api/board/game/stream/{gameId}
        URL url = new URL(baseUrl +"/api/board/game/stream/" + ID );
        /*connect to the URL*/
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        /*creates a GET request to the API.. Asking the server to retrieve information for our program*/
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      
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
            if (inputLine.isEmpty()) continue;
            JSONObject info = new JSONObject(inputLine);
            if (info.has("type") && info.getString("type").equals("gameState")){
            String move = info.getString("moves");
            String[] allMoves = move.split(" ");
            String lastMove = allMoves[allMoves.length - 1];
            lastOpponentMove.set(lastMove);
            }
            System.out.println(inputLine);
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

    }
    public void makeCallAi(String move){

    }
    public static void main(String[] args) {
        try{
        JSONObject res = sendAIChallenge("https://lichess.org/api/challenge/ai", token);
        System.err.println(res);
        String id = jsonProcessing.challenge(res);
     
        new Thread(()-> {
            try{
                 getStream(id);
                 System.out.println("W");
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        System.out.println(id);
        
        System.out.println(lastOpponentMove);
        } catch (Exception e){
            e.printStackTrace();
        }
    }   
}



