package src;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Random;
import java.util.*;
public class TypoCommand extends JFrame implements KeyListener, FocusListener {
    private int numLetters = 10;     // This value should be between 1 and 30.
    private int numCities = 8;       // This value needs to be 8.
    private int appletWidth;         // width of the window
    private int appletHeight;        // height of the window
    private Letter[] letters;        // array of all falling letters
    private City[] cities;           // array of 8 cities
    private LaserCannon laserCan;    // the 1 laser cannon
    private int score = 0;
    private Graphics g, gBuffer;     // used for virtual memory/double buffering
    private Image virtualMem;        // to eliminate flicker in the animation
   private boolean gameOver = false;
   private int numDestroyed = 0;
    private char keyFired;           // the last key pressed by the user
    private boolean focus;           // equals true when window is in focus
    private ArrayList<ArrayList<Integer>> stars = new ArrayList<>();
    private int limit = 0;
    private int missed = 0;
    public TypoCommand() {
        setTitle("TypoCommand");
        setSize(1050, 650);          // Set the initial size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        appletWidth = getWidth();
        appletHeight = getHeight();

        virtualMem = createImage(appletWidth, appletHeight);
        gBuffer = virtualMem.getGraphics(); 
        letters = new Letter[numLetters];         // instantiate array
        for (int j = 0; j < numLetters; j++)
            letters[j] = getNewLetter(j, j, gBuffer);  // instantiate each array element

        cities = new City[numCities];             // instantiate Cities array
        for (int j = 0; j < numCities; j++)
            cities[j] = new City(gBuffer, j);     // instantiate each individual City object

        laserCan = new LaserCannon(gBuffer);      // instantiate Laser Cannon

        keyFired = ' ';
        addKeyListener(this);
        addFocusListener(this);
        focus = false;
        for(int i = 0; i < 100; i++){
         ArrayList<Integer> temp = new ArrayList<>(); 
         temp.add((int)(Math.random()*appletWidth));
         temp.add((int)(Math.random()*appletHeight));
         stars.add(temp);
        }
    }

    @Override
    public void paint(Graphics g) {
        if(gameOver){//gBuffer.setColor(Color.red); gBuffer.fillRect(0,0,500,400);
         gameOverScreen(g); 
         //repaint();         
        }
        else{
        this.g = g;  // allows all methods to access g without passing it as a parameter

        if (!focus)
            titleScreen();  // The title screen is shown until the user clicks in the window.
        else {
            Grfx.setBackground(gBuffer, Grfx.black);
            drawLetters();
            drawCities();
            drawStars(); //System.out.println("ok");
            drawScore();
            laserCan.drawLaserCannon();
            limit = Math.min(6,Math.max(limit, score/500));
            //System.out.println(limit);
        }

        g.drawImage(virtualMem, 0, 0, this);
        repaint();  // makes the program repeat
        
        }
    }
   public void gameOverScreen(Graphics gBuffer){
        Grfx.setBackground(gBuffer, Grfx.darkRed);
        gBuffer.setColor(Grfx.darkBlue); //System.out.println("ok");
        Grfx.drawThickRectangle(gBuffer, 0, 0, appletWidth, appletHeight, 48);
        Grfx.drawThickRoundedRectangle(gBuffer, 0, 0, appletWidth, appletHeight, 48, 48);

        Font title = new Font("Algerian", Font.ITALIC, 72);
        Font directions = new Font("Times New Roman", Font.BOLD, 36);
        Font footNote = new Font("Times New Roman", Font.PLAIN, 24);

        gBuffer.setFont(title);
        gBuffer.setColor(Grfx.white);
        gBuffer.drawString("GAME OVER.", 150, 150);
        gBuffer.setFont(directions);
        gBuffer.drawString("Final Score: " + score, 320, 235);

        //gBuffer.drawString("Click inside the window to begin/resume the game.", 255, 470);
   }
    public void titleScreen() {
        // This screen is shown anytime the window is not in "focus".
        Grfx.setBackground(gBuffer, Grfx.darkRed);
        gBuffer.setColor(Grfx.darkBlue);
        Grfx.drawThickRectangle(gBuffer, 0, 0, appletWidth, appletHeight, 48);
        Grfx.drawThickRoundedRectangle(gBuffer, 0, 0, appletWidth, appletHeight, 48, 48);

        Font title = new Font("Algerian", Font.ITALIC, 72);
        Font directions = new Font("Times New Roman", Font.BOLD, 36);
        Font footNote = new Font("Times New Roman", Font.PLAIN, 24);

        gBuffer.setFont(title);
        gBuffer.setColor(Grfx.white);
        gBuffer.drawString("TYPOCOMMAND 2012", 150, 150);
        gBuffer.setFont(directions);
        gBuffer.drawString("By: John L. M. Schram", 320, 235);

        gBuffer.drawString("Instructions:", 400, 400);
        gBuffer.setFont(footNote);
        gBuffer.drawString("Based on Atari's \"Missile Command\" and \"Typo Attack\"", 245, 305);
        gBuffer.drawString("Click inside the window to begin/resume the game.", 255, 470);
    }

