/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 *
 * @author Siam
 */
/*
DESCRIPTION: Is the player
*/
public class Ship extends Polygon{
    
    //creates multiple private double variables to hold various important values
    private double dblFriction = .075;
    private double dblBSpeed;
    //creates a new ArrayList to hold the bullets fired from the ship
    ArrayList<Bullet> bullets;
    
    public Ship(Point[] inShape, Point inPosition, double inRotation) {
        //sends all the inputed variables to the Polygon parent class
        super(inShape, inPosition, inRotation);
        //initializes the arraylist to a new arraylist
        bullets = new ArrayList();
        //initializes the velocities to 0
        velX = 0;
        velY = 0;
        //sets the bullet speed to an arbitrary value
        dblBSpeed = 15;
    }
    //paints the ship to the screen
    public void paint(Graphics brush){
        //getss the points of the ship and puts the values inside of a new point array
        Point[] p = getPoints();
        //runs a similar algorithm to the other objects to seperate the x and components y of the points into 2 seperate integer arrays 
        int[] x = new int[p.length];
        int[] y = new int[p.length];
        for(int i = 0; i < p.length; i++){
            x[i] = (int)(p[i].x);
            y[i] = (int)(p[i].y);
        }
        //sets the colour to white and draws the ship to the screen
        brush.setColor(Color.white);
        brush.drawPolygon(x, y, x.length);
    }
    
    //is used to update the rotation of the ship
    public void updateRotation(){
        //clamps the rotation by checking whether or not the rotation is either above or below the max and min values, 360 and 0, and if they are, it will adjust it to fit within the parameters
        if(this.rotation < 0){
            this.rotation = 360 + this.rotation;
        }
        if(this.rotation > 360){
            this.rotation = this.rotation - 360;
        }
    }
    
    //is used to applay friction to the ship
    public void applyFriction(){
        //checks to see which way the ship is moving and accelerates them in the opposite direction until they hit zero
        if(velX > 0){
           velX -= dblFriction;
           velX = Asteroids.clamp(velX, 5, 0);
        }
        else if(velX < 0){
            velX += dblFriction;
            velX = Asteroids.clamp(velX, 0, -5);
        }
        if(velY > 0){
            velY -= dblFriction;
            velY = Asteroids.clamp(velY, 5, 0);
        }
        else if(velY < 0){
            velY += dblFriction;
            velY = Asteroids.clamp(velY, 0, -5);
        }
    }
    
    //is used to check if a bullet is outside of the visible screen, and if so, will proceed to destroy it immediatly
    public void destroyClamp(){
        //will run through all the bullets inside the bullets arraylist
        for(int i = 0; i < bullets.size(); i++){
            //ifThe lifespan of the bullet is less than or equal to zero, it will detroy it by removing it from the arraylist 
            if(bullets.get(i).intLife <= 0){
                bullets.remove(bullets.get(i));
            }
        }
    }
    //fires a bullet
    public void Fire(){
        //creates a new bullet by giving it the location of the tip of the ship, the rotation of the ship and the speed of bullets fired from this ship
        Bullet b = new Bullet(super.getPoints()[3], rotation, dblBSpeed);
        //adds thebullet to the arraylist
        bullets.add(b);
    }
    //is used to get the list of bullets currently in play
    public ArrayList<Bullet> getBullets(){
        //returns bullets
        return bullets;
    }
    //removes a bullet if it is called
    public void hit(Bullet b){
        //removes the bullet mentioned in the parameters from the list
        bullets.remove(b);
    }
}
