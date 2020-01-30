/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Asteroids;

/**
 *
 * @author 202432
 */
/*
CLASS: Polygon
DESCRIPTION: A polygon is a sequence of points in space defined by a set of
             such points, an offset, and a rotation. The offset is the
             distance between the origin and the center of the shape.
             The rotation is measured in degrees, 0-360.
USAGE: You are intended to instantiate this class with a set of points that
       forever defines its shape, and then modify it by repositioning and
       rotating that shape. In defining the shape, the relative positions
       of the points you provide are used, in other words: {(0,1),(1,1),(1,0)}
       is the same shape as {(9,10),(10,10),(10,9)}.
NOTE: You don't need to worry about the "magic math" details.
*/

class Polygon extends Shape{
  protected Point[] shape;   // An array of points.
  Point position;   // The offset mentioned above.
  public double rotation;  // Zero degrees is due east.
  double velX; 
  double velY;
  
  public Polygon(Point[] inShape, Point inPosition, double inRotation) {
    shape = inShape;
    position = inPosition;
    rotation = inRotation;
    
    // First, we find the shape's top-most left-most boundary, its origin.
    /*Point origin = shape[0];
    
    // Then, we orient all of its points relative to the real origin.
    for (Point p : shape) {
      p.x -= origin.x;
      p.y -= origin.y;
    }*/
  }
  public boolean collision(Shape p2){
      Point[] p2Points = p2.getPoints();
      boolean isColliding = false;
      for( int i = 0; i < p2Points.length; i++){
          Point temp = p2Points[i];
          if(this.contains(temp)){
              isColliding = true;
          }
      }
      return isColliding;
  }
  
  // "getPoints" applies the rotation and offset to the shape of the polygon.
  public Point[] getPoints() {
    Point center = findCenter();
    Point[] points = new Point[shape.length];
    for(int i = 0; i< shape.length; i++){
        Point p = new Point(shape[i].x,shape[i].y);
        double x = ((p.x-center.x) * Math.cos(Math.toRadians(rotation))) - ((p.y-center.y) * Math.sin(Math.toRadians(rotation))) + center.x/2 + position.x;
        double y = ((p.x-center.x) * Math.sin(Math.toRadians(rotation))) + ((p.y-center.y) * Math.cos(Math.toRadians(rotation))) + center.y/2 + position.y;
        points[i] = new Point(x,y);
    }
    return points;
  }
  
  // "contains" implements some magical math (i.e. the ray-casting algorithm).
  public boolean contains(Point point) {
    Point[] points = getPoints();
    double crossingNumber = 0;
    for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
      if ((((points[i].x < point.x) && (point.x <= points[j].x)) ||
           ((points[j].x < point.x) && (point.x <= points[i].x))) &&
          (point.y > points[i].y + (points[j].y-points[i].y)/
           (points[j].x - points[i].x) * (point.x - points[i].x))) {
        crossingNumber++;
      }
    }
    return crossingNumber%2 == 1;
  }
  
  public void rotate(int degrees) {rotation = (rotation+degrees)%360;}
  
  public void updatePos(){
        position.x += velX;
        position.y += velY;
  }
  
  /*
  The following methods are private access restricted because, as this access
  level always implies, they are intended for use only as helpers of the
  methods in this class that are not private. They can't be used anywhere else.
  */
  
  // "findArea" implements some more magic math.
  private double findArea() {
    double sum = 0;
    for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
      sum += shape[i].x*shape[j].y-shape[j].x*shape[i].y;
    }
    return Math.abs(sum/2);
  }
  
  // "findCenter" implements another bit of math.
  private Point findCenter() {
    Point sum = new Point(0,0);
    for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
      sum.x += (shape[i].x + shape[j].x)
               * (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
      sum.y += (shape[i].y + shape[j].y)
               * (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
    }
    double area = findArea();
    return new Point(Math.abs(sum.x/(6*area)),Math.abs(sum.y/(6*area)));
  }
}
