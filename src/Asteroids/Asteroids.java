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
CLASS: Asteroids
DESCRIPTION: Extending Game, Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
*/
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

class Asteroids extends Game implements KeyListener{
    //creates a new graphics object and calls it brush
    Graphics brush;
    //creates a new string to hold the state and sets it's value to Menu
    String strState = "Menu";
    //creates a boolean value to hold the value of whether you are allowed to fire or not
    boolean isFireable;
    //creates multiple int variables, one to hold the fire counter, which counts time to see if you can fire, and one is the threshhold for when the timers ays you can fire
    int intFireable;
    int intFireLag;
    ////creates two point arrays to hold the shapes of asteroids and ships. Only the ship point array is used to it's fullet
    Point[] aShape = {new Point(0, 0)};
    Point[] sShape = {new Point(0, 0), new Point(15, 20), new Point(30, 0), new Point(15, 45), new Point(0, 0)};
    //creates a new ship object
    Ship s;
    //creates a ne HUD object
    HUD hud;
    //creates a new integer arraylist to hold the inputs
    ArrayList <Integer> movements = new ArrayList();
    //creates a variable to hold the size of bullets
    int intBSize = 5;
    //creates multiple int variables to hold the min and max velocities for the ship and the rotation increments in degrees
    int intMin = -7, intMax = 7, intRotInc = 2;
    //creates an arraylist to hold asteroids
    ArrayList <Asteroid> asteroids;
    //creates a variable to hold the wave number
    int intWave;
    //creates multiple sound objects to hold different tracks
    Sound background = new Sound("PSMSpace.wav", true);
    Sound shootSound = new Sound("PEW.wav", false);
    Sound deathMusic = new Sound("deathMusic.wav", true);
    
    //creates a new bufferedimage to hold the background of the game
    protected BufferedImage back;
    public Asteroids() {
        //sends a string and the dimensions to the Game parent class
        super("Asteroids!",800,600);
        //sets the bufferedimage to an image in a file location while catching any possible exceptions that may occur when doing so
        try{
            back = ImageIO.read(new File("background.jpg"));
        }catch(Exception e){}
        //initializes the HUD
        hud = new HUD();
        //initializes the ship to the center of the screen facing upwards
        s = new Ship(sShape, new Point(395, 275), 180);
        //initializes the astroids arraylist
        asteroids = new <Integer>ArrayList();
        
        //initializes the wave number to 1
        intWave = 1;
        
        //adds a keylistener to the game
        this.addKeyListener(this);
        
        //initializes multiple variables all required to buffer the firing
        intFireable = 0;
        intFireLag = 10;
        isFireable = false;
        
    }
    
