import java.awt.Rectangle;

//Variables and methods for a simple paddle in BrickBreaker
public class Paddle extends Brick
{
	private boolean left, right;
	private static int width, height, normalSpeed;
	private int hurtTimer, immuneTimer;
	protected int speedX;

	// Creates a paddle without changing the static width or height (unused but originally planned for 2p)
	public Paddle(int x, int y, int speed)
	{
		super(x, y);
		speedX = speed;
		exist = true;
		left = false;
		right = false;
		hurtTimer = 0;
		immuneTimer = 0;
	}

	// Creates a paddle with a static width and height
	public Paddle(int x, int y, int speed, int width, int height)
	{
		this(x, y, speed);
		normalSpeed = speed;
		Paddle.width = width;
		Paddle.height = height;
	}

	// GETTERS AND SETTERS (method name should be self explanatory)
	public int getSpeedX()
	{
		return speedX;
	}

	public void setSpeedX(int speed)
	{
		speedX = speed;
	}

	// Sets the debuff timer when paddle is stunned
	public void setHurtTimer(int time)
	{
		hurtTimer = time;
	}

	public int getHurtTimer()
	{
		return hurtTimer;
	}

	public void setImmuneTimer(int time)
	{
		immuneTimer = time;
	}

	public int getImmuneTimer()
	{
		return immuneTimer;
	}

	public void setRight(boolean b)
	{
		right = b;
	}

	public void setLeft(boolean b)
	{
		left = b;
	}

	public static int getNormalSpeed()
	{
		return normalSpeed;
	}

	// Override due to static variable problems
	public static int getWidth()
	{
		return width;
	}

	// Override due to static variable problems
	public static int getHeight()
	{
		return height;
	}

	// Override due to static variable problems
	public Rectangle getBounds()
	{
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	//When paddle tries to leaves screen (although screenHeight is not used, the top and bottom of the screen are "walls" 
	//in the brickbreaker and availability to use the top and bottom should be included within the parameters)
	public void wallCollision(int screenHeight, int screenWidth)
	{
		if (x < 0)
			x = 0;
		else if (x > screenWidth - width)
			x = screenWidth - width;
	}

	// movement of the paddle
	public void move()
	{
		if (left)
			x -= speedX;
		if (right)
			x += speedX;
	}

}
