import java.awt.Rectangle;

//Variables and methods for a simple brick in BrickBricker
   class Brick
   {
      protected int x, y;
      private static int width, height;
      protected boolean exist;
      
      public Brick(int x, int y)
      {
         this.x = x;
         this.y = y;
         exist = false;
      }
      
      //sets width and height of ALL bricks in the game
      public static void initialize(int w, int h)
      {
    	  width = w;
    	  height = h;
      }
      
      //GETTERS AND SETTERS (method name should be self explanatory)
      public int getX()
      {
         return x;
      }
      
      public void setX(int x)
      {
    	  this.x = x;
      }
    
      public int getY()
      {
         return y;
      }
      
      public void setY(int y)
      {
    	  this.y = y;
      }
    
      public void setExist(boolean a)
      {
    	  exist = a;
      }
      
      public boolean getExist ()
      {
    	  return exist;
      }
      
      public static int getWidth()
      {
         return width;
      }
    
      public static int getHeight()
      {
         return height;
      }
    
      public Rectangle getBounds()
      {
         return new Rectangle(getX(),getY(),getWidth(),getHeight());
      }
   
   }