    public void drawLetters() {
        for (int j = 0; j < numLetters; j++) {
            letters[j].fall(); // move every letter down a bit
            int temp = Math.max(1, 7 - (score/500)); 
            letters[j].setSpeed(temp); //System.out.println(temp);
            if (!letters[j].isExploding() && letters[j].getLetter() == keyFired && letters[j].onScreen())
                letters[j].triggerExplosion(); 

            if (letters[j].isBeingTrackedAndShot())
                laserCan.drawLaser(letters[j]);

            if (letters[j].hitBottom()) {
                for (int c = 0; c < numCities; c++)
                    if (!letters[j].isExploding() && cities[c].isAlive() && cities[c].hit(letters[j].getColumn()))  // Did a letter hit a city when it hit bottom?
                        cities[c].triggerExplosion();

                letters[j] = getNewLetter(j, numLetters, gBuffer);
                // A new letter is created when the old letter hits bottom.
            }

            if (!letters[j].isAlive()) {
               temp = letters[j].y;
                score+= (int)((double)(650-temp)/650*25);
                letters[j] = getNewLetter(j, numLetters, gBuffer);
                // A new letter is created when the old letter is shot with a laser.
            }
            
        }
    }

    public Letter getNewLetter(int insertIndex, int currentArraySize, Graphics gBuffer) {
        Letter temp = getSpecialLetter();

        // This while loop prevents letters from being on top of each other by not
        // allowing a new letter to be placed in the same column as any existing letter.
        boolean duplicateColumn = true;
        while (duplicateColumn) {
            duplicateColumn = false;
            for (int k = 0; k < currentArraySize; k++)
                if (k != insertIndex && temp.getColumn() == letters[k].getColumn())
                    duplicateColumn = true;
            if (duplicateColumn)
                temp = getSpecialLetter(); //System.out.println("okkk");
        }
        return temp;
    }
   
    private Letter getSpecialLetter() {
        Letter temp; 
        switch (Grfx.random(1,limit)) {
            case 1:
                temp = new FastLetter(gBuffer);
                break;
            case 2:
                temp = new VanishingLetter(gBuffer);
                break;
            case 3:
                temp = new ShrinkingLetter(gBuffer);
                break;
            case 4:
                temp = new WarpingLetter(gBuffer);
                break;
            case 5:
                temp = new MorphingLetter(gBuffer);
                break;
            default:
                temp = new Letter(gBuffer);
        }
        return temp;
    }

    public void drawCities() {
        numDestroyed = 0;
        for (int x = 0; x < numCities; x++){
            cities[x].drawCity(); 
            numDestroyed+= cities[x].getAlive() == true? 0: 1;} //System.out.println(numDestroyed); System.out.println(numCities);
        if(numDestroyed == numCities){
            gameOver = true;
        }
        
    }
    public void drawStars(){
    gBuffer.setColor(Color.white);
       for(int i= 0; i < stars.size(); i++){ 
         int x = stars.get(i).get(0);
         int y = stars.get(i).get(1);
         stars.get(i).set(0, (x+1)%appletWidth);         
         gBuffer.fillRect(x, y, 2, 2);
      }
    }
    public void drawScore(){
      gBuffer.setColor(Color.white);
      gBuffer.drawString("Score: " + String.valueOf(score),25,100);
    }
    @Override
    public void focusGained(FocusEvent e) {
        focus = true;
    }

    @Override
    public void focusLost(FocusEvent e) {
        focus = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // The retrieved character is converted to a String, so it can be "up-cased",
        // and then is converted back into a character.
        keyFired = String.valueOf(e.getKeyChar()).toUpperCase().charAt(0);
        //System.out.println(String.valueOf(e.getKeyChar()).toUpperCase());
        if (keyFired == '0')  // Prevents confusion of letter O and number 0.
            keyFired = 'O';
        boolean isALetter = false;
        for(Letter l: letters){
         if(l.getLetter()==keyFired){
            isALetter = true;
         }
        }
        if(isALetter == false){
         missed++; 
         score = Math.max(score-20, 0);
            if(missed == 20){
               score = 0; missed = 0;
            }
        }
    }

    // These methods are not used, but they are needed because this class "implements" KeyListener.
    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { 
    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
         limit = 6; System.out.println("Backspace Pressed- All Letters Activated");
        }} // used for scan codes

    public static void main(String[] args) {
        new TypoCommand();
    }
}
