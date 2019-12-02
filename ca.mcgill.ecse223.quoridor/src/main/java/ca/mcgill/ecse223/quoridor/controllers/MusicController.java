package ca.mcgill.ecse223.quoridor.controllers;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("Duplicates")
public class MusicController {
    private static Clip clip;
    private static Clip chooseYourChar;
    public static void playMainMenu(){
        try {
            File musicPath = new File("./src/main/resources/music/MainMenu.wav");
            if(clip != null) {
                clip.stop();
                clip.close();
            }
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
    public static void playEpicMusic(){
        try{
            File musicPath = new File("./src/main/resources/music/BattleTheme.wav");
            if(clip != null) {
                clip.stop();
                clip.close();
            }
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{
                System.out.println("Can't find file");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void playBossBattle(){
        try{
            File musicPath = new File("./src/main/resources/music/megalovania.wav");
            if(clip != null)
                clip.stop();
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{
                System.out.println("Can't find file");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void playChooseYourChar(){
        try{
            File musicPath = new File(".\\src\\main\\resources\\music\\ChooseYourChar.wav");
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                chooseYourChar = AudioSystem.getClip();
                chooseYourChar.open(audioInput);
                chooseYourChar.start();
            }
            else{
                System.out.println("Can't find file");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public static void stopAllMusic(){
        if(clip != null){
            clip.stop();
            clip.close();
        }
        if(chooseYourChar != null){
            chooseYourChar.stop();
            chooseYourChar.close();
        }
    }
}
