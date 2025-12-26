package src;
import java.awt.*;
import java.applet.*;
import java.util.Random;
public class MorphingLetter extends Letter{
   private int count = 0;
   public MorphingLetter(Graphics g){
      super(g);
   }
   public void drawLetter(){
      if(count==300){ //System.out.println("ok");
         count = 0;
         int letterOrNumber= (int)(Math.random()*2);
         if(letterOrNumber ==0){
            letter = (char)((int)(Math.random()*26)+65);
         }
         else if(letterOrNumber ==1){
            int temp = (int)(Math.random()*10);
            letter = (char) (((int) '0') + temp);
         }
         letterString = String.valueOf(letter);
      }
      count++;
      super.drawLetter();
   }
}