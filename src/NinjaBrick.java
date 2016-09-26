//Variables and methods for a special type of brick in BrickBreaker
public class NinjaBrick extends Brick
{
	private int delay;
	private boolean ballThrow;
	
	public NinjaBrick(int x , int y)
	{
		super (x,y);
		exist = true;
		delay = ((int)(Math.random() * 500));
		ballThrow = false;
	}

	//Runs When the ninja throws 
	public void ninjaThrow ()
	{
		if (delay > 0)
			delay --;
		else
		{
			ballThrow = true;
			delay = 100 + ((int)(Math.random() * 400));
		}
	}
	
	//GETTERS AND SETTERS
	public boolean getBallThrow ()
	{
		return ballThrow;
	}
	
	public void setBallThrow (boolean b)
	{
		ballThrow = b;
	}
	
}
