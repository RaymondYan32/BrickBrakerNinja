//Object Class for "balls" that thrown at the player
public class BadBall extends Ball
{ 
	private int row, col;
	
	public BadBall(int x, int y, int speedX, int speedY, int row, int col)
	{
		super (x,y, speedX, speedY);
		exist = false;
		this.row = row;
		this.col = col;
	}
	
	//returns the row of the ninja connected to this BadBall
	public int getRow()
	{
		return row;
	}
	
	//returns the col of the ninja connected to this BadBall
	public int getCol()
	{
		return col;
	}
	
	// Sets speed at which it travels when thrown
	public void thrown (int x , int y)
	{
		exist = true;
		this.x = x;
		this.y = y;
		
		if (Math.random() > 0.5)
			speedX = (1 + (int)(Math.random() * 2));
		else
			speedX = -(1 + (int)(Math.random() * 2));
		speedY = 2 + (int)(Math.random() * 3);	
	}
}
