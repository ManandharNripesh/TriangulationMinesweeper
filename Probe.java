import java.awt.Graphics;
import java.awt.Color;

import java.util.ArrayList;
import java.util.Iterator;

public class Probe extends GameObject {
   private ArrayList<Mine> mines = new ArrayList<Mine>();      //represents radial distances from this probe to mine in its range
   
   private boolean enabled = true;
   
   protected final int RANGE = RADIUS * 12;
   
   public Probe(int xi, int yi) {
      super(xi, yi);
      update();
   }
   
   //updates mines to match found mines
   public void update() {
      mines = new ArrayList<Mine>();
      int width = GameScreen.getScreenWidth()/HashTable.MULT;
      int height = GameScreen.getScreenHeight()/HashTable.MULT;
      
      int center = getY()/height * HashTable.MULT + getX()/width;
      int north = center - HashTable.MULT;
      int south = center + HashTable.MULT;
      int west = center - 1;
      int east = center + 1;
      
      Tree temp = new Tree();
      
      Iterator iterator = GameScreen.mines.getIterator(center);
      while(iterator.hasNext()) {
         temp.add((Mine)(iterator.next()));
      }
      
      if(north > 0) {
         iterator = GameScreen.mines.getIterator(north);
         while(iterator.hasNext()) {
            temp.add((Mine)(iterator.next()));
         }
      }
      
      if(south < GameScreen.mines.length()) {
         iterator = GameScreen.mines.getIterator(south);
         while(iterator.hasNext()) {
            temp.add((Mine)(iterator.next()));
         }
      }
      
      if(west > 0 && west / HashTable.MULT == center / HashTable.MULT) {
         iterator = GameScreen.mines.getIterator(west);
         while(iterator.hasNext()) {
            temp.add((Mine)(iterator.next()));
         }
         if(north > 0) {
            iterator = GameScreen.mines.getIterator(north - 1);
            while(iterator.hasNext()) {
               temp.add((Mine)(iterator.next()));
            }
         }
         if(south < GameScreen.mines.length()) {
            iterator = GameScreen.mines.getIterator(south - 1);
            while(iterator.hasNext()) {
               temp.add((Mine)(iterator.next()));
            }
         }
      }
      
      if(east / HashTable.MULT == center / HashTable.MULT) {
         iterator = GameScreen.mines.getIterator(east);
         while(iterator.hasNext()) {
            temp.add((Mine)(iterator.next()));
         }
         if(north > 0) {
            iterator = GameScreen.mines.getIterator(north + 1);
            while(iterator.hasNext()) {
               temp.add((Mine)(iterator.next()));
            }
         }
         if(south < GameScreen.mines.length()) {
            iterator = GameScreen.mines.getIterator(south + 1);
            while(iterator.hasNext()) {
               temp.add((Mine)(iterator.next()));
            }
         }
      }
      
      iterator = temp.iterator();
      while(iterator.hasNext()) {
         Mine mine = (Mine)(iterator.next());
         double dist = dist(mine);
         if(!mine.isFound() && dist <= RANGE) {
            mines.add(mine);
         }
      }
   }
   
   //return number of mines
   public int getNumMines() {
      return mines.size();
   }
   
   //return mines
   public ArrayList<Mine> getMines() {
      return mines;
   }
   
   //draw the probe and white circles
   public void draw(Graphics g) {
      if(enabled) {
         g.setColor(Color.BLACK);
         if(getNumMines() > 0) {
            g.fillOval(getX() - RADIUS, getY() - RADIUS, RADIUS * 2, RADIUS * 2);
         }
         else {
            g.fillOval(getX() - RADIUS / 2, getY() - RADIUS / 2, RADIUS, RADIUS);
         }
      
         g.setColor(Color.WHITE);
         for(int i = 0; i < mines.size(); i++) {
            g.drawOval((int)(getX() - dist(mines.get(i))), (int)(getY() - dist(mines.get(i))), (int)(dist(mines.get(i)) * 2), (int)(dist(mines.get(i)) * 2));
         }
      
         if(getNumMines() > 0) {
            g.drawString(mines.size() + "", getX() - RADIUS / 2, getY() + 6);
         }
      }
      else {
         g.setColor(Color.WHITE);
         g.drawString("X", getX() - RADIUS / 2, getY() + 6);
      }
   }
   
   //draw the probe background
   public void drawBackground(Graphics g) {
      if(enabled) {
         if(mines.size() == 0) {
            g.setColor(Color.BLUE.brighter().brighter().brighter());
         }
         else {
            g.setColor(Color.BLUE.darker());
         }
         g.fillOval(getX() - RANGE, getY() - RANGE, RANGE * 2, RANGE * 2);
      }
   }
   
   public void enable() {
      enabled = !enabled;
   }
}