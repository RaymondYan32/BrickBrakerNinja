import java.awt.Rectangle;

//Variables and methods for a simple ball in BrickBreaker
public class Ball extends Paddle
{
	private static int diameter;
	protected int speedY;
	private int paddleDelay, nonExistDelay;

	//Constructor for a ball without changing the static size
	public Ball(int x, int y, int speedX, int speedY)
	{
		super(x, y, speedX);
		this.speedY = speedY;
	}

	//Constructor that creates a ball and dictates the static size as well
	public Ball(int x, int y, int speedX, int speedY, int diameter)
	{
		this(x, y, speedX, speedY);
		Ball.diameter = diameter;
	}

	//GETTERS AND SETTERS (method name should be self explanatory)
	public int getSpeedY()
	{
		return speedY;
	}

	public void setSpeedY(int speedY)
	{
		this.speedY = speedY;
	}

	public int getDiameter()
	{
		return diameter;
	}
	
	@Override //balls are perfect circles therefore the width and length are the same
	public Rectangle getBounds()
	{
		return new Rectangle(getX(), getY(), getDiameter(), getDiameter());
	}
	
	//Helps with the prevention of ball being trapped in the paddle
	public void setPaddleDelay(int i)
	{
		paddleDelay = i;
	}

	public int getPaddleDelay()
	{
		return paddleDelay;
	}
	
	//Helps with delay from losing a ball life to another reappearing 
	public int getNonExistDelay()
	{
		return nonExistDelay;
	}

	public void setNonExistDelay(int i)
	{
		nonExistDelay--;
	}

	//BALL COLLISION STUFF
	public void reflectX()
	{
		speedX *= -1;
	}

	public void reflectY()
	{
		speedY *= -1;
	}

	@Override//when ball collides with the borders of the game screen
	public void wallCollision(int screenHeight, int screenWidth)
	{
		if (exist)
		{
			if (x < 0) 
				speedX = Math.abs(speedX);
			else if (x > screenWidth - diameter)
				speedX = -Math.abs(speedX);
			if (y < 0)
				speedY = Math.abs(speedY);
			if (y > screenHeight - diameter)
			{
				exist = false;
				nonExistDelay = 80;
			}
		}
	}

	@Override
	public void move()
	{
		x += speedX;
		y += speedY;
		if (paddleDelay > 0)
			paddleDelay -= 1;
	}	
	
}
