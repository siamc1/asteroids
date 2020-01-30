/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Asteroids;

import java.awt.Graphics;

/**
 *
 * @author Siam
 */
/*
DESCRIPTION: is a parent class for the Bullet class, implementing multiple variables and classes
*/
public class Circle extends Shape{
    //creates a new Point variable to hold the position
    Point CPos;
    //creates a new int variable to hold the rotation 
    int intR;
    public Circle(Point p){
        //sets the positionto the entered 
        CPos = p;
    }
    //checks to see if the bullet contains any polygons and returns the outcome
    public boolean contains(Polygon s){
        //if the bullet includes a polygon, it will return true, else false
        if(s.position.x > CPos.x - intR && s.position.x < CPos.x + intR && s.position.y > CPos.y - intR && s.position.y < CPos.y + intR) return true;
        else return false;
    }
    //returns a set of 8 points aroind the circumfrence of the circle, NOT USED
    public Point[] getPoints(){
        //creates a new point array with length 8 to hold the points around the perimeter
        Point[] p = new Point[8];
        //finds the first four points at 90 degree increments from the start position and sets their values to the first four positions in the array
        p[0] = (new Point(CPos.x, CPos.y + intR));
        p[1] = (new Point(CPos.x, CPos.y - intR));
        p[2] = (new Point(CPos.x + intR, CPos.y));
        p[3] = (new Point(CPos.x - intR, CPos.y));
        //finds the last four points  using some magic math and sets their values to the last four positions in the array
        p[4] = (new Point(intR * Math.cos(Math.PI / 2) + CPos.x, intR * Math.sin(Math.PI / 2) + CPos.y));
        p[5] = (new Point(intR * Math.cos(3 * Math.PI / 2) + CPos.x, intR * Math.sin(Math.PI / 2) + CPos.y));
        p[6] = (new Point(intR * Math.cos(3 * Math.PI / 2) + CPos.x, intR * Math.sin(-Math.PI / 2) + CPos.y));
        p[7] = (new Point(intR * Math.cos(Math.PI / 2) + CPos.x, intR * Math.sin(-Math.PI / 2) + CPos.y));
        //returns the array
        return p;
    }
}