    //is used to paint the game
    public void paint(Graphics brush) {
        //if the game state is Game it will run this code
        if(strState.equals("Game")){
            //plays the track Background
            background.play();
            //if the fire counter exceeds the threshhod, it sets isFireable to  true and rests the fire counter to 0
            if(intFireable > intFireLag){
                isFireable = true;
                intFireable = 0;
            }
            //otherwise it increments the counter
            else{
                intFireable++;
            }
            //draws the background image
            brush.drawImage(back, 0, 0, this);
            
            //runs the move command to go through the inputs and update all values
            Move();
            //updates the position of the ship
            s.updatePos();
            //mirrorc clamps the position fo the ship with the width and hiehgt of the canvas
            s.position.x = mirrorClamp((int)(s.position.x), 800, 0);
            s.position.y = mirrorClamp((int)(s.position.y), 600, 0);
            
            //applys friction to the ship
            s.applyFriction();
            //paints the ship using it's paint method
            s.paint(brush);
            
            //spawnss a wave of asteroids
            spawnWave();
        
            //if the ships bullets list is not empty, it will paint the bullets using the paintBullets method
            if(!s.bullets.isEmpty()){
                paintBullets(brush);
            }
            
            //will run through all asteroids
            for(int i = 0; i < asteroids.size(); i++){
                //creates a new linked list to hold asteroids
                LinkedList <Asteroid> asteroid = new LinkedList();
                //adds the asteroid from asteroids at i to the linked list
                asteroid.add(asteroids.get(i));
                //creates a new linked list to hold asteroids
                LinkedList <Asteroid> subAsteroids = new LinkedList();
                //puts all the asteroids from asteroids into the linked list subAsteroids
                for(int j = 0; j < asteroids.size(); j++)subAsteroids.add(asteroids.get(j));
                //removes the asteroid from asteroidd from the subAsteroids linked list
                subAsteroids.remove(asteroid.get(0));
                //updates the position of the asteroid in asteroid and mirror clamps the position with the width and height of the canvas before painting it to the screen using asteroid.paint
                asteroid.get(0).UpdatePos();
                asteroid.get(0).position.x = mirrorClamp((int)(asteroid.get(0).position.x), 800, 0);
                asteroid.get(0).position.y = mirrorClamp((int)(asteroid.get(0).position.y), 600, 0);
                asteroid.get(0).paint(brush);
                //checks to see if the asteroid collides with a ship, and if so, will remove the asteroid from play and subtract 10 health from the player
                if(s.collision(asteroid.get(0))){
                    hud.intHealth -= 10;
                    asteroids.remove(asteroid.get(0));
                }
                //clamps the health between 0 and 100
                hud.intHealth = (int)(clamp(hud.intHealth, 100, 0));
            }
            //paints the heads up display onto the screen using the HUD.paint
            hud.paint(brush);
            
            //runs the checkDeath() method
            checkDeath();
        }
        //if the game state is Menu it will run this code
        else if(strState.equals("Menu")){
            //fills the background with the colour black
            brush.setColor(Color.black);
            brush.fillRect(0,0,width,height);
            
            //creates a new font for the title, f1, and a new font for the other text, f2. Both are Papyrus because I liked that font
            Font f1 = new Font("Papyrus", Font.PLAIN, 100);
            Font f2 = new Font("Papyrus", Font.PLAIN, 45);
            
            //sets the font to f1 and color to white before drawing the title into the top center of the screen
            brush.setFont(f1);
            brush.setColor(Color.WHITE);
            brush.drawString("Asteroids", 165, 200);
            //sets the font to f2 before drawing the options on the screen
            brush.setFont(f2);
            brush.drawString("Play (p)", 325, 350);
            brush.drawString("Rules (r)", 315, 450);
            //runs the Menu() class to check for user input
            Menu();
        }
        //will run if the game state it Rules
        else if(strState.equals("Rules")){
            //fills the background with the colour black
            brush.setColor(Color.black);
            brush.fillRect(0,0,width,height);
            
            //creates 2 new fonts, one for the title, f1, and the other for the text, f2
            Font f1 = new Font("Papyrus", Font.PLAIN, 100);
            Font f2 = new Font("Papyrus", Font.PLAIN, 30);
            
            //sets font to f1 and color to white before drawing the title to the screen
            brush.setFont(f1);
            brush.setColor(Color.WHITE);
            brush.drawString("Rules", 250, 100);
            //sets the font to f2 before drawing the rules to the screen
            brush.setFont(f2);
            brush.drawString("use W and A to accelerate forwards and backwards", 50, 200);
            brush.drawString("Turn your ship by using A and D", 50, 250);
            brush.drawString("Shoot the asteroids with SPACE", 50, 300);
            brush.drawString("You will earn points when you hit an asteroid", 50, 350);
            brush.drawString("If you get hit by an asteroid, you will lose health", 50, 400);
            brush.drawString("Once your health runs out, you will lose the game", 50, 450);
            brush.drawString("HAVE FUN", 50, 500);
            brush.drawString("Back to Menu (b)", 550, 550);
            //calls the methodRules() to check for input
            Rules();
        }
        //if the game state is death it will run this code
        else if(strState.equals("Death")){
            //stops the playing of the game music before playing the death music
            background.stop();
            deathMusic.play();
            //fills the background with the colour black
            brush.setColor(Color.black);
            brush.fillRect(0,0,width,height);
            
            //creates 2 fonts, one for the titlee and the other for text
            Font f1 = new Font("Papyrus", Font.PLAIN, 100);
            Font f2 = new Font("Papyrus", Font.PLAIN, 30);
            
            //sets the font to f1 and the colour to white before drawing the title of the screen
            brush.setFont(f1);
            brush.setColor(Color.WHITE);
            brush.drawString("You Died", 175, 200);
            //sets the font to f2 before drawing the final score achieved as well as the options available from there
            brush.setFont(f2);
            brush.drawString("Final Score  " + hud.getScore(), 300, 300);
            brush.drawString("Retry (r)", 340, 425);
            brush.drawString("Main Menu (m)", 300, 475);
            brush.drawString("Quit (q)", 340, 525);
            //runs the Death() method to check for user input
            Death();
        }
        //runs Asteroids.paint again
        repaint();
    }
    
