import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Screen extends JPanel {
   private static long frame = 0;   //current frame that does NOT reset every second
   private static int fps = 60;
   
   public Screen() {
      super();
      setBackground(Color.BLACK);
   }
   
   //updates, add to frame count
   public void update() {
      frame++;
      ((GameScreen)(getComponent(0))).update();
      ((SideScreen)(getComponent(1))).update();
   }
   
   //drawing
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
   }
   
   //rendering graphics
   public void render() {
      repaint();
      ((GameScreen)(getComponent(0))).render();
      ((SideScreen)(getComponent(1))).render();
   }
   
   //setter for fps
   public void setFps(int newFps) {
      fps = newFps;
   }
   
   //getter for frame
   public static long getFrame() {
      return frame;
   }
   
   //setter for frame
   public static void setFrame(long f) {
      frame = f;
   }
}