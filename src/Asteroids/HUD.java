/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Asteroids;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Siam
 */
/*
DESCRIPTION: The heads up display. Displays the current health and score
*/
public class HUD {
    //creates multiple variables to hold the values required for the heads up display to work
    static int intHealth = 100;
    private int intGreenVal = 100;
    private int intScore;
    int x = 15, y = 15, intHeight = 32, intWidth = 200;
    
    //Sets the health to 100 and sets the score to 0 upon creation of the HUD
    public HUD(){
        intHealth = 100;
        intScore = 0;
    }
    //Paints the object to the screen
    public void paint(Graphics g){
        intGreenVal = intHealth;
        //sets the colour to grey
        g.setColor(Color.GRAY);
        //paints the background of the health bar as a grey rectangle
        g.fillRect(x, y, intWidth, intHeight);
        //sets the color to a custom colour using the green value and a set red value and blue value of 0
        g.setColor(new Color(75, intGreenVal, 0));
        //draws the health bar
        g.fillRect(x, y, intHealth * 2, intHeight);
        //sets the colour to black
        g.setColor(Color.BLACK);
        //draws a border around the health bar
        g.drawRect(x, y, intWidth, intHeight);
        //sets the colour to white
        g.setColor(Color.white);
        //draws the string of the score
        g.drawString("Score: " + intScore, 10, 64);
    }
    //is used to set the current score
    public void setScore(int intScore){
        //sets the current score to the entered value
        this.intScore= intScore;
    }
    //is used to get the score
    public int getScore(){
        //returns the current score
        return intScore;
    }
    //Is used to get the health vale
    public int getHealth(){
        //return the current health
        return(intHealth);
    }
}