    //Is used to check for input doring the game, and deals with the actions 
    public void Move(){
        //if the movements list contains the key W it will run this code
        if(movements.contains(KeyEvent.VK_W)){
            //increases the values of velY using magic math while decreasing the value of velX using magic math. Both velocities belong to the ship
            s.velY += (1 * Math.cos(Math.toRadians(s.rotation)));
            s.velX -= (1 * Math.sin(Math.toRadians(s.rotation)));
            
            //clamps the ships velocities using the set max and min velocities
            s.velX = clamp(s.velX, 10, -10);
            s.velY = clamp(s.velY, 10, -10);
        }
        //if the movements list contains the key S it will run this code
        if(movements.contains(KeyEvent.VK_S)){
            //runs similar code to previous, with opposite operands
            s.velY -= (1 * Math.cos(Math.toRadians(s.rotation)));
            s.velX += (1 * Math.sin(Math.toRadians(s.rotation)));
            
            //clamps the ships velocities using the set max and min velocities
            s.velX = clamp(s.velX, intMax, intMin);
            s.velY = clamp(s.velY, intMax, intMin);
        }
        //if the movements list contains the key A it will run this code
        if(movements.contains(KeyEvent.VK_A)){
            //decreases the rotation of the ship by the increment set prior
            s.rotation -= intRotInc;
        }
        //if the movements list contains the key D it will run this code
        if(movements.contains(KeyEvent.VK_D)){
            //increases the rotation of the ship by the increment set prior
            s.rotation += intRotInc;
        }
        //if the movements list contains the key SPACE it will run this code
        if(movements.contains(KeyEvent.VK_SPACE) && isFireable){
            s.Fire();
            isFireable = false;
            shootSound.play();
        }
        //updates position and rotation of the ship before mirror clamping it
        s.updatePos();
        s.updateRotation();
        s.position.x = mirrorClamp((int)(s.position.x), 800, 0);
        s.position.y = mirrorClamp((int)(s.position.y), 600, 0);
    }
    //is uses to check for input in the Menu state of the game
    public void Menu(){
        //if the movements list contains the KeyCode of P it will run this cod
        if(movements.contains(KeyEvent.VK_P)){
            hud.intHealth = 100;
            hud.setScore(0);
            s.position = new Point(400, 300);
            s.velX = 0;
            s.velY = 0;
            intWave = 1;
            asteroids.clear();
            strState = "Game";
        }
        //if the movements list contains the KeyCode of P it will run this cod
        else if(movements.contains(KeyEvent.VK_R)){
            strState = "Rules";
        }
    }
    public void Death(){
        //if the movements list contains the KeyCode of P it will run this cod
        if(movements.contains(KeyEvent.VK_R)){
            //resets all values used in the game's Game state before setting the state to game
            hud.intHealth = 100;
            hud.setScore(0);
            s.position = new Point(400, 300);
            s.velX = 0;
            s.velY = 0;
            intWave = 1;
            asteroids.clear();
            strState = "Game";
            deathMusic.stop();
        }
        //if the movements list contains the KeyCode of P it will run this cod
        else if(movements.contains(KeyEvent.VK_M)){
            //sets the game state to menu before stopping the death music 
            strState = "Menu";
            deathMusic.stop();
        }
        //if the movements list contains the KeyCode of P it will run this cod
        else if(movements.contains(KeyEvent.VK_Q)){
            //exits the program
            System.exit(0);
        }
    }
    //
    public void Rules(){
        //if the movements list contains the KeyCode of B it will run this cod
        if(movements.contains(KeyEvent.VK_B)){
            //sets game state to Menu
            strState = "Menu";
        }
    }
    
    
    //is used to spawn a whole asteroid in a rondom edge of the screen
    public void spawnAsteroid(){
        //finds a random number between 1 and 4 to see which side of the screen it will spawn on
        int n = (int)(4 * Math.random() + 1);
        //if the number is 1, it will spawn it on the left side of the screen with velocity point to the right
        if(n == 1){
            asteroids.add(new Asteroid(aShape, new Point(0,(int)(400 * Math.random() + 100)), (int)(Math.random() + 359 + 1), false));
        }
        //if the number is 2, it will spawn it on the right side of the screen with velocity point to the left
        else if(n == 2){
            asteroids.add(new Asteroid(aShape, new Point(800,(int)(400 * Math.random() + 100)), (int)(Math.random() + 359 + 1), false));
        }
        //if the number is 3, it will spawn it on the top side of the screen with velocity point to the down
        else if(n == 3){
            asteroids.add(new Asteroid(aShape, new Point((int)(600 * Math.random() + 100),0), (int)(Math.random() + 359 + 1), false));
        }
        //if the number is 4, it will spawn it on the bottom side of the screen with velocity point to the up
        else if(n == 4){
            asteroids.add(new Asteroid(aShape, new Point((int)(600 * Math.random() + 100), 600), (int)(Math.random() + 359 + 1), false));
        }
    }
    //is used to spawn a broken asteroid in the place of a regular asteroid
    public void spawnBrokenAsteroid(Asteroid a){
        //if the asteroid is not broken, it will spawn a new ateroid that is broken in the same location with a random velocity in a different direction
        if(!a.isBroken){
            asteroids.add(new Asteroid(aShape, a.position, (int)(Math.random() + 359 + 1), true));
        }
    }
    //is used to paint the bullets fired by the ship onto the screen and preform logic of the bullets 
    public void paintBullets(Graphics brush){
        
        //applys the destroy clamp in the ship class tothe ships bullets
        s.destroyClamp();
        
        //creates a new array list and stores the ships fired bullets
        ArrayList<Bullet> BOnScreen = s.getBullets();
        
        //creates a new array list and stores the asteroids in play
        ArrayList<Asteroid> subAsteroids = asteroids;
        //runs through all the bullets
        for(int i = 0; i < s.bullets.size(); i++){
            //updates the position of said bullet
            BOnScreen.get(i).updateCPos();
            //draws the bullet to the screen
            brush.drawOval((int)(BOnScreen.get(i).getPosition().x ), (int)(BOnScreen.get(i).getPosition().y + 150 ), intBSize, intBSize);
            
            //runs through all the asteroid in play
            for(int j = 0; j < asteroids.size(); j++){
                
                try{
                    //if the asteroid is hit by a bullet, it will spawn 2 broken asteroids to the screen before removing itself and the bullet it was hit by from the screen
                    if(asteroids.get(j).collision(s.bullets.get(i))){
                        BOnScreen.remove(s.bullets.get(i));
                        spawnBrokenAsteroid(asteroids.get(j));
                        spawnBrokenAsteroid(asteroids.get(j));
                        subAsteroids.remove(asteroids.get(j));
                        hud.setScore(hud.getScore() + 10);
                    }
                }catch(Exception e){}
            }
        }
        //updates the two arraylists with the temporary arraylists
        s.bullets = BOnScreen;
        asteroids = subAsteroids;
    }
    
