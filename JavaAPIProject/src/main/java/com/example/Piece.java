package com.example;
public class Piece{
    //basic attirbutes of every chess piece
    private int x;
    private int y;
    private boolean taken;
    private int color;
    private int value;
    private String type;
    /*
     * this construtcs a piece object with all the key paramters in terms of the location,color,type and value
     */
    public Piece(int x,int y, int color,String t,int value){
        this.x = x;
        this.y = y;
        taken = false;
        this.color = color;
        this.type = t;
        this.value = value;

    }
   public int getX(){
    return x;
   }
   
   public String getType(){
    return type;
   }
   public int getColor(){
    return color;
   }
   /*
    * this method converts a letter notation into the arrays corresponding column number
    * param letter String this is the letter to convert
    */
    public static int convertToInt(String letter){
        String[] dic = {"A","B","C","D","E","F","G","H"};
        for(int i =0;i<dic.length;i++){

            if(dic[i].equals(letter)){
                return i;
            }
        }
        return -1;
    }

   



}