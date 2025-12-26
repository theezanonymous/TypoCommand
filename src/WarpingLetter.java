package src;
import java.awt.*;
import java.applet.*;
import java.util.Random;
public class WarpingLetter extends Letter{
   private int count = 0;
   public WarpingLetter(Graphics g){
      super(g);
   }
   public void drawLetter(){
      if(count==100){
         count = 0;
         x = (int)(Math.random()*650);
      }
      count++;
      super.drawLetter();
   }
}