// from:  http://forum.codecall.net/topic/47096-double-buffering-movement-and-collision-detection/
/* Author : Raymond Yan
 * Date : March 18, 2015.
 * Description : BrickBreaker game modified with ninja sprites
 
 INSTRUCTIONS:
 - Move your character (paddle) on the bottom of the screen using left and right arrow keys
 - Deflect the bouncing black kunai with your character to hit every target on the upper part of the screen
 - Black kunais will disappear if they go past the bottom of the screen 
 - Lose when you run out of spare kunais
 - Watch out for the Ninjas who will throw their red kunais at you, if you are hit you will be stunned temporarily
 
 (CHEATS)
 - Press Enter to get 10 lives, hold for more
 - Press end to instantly win level 
 (Will reach a infinite loop at around level 65, not like anyone can get that far without cheating therefore irrelevant)
*/
   import javax.swing.*;

/*****************************************************************************************************/
// create a window with Swing
   public class BrickBreaker_Raymond
   {
      JFrame window;
      DrawPanel panel;
   
		// constructor
      public BrickBreaker_Raymond()
      {
         window = new JFrame("Ninja Training");
         panel = new DrawPanel();
         window.setSize(panel.WIDTH + 15,panel.HEIGHT);
         window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         window.getContentPane().add(panel);
         window.setLocationRelativeTo(null);
         window.setVisible(true);
      }
      
      public void go()
      {
         panel.startGame();
      }
      
      public static void main(String[]args)
      {
         BrickBreaker_Raymond game = new BrickBreaker_Raymond();
         game.go();
      }
   }

/*****************************************************************************************************/


