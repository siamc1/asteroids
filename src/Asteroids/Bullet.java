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
DESCRIPTION: is used to shoot at the asteroids, is the thing that destroys asteroids
*/
public class Bullet extends Circle{
    //creates multiple variables to hold the dblSpeed, the rotation and the intLife
    double dblSpeed;
    double dblTurn;
    int intLife;
    public Bullet(Point p,double q, double s) {
        super(p);
        //corrects theposition of the bullet to have it fire from the tip of the ship
        CPos.y -= 150;
        //sets the radius of the bullet to 5
        intR = 5;
        //sets the dblTurn to the entered value
        dblTurn = q;
        //sets the dblSpeed to the entered value
        dblSpeed = s;
        //sets the intLife to 50
        intLife = 50;
    }
    //get's the points of the bullet
    public Point getPosition(){
        //redblTurns the position of the bullet
        return CPos;
    }
    //is used to update the position of the bullet
    public void updateCPos(){
        //updates the position by adding the corresponding velocity value to the right position vale
        CPos.x += -(dblSpeed * Math.cos(Math.toRadians(dblTurn - 90)));
        CPos.y += -(dblSpeed * Math.sin(Math.toRadians(dblTurn - 90)));
        //subtracts from the intLife
        intLife--;
    }
    
}
