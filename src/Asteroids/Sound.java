/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Asteroids;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 *
 * @author Siam
 */
/*
DESCRIPTION: is used to create new sound instances and play and stop sounds in the game
*/
public class Sound {
    //Creates multiple variables to store variouds things later in the code
    private boolean isLooped;
    private AudioInputStream audioIn;
    private Clip track;
    
    
    public Sound(String strFilePath, boolean isLooped){
        //sets the loopability to the entered loopability
        this.isLooped  = isLooped;
        //trys the code inside
        try{
            //gets the AudioInputStream from the file entered and puts it's value inside the audioIn variable
            audioIn = AudioSystem.getAudioInputStream(new File(strFilePath));
            //get's a clip of audio from the input stream and saves it to the track variable
            track = AudioSystem.getClip();
            //opens an output stream from the track using the input stream
            track.open(audioIn);
        }catch(Exception e){}//catches any exception such as a mispelled file name
        //stops the track from playing
        track.stop();
    }
    
    //is used to play the track
    public void play(){
        //will run only if the track isn't currently playing
        if(!track.isActive()){
            //will try to execute the code inside
            try{
                //sets the playback position to 0 microseconds after the start
                track.setMicrosecondPosition(0);
                //starts playing the song
                track.start();
                //if the track is loopable, it will loop the track
                if(isLooped){
                    track.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }catch(Exception e){}//catches any  excpetion such as a sound file with no time duration
        }
        
    }
    //is used to stop the track from playing
    public void stop(){
        //qill only run if the track is currently playing
        if(track.isActive()){
            //stopes the track
            track.stop();
        }
    }
}
