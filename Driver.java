import javax.swing.*;

import java.awt.*;

public class Driver extends JFrame {
   private static boolean running = true;   //game is running
   public static final int TARGET_FPS = 60;   //the targeted frames per second
   public static final int WIDTH = 700, HEIGHT = 700;   //width and height of window
   private static int cx, cy;    //center of screen, not window
   public static Screen screen = new Screen();   //screen that extends JPanel
   
   public Driver() {
      super("Triangulation Minesweeper");   //title of window
      
      //get the width and height of the display monitor
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      double width = screenSize.getWidth();
      double height = screenSize.getHeight();
      
      //setup the window
      setSize(WIDTH, HEIGHT);   //set width and height of window
      
      cx = (int)(width / 2);
      cy = (int)(height / 2);
      
      setLocation(cx - WIDTH / 2, cy - HEIGHT / 2);   //set the location of the window on the display monitor
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);   //when window closes, exit the app
      
      screen.setLayout(new BorderLayout());
      screen.add(new GameScreen(), BorderLayout.CENTER);
      screen.add(new SideScreen(), BorderLayout.EAST);
      
      setContentPane(screen);   //set the screen of the window to the screen we made
      //setUndecorated(true);   //delete borders
      setVisible(true);   //set the window visible
      setResizable(false);   //set the window unresizable
      screen.requestFocus();   //"click" on the window
      
      gameLoop();
      
      JOptionPane.showMessageDialog(new JOptionPane(),
                                    "On my honor as a member of the Woodson HS Community,\n"+
                                    "I, Nripesh Manandhar, certify that I have neither given \n"+
                                    "nor received unauthorized aid on this assignment, \n"+
                                    "that I have cited my sources for authorized aid, and \n"+
                                    "that this project was started on or after April 18, 2018.",
                                    "Honor Code",
                                    JOptionPane.INFORMATION_MESSAGE);
      
      dispose();
   }
   
   //process inputs, update and render game and calculate FPS
   private void gameLoop() {
      final long TARGET_FRAMETIME = 1000000000 / TARGET_FPS;
      int frame = 0;   //resets every second to zero
      long secondTime = System.nanoTime();
      while(running) {
         if(!isVisible())
            running = false;
         
         frame++;
         long startTime = System.nanoTime();
         
         //process inputs, update and render
         screen.update();
         screen.render();
         
         long endTime = System.nanoTime();
         try {
            Thread.sleep((TARGET_FRAMETIME - (endTime - startTime)) / 1000000);
         }
         catch(Exception e) {
            
         }
         long now = System.nanoTime();
         //update FPS every second by setting the FPS to frame before resetting it
         if(secondTime + 1000000000 < now) {
            screen.setFps(frame);
            frame = 0;
            secondTime = now;
         }
      }
      
   }
   
   public static void main(String[] args) {
      new Driver();
   }
   
   //setter for boolean running
   public static void setRunning(boolean running) {
      Driver.running = running;
   }
   
   //getter for integer cx
   public static int getCX() {
      return cx;
   }
   
   //getter for integer cy
   public static int getCY() {
      return cy;
   }
}