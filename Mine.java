import java.awt.Graphics;
import java.awt.Color;

public class Mine extends GameObject {
   
   private boolean found = false, revealed = false;
   
   public Mine(int xi, int yi) {
      super(xi, yi);
   }
   
   //draw the mine
   public void draw(Graphics g) {
      if(revealed) {
         g.setColor(Color.RED);
      }
      else if(found) {
         g.setColor(Color.GREEN);
      }
      else {
         return;
      }
      g.fillOval(getX() - RADIUS, getY() - RADIUS, RADIUS * 2, RADIUS * 2);
      g.setColor(Color.BLACK);
      g.drawOval(getX() - RADIUS, getY() - RADIUS, RADIUS * 2, RADIUS * 2);
   }
   
   //creates hash code based on xy-coordinates
   public int hashCode() {
      int width = GameScreen.getScreenWidth() / HashTable.MULT;
      int height = GameScreen.getScreenHeight() / HashTable.MULT;
      return getY() / height * HashTable.MULT + getX() / width;
   }
   
   public boolean isFound() {
      return found;
   }
   
   public void find() {
      found = true;
   }
   
   public void reveal() {
      revealed = true;
   }
}