    public static void main (String[] args) {
        //creeates a new asteroids object to start the game
        new Asteroids();
    }
    
    //is used to check if the object is lying on the edge of anything and returns the opposite value if it is
    public static int mirrorClamp(int cur, int intMax, int intMin){
        //if the current value is greater than the max, it returnns the min
        if(cur > intMax)return intMin;
        //if the current value is less than the minimum, it returns the max
        else if(cur < intMin)return intMax;
        //else it returns the current value
        else return cur;
    }
    //is used to check if the value is lying on the edge of anything and if so returns the clamped value
    public static double clamp(double cur, int intMax, int intMin){
        //if the current value is greater than the max, it returns the max
        if(cur > intMax)return intMax;
        //else if the current value is less than the min, it will reutnr the min
        else if(cur < intMin) return intMin;
        //else it returns the current value
        else return cur;
    }
    
    //is used to spawn a wave of asteroids
    public void spawnWave(){
        //if the number of asteroids on the field is zero it will calculate how many asteroids to spawn in the next wave and spawns thembefore incrementing the wave number
        if(asteroids.size() == 0){
            int spawnNum = intWave * 3;
            for(int i = 0; i < spawnNum; i++){
                spawnAsteroid();
            }
            intWave++;
        }
    }
    
    //is used to check if the player is dead or not and updates the game state based on the result
    public void checkDeath(){
        //if the players  health is zero, it will stop the game music and set the game state to Death
        if(hud.getHealth() == 0){
            background.stop();
            strState = "Death";
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //unused
    public void keyTyped(KeyEvent ke) {
        
    }
    
    //is used to check for user input and store it within the input arraylist
    public void keyPressed(KeyEvent e) {
        //if the input arraylist doesn't contain the current input, it will add the input to the input arraylist
        if(!movements.contains(e.getKeyCode())){
            movements.add(e.getKeyCode());
        }
        //calls the move method
        Move();
    }
    
    //is used to remove keys from the input arraylist once they have been release
    public void keyReleased(KeyEvent e) {
        //removes the object from the input arraylist that corresponds to the key that was released
        movements.remove(movements.indexOf(e.getKeyCode()));
    }
}