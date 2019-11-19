public class GameObject implements Comparable {
   private int x, y;
   
   protected final int RADIUS = 7;
   
   public GameObject(int xi, int yi) {
      x = xi;
      y = yi;
   }
   
   //getter for x position
   public int getX() {
      return x;
   }
   
   //getter for y position
   public int getY() {
      return y;
   }
   
   //distance formula helper method
   public static double dist(int x1, int x2, int y1, int y2) {
      return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
   }
   
   //returns distance to GameObject go
   public double dist(GameObject go) {
      return dist(x, go.getX(), y, go.getY());
   }
   
   //compares by absolute position
   public int compareTo(Object other) {
      if(this instanceof Probe) {
         Probe po = (Probe)other;
         if(((Probe)(this)).getNumMines() - po.getNumMines() != 0) {
            return ((Probe)(this)).getNumMines() - po.getNumMines();         
         }
         else {
            if(dist(po) < RADIUS) {
               return 0;
            }
            return Driver.WIDTH * getY() + getX() - (Driver.WIDTH * po.getY() + po.getX());
         }
      }
      GameObject go = (GameObject)other;
      if(dist(go) < RADIUS) {
         return 0;
      }
      return Driver.WIDTH * getY() + getX() - (Driver.WIDTH * go.getY() + go.getX());
   }
}