package com.example;
public class Piece{
    //basic attirbutes of every chess piece
    private int x;
    private int y;
    private boolean taken;
    private int color;
    private int value;
    private String type;

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
   public String getXLetter(){
        return convertToStr(x);
   }
   public String getType(){
    return type;
   }
   public int getColor(){
    return color;
   }
    public static int convertToInt(String letter){
        String[] dic = {"A","B","C","D","E","F","G","H"};
        for(int i =0;i<dic.length;i++){

            if(dic[i].equals(letter)){
                return i;
            }
        }
        return -1;
    }

     public static String convertToStr(int x){
        String[] dic = {"A","B","C","D","E","F","G","H"};

        for(int i =0;i<dic.length;i++){
            System.out.println(dic[i]);
            if(i == x){
                return dic[i];
            }
        }
        return null;
    }



}