import java.awt.Graphics;
import java.awt.Color;

public class Flag extends GameObject {
   
   public Flag(int xi, int yi) {
      super(xi, yi);
   }
   
   //draw the flag
   public void draw(Graphics g) {
      g.setColor(Color.RED);
      g.fillOval(getX() - RADIUS / 2, getY() - RADIUS / 2, RADIUS, RADIUS);
      g.setColor(Color.BLACK);
      g.drawOval(getX() - RADIUS / 2, getY() - RADIUS / 2, RADIUS, RADIUS);
   }
}