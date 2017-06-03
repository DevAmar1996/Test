/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author QamarNa
 */
public class Users implements Comparable<Users>{

    private String name;
    private int score;

    public Users(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Users o) {
        if(this.score > o.score ){
         return -1;
         }
        else if(this.score < o.score){
             return 1;
         }
        return 0;
    }

 
}
