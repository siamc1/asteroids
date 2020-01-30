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
DESCRIPTION: Is the enemy object, is used to attack the player with
*/
public class Asteroid extends Polygon{
    //creates a boolean value to hold the value of whether it is a broken asteroid or not
    boolean isBroken;
    /*
    DESCRIPTION: An asteroid is the enemies in the game that you must shoot and take damage if they hit you
                 extends polygon
    */
    public Asteroid(Point[] inShape, Point inPosition, double inRotation, boolean isB) {
        super(inShape, inPosition, inRotation);
        //sets the vale of isBroken to the entered value
        isBroken = isB;
        //creates the shape of the asteroid as an array of points
        Point[] aShape = {new Point(0, 0), new Point(18, 2), new Point(22, 4), new Point(28, 2), new Point(28, 8), new Point(25, 9), new Point(26, 11), new Point(25, 15), new Point(23, 13), new Point(24, 17), new Point(16, 20), new Point(13, 19), new Point(14, 15), new Point(12, 13), new Point(10, 15), new Point(12, 17),  new Point(6, 20), new Point(3, 17), new Point(6, 12), new Point(6, 9), new Point(3, 5), new Point(0, 0)};
        //if the asteroid isn't broken, it will multiply all it's points to enlarge the asteroid
        if(!isBroken){
            for(int i = 0; i < aShape.length; i++){
                aShape[i].x *= 2;
                aShape[i].y *= 2;
            }
        }
        //sets the shape of the asteroid as the shape created earlier, makes it easier to spawn asteroids repeatedly
        this.shape = aShape;
        //if the asteroid is not broken, it will set  it's velocity to go randomly towards the center.
        if(!isBroken){
            if(position.x < 100){
                velX = 4 * Math.random() + 1;
            }
            else{
                velX = -4 * Math.random() - 1;
            }
            if(position.y < 100){
                velY = 4 * Math.random() + 1;
            }
            else{
                velY = -4 * Math.random() - 1;
            }
        }
        //if the asteroid is broken, it will give it a random velocity with about half the magnitude as it's bigger counterparts, using a velocity checker to make sure it doesn't give it a velocity of 0
        else{
            velX = Math.random() * 4 - 2;
            if(velX == 0){
                velX++;
            }
            velY = Math.random() * 4 - 2;
            if(velY == 0){
                velY++;
            }
        }
    }
    //This method paint's the asteroid to the screen
    public void paint(Graphics brush){
        //gets the points of the asteroid  and stores it within a Point array
        Point[] p = getPoints();
        //creates 2 new int arrays and sets their lengths to the length of the point array
        int[] x = new int[p.length];
        int[] y = new int[p.length];
        //seperates the x and y components of the points in the array and put's the values inside the respective array
        for(int i = 0; i < p.length; i++){
            x[i] = (int)(p[i].x);
            y[i] = (int)(p[i].y);
        }
        //sets the colour of the brush to white
        brush.setColor(Color.white);
        //draws the polygon of the asteroid to the screen
        brush.drawPolygon(x, y, x.length);
    }
    //is used to update the position of the asteroid
    public void UpdatePos(){
        //adds the velocities to their respective positions
        position.x += velX;
        position.y += velY;
    }
}
