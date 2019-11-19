import java.util.Iterator;

public class HashTable {
   public static final int MULT = 2;
   
   private Tree[] table;
   
   public HashTable() {
      table = new Tree[MULT * MULT];
      for(int i = 0; i < table.length; i++) {
         table[i] = new Tree();
      }
   }
   
   //add o to table
   public boolean add(Comparable o) {
      return table[o.hashCode() % table.length].add(o);
   }
   
   //remove o from table
   public boolean remove(Comparable o) {
      return table[o.hashCode() % table.length].remove(o);
   }
   
   public Tree get(int i) {
      return table[i];
   }
   
   //return iterator of one tree in table
   public Iterator getIterator(int i) {
      return table[i].iterator();
   }
   
   public int length() {
      return table.length;
   }
   
   //creates hash code based on xy-coordinates
   public static int hashCode(int x, int y) {
      int width = GameScreen.getScreenWidth() / MULT;
      int height = GameScreen.getScreenHeight() / MULT;
      return y / height * MULT + x / width;
   }
}