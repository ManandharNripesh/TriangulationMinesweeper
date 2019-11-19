import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen extends Screen implements MouseMotionListener, MouseListener {
   
   private static int numProbes = 0;
   
   private static boolean gameOver = false;
   
   public static HashTable mines;
   
   private int numMines = 20;
   
   private Tree probes = new Tree();
   
   protected static Tree flags = new Tree();
   
   private static int screenWidth, screenHeight;
   
   private static List<Runnable> inputs = new ArrayList<Runnable>();
   
   //Strings for naming actions
   private final String EXIT = "exit";
   
   public GameScreen() {
      setBackground(Color.BLACK);
      setKeyBindings();
      addMouseListener(this);
      addMouseMotionListener(this);
   }
   
   public void restart(int nm) {
      numMines = nm;
      restart();
   }
   
   private void restart() {
      gameOver = false;
      probes = new Tree();
      mines = new HashTable();
      flags = new Tree();
      Screen.setFrame(0);
   }
   
   //run the actions that the user inputs
   public synchronized void processInputs() {
      for(int i = 0; i < inputs.size(); i++) {
         inputs.get(i).run();
      }
      inputs.clear();
   }
   
   //updates
   public void update() {
      //intial setup
      if(Screen.getFrame() == 1) {
         screenWidth = getWidth();
         screenHeight = getHeight();
         mines = new HashTable();
         for(int i = 0; i < numMines; i++) {
            boolean done = false;
            while(!done) {
               done = mines.add(new Mine((int)(Math.random() * (getWidth() - 20) + 10), (int)(Math.random() * (getHeight() - 20) + 10)));
            }
         }
      }
      numProbes = probes.size();
   }
   
   //drawing
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      //draw backgrounds
      Iterator iterator = probes.iterator();
      while(iterator.hasNext()) {
         Probe temp = (Probe)(iterator.next());
         if(temp != null) {
            temp.drawBackground(g);
         }
      }
      
      //draw probes
      iterator = probes.iterator();
      while(iterator.hasNext()) {
         ((Probe)(iterator.next())).draw(g);
      }
      
      //draw mines
      if(mines != null) {
         for(int i = 0; i < mines.length(); i++) {
            iterator = mines.getIterator(i);
         
            while(iterator.hasNext()) {
               try {
                  ((Mine)(iterator.next())).draw(g);
               }
               catch(Exception e) {
                  
               }
            }
         }
      }
      
      //draw flags
      iterator = flags.iterator();
      while(iterator.hasNext()) {
         ((Flag)(iterator.next())).draw(g);
      }
      
      if(isGameOver())
         if(gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("DIALOG", Font.BOLD, 50));
            g.drawString("GAME OVER", 100, 300);
            g.setFont(new Font("DIALOG", Font.BOLD, 25));
            g.drawString("CLICK TO RESTART", 125, 350);
         }
         else {
            g.setColor(Color.GREEN);
            g.setFont(new Font("DIALOG", Font.BOLD, 50));
            g.drawString("YOU WIN!", 100, 300);
            g.setFont(new Font("DIALOG", Font.BOLD, 25));
            g.drawString("CLICK TO RESTART", 125, 350);
         }
   }
   
   //rendering graphics
   public void render() {
      repaint();
   }
   
   //getter for screen width
   public static boolean isGameOver() {
      return gameOver || SideScreen.getNumMines() == 0;
   }
   
   //getter for screen width
   public static int getScreenWidth() {
      return screenWidth;
   }
   
   //getter for screen height
   public static int getScreenHeight() {
      return screenHeight;
   }
   
   //getter for number of probes used
   public static int getNumProbes() {
      return numProbes;
   }
   
   //add user input to list of actions to be performed
   public static void addInput(Runnable input) {
      inputs.add(input);
   }
   
   //********** KEY BINDINGS **********
   //for user inputs
   private void gimp(String key, String name) {
      getInputMap().put(KeyStroke.getKeyStroke(key), name);
   }
   
   //for user inputs
   private void gamp(String name, AbstractAction action) {
      getActionMap().put(name, action);
   }
   
   //set keys to actions
   private void setKeyBindings() {
      //////////////////////set keys to names/////////////////////////////
      gimp("ESCAPE", EXIT);
      
      ////////////////////////set names to actions//////////////////////////////////
      gamp(EXIT, 
         new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               System.exit(1);
            }
         }
         );
   }
   //********** END KEY BINDINGS **********
   
   
   //********** MOUSE **********
   @Override
   public void mouseClicked(MouseEvent e) {
      
   }

   @Override
   public void mousePressed(MouseEvent e) {
      
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if(isGameOver()) {
         if(e.getButton() == MouseEvent.BUTTON1) {
            restart();
         }
      }
      else {
         try {
            if(e.getButton() == MouseEvent.BUTTON1) {
               Tree tempMines = mines.get(HashTable.hashCode(e.getX(), e.getY()));
               if(tempMines != null && tempMines.contains(new Mine(e.getX(), e.getY()))) {
               //game over
                  gameOver = true;
                  for(int i = 0; i < mines.length(); i++) {
                     Iterator iterator = mines.getIterator(i);
                     while(iterator.hasNext()) {
                        ((Mine)(iterator.next())).reveal();
                     }
                  }
                  return;
               }
               if(probes.contains(new Probe(e.getX(), e.getY()))) {
                  Probe probe = (Probe)(probes.get(new Probe(e.getX(), e.getY())));
               
                  if(probe.getNumMines() == 0) {
                     return;
                  }
               
               //check flags
                  Iterator iterator = flags.iterator();
                  ArrayList<Flag> foundFlags = new ArrayList<Flag>();
                  while(iterator.hasNext()) {
                     Flag temp = (Flag)(iterator.next());
                     if(probe.dist(temp) < probe.RANGE) {
                        foundFlags.add(temp);
                     }
                  }
                  if(foundFlags.size() >= probe.getNumMines()) {
                     boolean end = false;
                  //check positions of flags
                     for(int i = 0; i < foundFlags.size(); i++) {
                        Flag tempF = (Flag)(foundFlags.get(i));
                        int x = tempF.getX();
                        int y = tempF.getY();
                        Mine temp = new Mine(x, y);
                        boolean match = false;
                        for(int j = 0; j < probe.getNumMines(); j++) {
                           if(probe.getMines().get(j).compareTo(temp) == 0) {
                              match = true;
                              break;
                           }
                        }
                        if(!match) {
                           end = true;
                           break;
                        }
                     }
                     if(end) {
                     //game over
                        gameOver = true;
                        for(int i = 0; i < mines.length(); i++) {
                           iterator = mines.getIterator(i);
                           while(iterator.hasNext()) {
                              ((Mine)(iterator.next())).reveal();
                           }
                        }
                        return;
                     }
                     else {
                     //clear mines, flags, and probes
                        for(int i = 0; i < foundFlags.size(); i++) {
                           flags.remove(foundFlags.get(i));
                        }
                     
                        for(int i = 0; i < probe.getNumMines(); i++) {
                           probe.getMines().get(i).find();
                        }
                     
                        iterator = probes.iterator();
                        while(iterator.hasNext()) {
                           ((Probe)(iterator.next())).update();
                        }
                     }
                  }
                  else {
                  //flash red
                  }
               }
               else {
                  probes.add(new Probe(e.getX(), e.getY()));
               }
            
            }
            else if(e.getButton() == MouseEvent.BUTTON3) {
               if(probes.contains(new Probe(e.getX(), e.getY()))) {
                  ((Probe)(probes.get(new Probe(e.getX(), e.getY())))).enable();
               }
               else {
                  if(!flags.remove(new Flag(e.getX(), e.getY()))) {
                     flags.add(new Flag(e.getX(), e.getY()));
                  }
               }
            }
         }
         catch(Exception ex) {
            
         }
      }
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      
   }

   @Override
   public void mouseExited(MouseEvent e) {
      
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      
   }
   
   //********** END MOUSE **********
}