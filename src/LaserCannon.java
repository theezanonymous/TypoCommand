package src;
// LaserCannon.java -- Stages 6D - 7

// Stage 6D returns the contents of the <drawLaser> method is now returned to this class,
// which makes this file essentially identical to Stages 6A and 6B.

// This file will remain unchanged until students do the Typocommand lab assignment.
// In that assignment students can improve the appearance of the laser cannon
// which right now is nothing more than a triangle.


import java.awt.*;
import java.applet.*;


public class LaserCannon extends GameThing
{
   private int angle = 0;
	public LaserCannon(Graphics g)  { super(g); }
   private int rad = 5;
   private int frameCount = 0; private boolean rise = true;
	public void drawLaserCannon()
	{  angle= (angle+1)%360;
		/*g.setColor(Grfx.lightCyan);
		int start  = 16 * letterSize + centerOffset;
		int finish = 18 * letterSize + centerOffset - 1;
		Grfx.fillTriangle(g,laserCanTipX,laserCanTipY,
		                  start,appletHeight,finish,appletHeight);*/
            g.setColor(new Color(169,169,169));
      g.fillOval(laserCanTipX-8, laserCanTipY-3, 16, 16);
      g.setColor(new Color(255,36,0));
      g.fillOval(laserCanTipX - 5, laserCanTipY, 10, 10);
      g.setColor(Color.yellow);
      g.fillOval(laserCanTipX-2, laserCanTipY+2, 5, 5);
      g.setColor(new Color(27,27,27));
      g.fillRect(laserCanTipX-10, laserCanTipY+10, 20, appletHeight - laserCanTipY-10);
      g.setColor(Color.red);
      for(int i = 0; i < 20; i++){
         g.setColor(new Color(255, 200-10*i, 200-10*i));
         g.drawOval(laserCanTipX-12-i, laserCanTipY + 10+ 4*i, 24 + i*2, 5);
      }
      /*g.drawOval(laserCanTipX-12, laserCanTipY+10, 24, 5);
      g.drawOval(laserCanTipX-13, laserCanTipY+14, 26, 5);
      g.drawOval(laserCanTipX-14, laserCanTipY+18, 28, 5);
      g.drawOval(laserCanTipX-15, laserCanTipY+22, 30, 5);
      g.drawOval(laserCanTipX-16, laserCanTipY+26, 32, 5);
      g.drawOval(laserCanTipX-17, laserCanTipY+30, 34, 5);
      g.drawOval(laserCanTipX-18, laserCanTipY+34, 36, 5);
      g.drawOval(laserCanTipX-19, laserCanTipY+38, 38, 5);
      g.drawOval(laserCanTipX-20, laserCanTipY+42, 40, 5);*/
      //System.out.println(angle);
      //int rad = (int)(Math.random()*15)+8;
      if(frameCount==10){
         if(rise == true){
            rad = rad+1;
            }
         else{
            rad = rad-1;
         }
         if(rad>30){
            rise = false;
         }
         if(rad <=1){
            rise = true;
         }
      frameCount = 0;
      }
      frameCount++;
      int x1 = laserCanTipX+ (int)(rad*Math.cos((double)angle/180*Math.PI)); 
      int y1 = laserCanTipY+ (int)(rad*Math.sin((double)angle/180*Math.PI));
      int x2= laserCanTipX+ (int)(rad*Math.cos((double)(angle+180)/180*Math.PI)); 
      int y2 = laserCanTipY+ (int)(rad*Math.sin((double)(angle+180)/180*Math.PI));
      g.setColor(new Color(0, 255, 255));
      g.drawLine(x1, y1, x2, y2); 
      //rad = (int)(Math.random()*15)+8;
      x1 = laserCanTipX+ (int)((30-rad)*Math.cos((double)(angle+90)/180*Math.PI)); 
      y1 = laserCanTipY+ (int)((30-rad)*Math.sin((double)(angle+90)/180*Math.PI));
      x2= laserCanTipX+ (int)((30-rad)*Math.cos((double)(angle+270)/180*Math.PI)); 
      y2 = laserCanTipY+ (int)((30-rad)*Math.sin((double)(angle+270)/180*Math.PI));
      g.drawLine(x1,y1,x2,y2);


	}

	public void drawLaser(Letter letter)
	{
		Grfx.setColor(g,Grfx.random(1,9));
		g.drawLine(laserCanTipX, laserCanTipY,
		           letter.getCenterX(), letter.getCenterY());
	}

	// Since I have decided that the LaserCannon will be indestructible
	// both methods are intentionally redefined with empty method bodies.
	public void triggerExplosion()
	{
	}

	public void explode()
	{
	}

}


