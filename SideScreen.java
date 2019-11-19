import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class SideScreen extends JPanel implements ActionListener {
   
   private static int numMines = 20;
   
   private JLabel mines = new JLabel("Mines Left: 20/20");
   private JLabel probes = new JLabel("Probes Used: 0");
   private JLabel time = new JLabel("Time: 0:00");
   private JLabel controls = new JLabel("<html>CONTROLS:<br>Left-click: put a probe<br>Right-click: flag/unflag bomb position<br>Left-click on probe: validate flags and clear area<br>Right-click on probe: disable/enable probe</html>");
   
   public SideScreen() {
      super();
      setLayout(new GridLayout(5, 1));
      add(mines);
      add(probes);
      add(time);
      
      //***** SUB-PANEL *****
      JPanel panel = new JPanel();
      
      panel.setLayout(new BorderLayout());
      
      JLabel label = new JLabel("Number of Mines:");
      JTextField text = new JTextField("20");
      JButton button = new JButton("Enter");
      button.addActionListener(this);
      panel.add(label, BorderLayout.WEST);
      panel.add(text, BorderLayout.CENTER);
      panel.add(button, BorderLayout.EAST);
      
      add(panel);
      //***** END SUB-PANEL *****
      
      add(controls);
   }
   
   //updates
   public void update() {
      numMines = 0;
      int totalMines = 0;
      for(int i = 0; i < GameScreen.mines.length(); i++) {
         Iterator iterator = GameScreen.mines.getIterator(i);
         while(iterator.hasNext()) {
            totalMines++;
            Mine temp = (Mine)(iterator.next());
            if(temp != null && !temp.isFound()) {
               numMines++;
            }
         }
      }
      mines.setText("Mines Left: " + numMines + "/" + totalMines);
      probes.setText("Probes Used: " + GameScreen.getNumProbes());
      
      if(!GameScreen.isGameOver()) {
         int sec = (int)(Screen.getFrame() / Driver.TARGET_FPS);
         int min = sec / 60;
         sec %= 60;
      
         if(sec / 10 < 1) {
            time.setText("Time: " + min + ":0" + sec);
         }
         else {
            time.setText("Time: " + min + ":" + sec);
         }
      }
   }
   
   //drawing
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
   }
   
   //rendering graphics
   public void render() {
      repaint();
   }
   
   public static int getNumMines() {
      return numMines;
   }
   
   public void actionPerformed(ActionEvent e) {
      if(e.getActionCommand().equals("Enter")) {
         try {
            int a = Integer.parseInt(((JTextField)(((JPanel)(getComponent(3))).getComponent(1))).getText());
            ((GameScreen)(Driver.screen.getComponent(0))).restart(a);
         }
         catch(Exception ex) {
            
         }
      }
   }
}