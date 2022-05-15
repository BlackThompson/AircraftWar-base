package edu.hitsz.application;

import javax.sound.sampled.*;
import java.io.*;

public class LoopMusic extends Thread{
    //音频文件名
    private String filename;
    Clip clip;

    public LoopMusic(String filename) {

        //初始化filename
        this.filename = filename;

    }



    @Override
    public void run() {

        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filename));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);


        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopLoop(){
        clip.close();
    }

    public void loopSleep() throws InterruptedException {
        clip.wait();
    }
}
