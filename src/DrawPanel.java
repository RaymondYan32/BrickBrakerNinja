import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class DrawPanel extends JPanel implements KeyListener
{
	// INSTANTIATION OF FIELD VARIABLES

	// Prime
	final int WIDTH = 970, HEIGHT = 600, COLUMNS = 17, ROWS = 4;
	int lives, score, level;
	boolean win, lose;

	// Objects
	BufferedImage buffer;
	Brick[][] brick;
	Paddle paddle;
	Ball ball;
	BadBall[] ninjaKunai;

	// Images
	Image BadKunaiImageDownRight;
	Image BadKunaiImageDownLeft;

	Image KunaiImageTopRight;
	Image KunaiImageTopLeft;
	Image KunaiImageDownRight;
	Image KunaiImageDownLeft;
	Image brickImage;
	Image NinjaImage;

	Image paddleImage;
	Image background;
	{
	try{
		BadKunaiImageDownRight = ImageIO.read(new File("images/Badkunai[DR].png"));
		BadKunaiImageDownLeft = ImageIO.read(new File("images/Badkunai[DL].png"));

		KunaiImageTopRight = ImageIO.read(new File("images/kunai[TR].png"));
		KunaiImageTopLeft = ImageIO.read(new File("images/kunai[TL].png"));
		KunaiImageDownRight = ImageIO.read(new File("images/kunai[DR].png"));
		KunaiImageDownLeft = ImageIO.read(new File("images/kunai[DL].png"));
		brickImage = ImageIO.read(new File("images/brick.png"));
		NinjaImage = ImageIO.read(new File("images/Ninja.png"));

		paddleImage = ImageIO.read(new File("images/paddle.png"));
		background = ImageIO.read(new File("images/background.jpg"));
	}
	catch (Exception e)
	{}}
	

	public DrawPanel()
	{
		setIgnoreRepaint(true);
		addKeyListener(this);
		setFocusable(true);
	}

	/************************** KeyListener needs these **********************************************/
	public void keyTyped(KeyEvent e)
	{

	}

	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT)
			paddle.setLeft(true);
		if (key == KeyEvent.VK_RIGHT)
			paddle.setRight(true);

		// CHEATS
		if (key == KeyEvent.VK_END)
			win = true;
		if (key == KeyEvent.VK_ENTER)
			lives += 10;
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT)
			paddle.setLeft(false);
		if (key == KeyEvent.VK_RIGHT)
			paddle.setRight(false);
	}

	/************************** end methods needed for KeyListener ****************************************/

	// Initializes all the field variables
	public void initialize()
	{
		// for double buffering
		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		// bricks
		brick = new Brick[ROWS][COLUMNS];
		Brick.initialize(40, 44);
		brickGenerator();

		// paddle
		paddle = new Paddle(100, 460, 5, 64, 100);

		// ball
		ball = new Ball(50, 420, 3, -6, 30);

		// general
		win = false;
		lose = false;
		score = 0;
		level = 0;
		lives = 2;
	}

	// Generate all the bricks (targets) during the startup of a level
	public void brickGenerator()
	{
		// Initializes all the bricks to default with unique x and y co
		// ordinates
		for (int row = 0; row < ROWS; row++)
			for (int col = 0; col < COLUMNS; col++)
			{
				brick[row][col] = new Brick(col * 55 + 20, row * 50 + 50);
			}

		// Generates random Ninjas among the bricks based of level
		ninjaKunai = new BadBall[4 + level];
		int counter = 0;
		// a loop to prevent ninjas being spawned on the same location (will
		// cause infinite loop if level 65 is reached)
		while (counter < ninjaKunai.length)
		{
			int randRow = (int) (Math.random() * ROWS);
			int randCol = (int) (Math.random() * COLUMNS);
			// Checks if the randomly selected brick currently exists (if it
			// does it is already a Ninja brick)
			if (!brick[randRow][randCol].getExist())
			{
				// Initializes the kunai for the ninja and reintializes the
				// brick into the subclass ninjaBrick
				ninjaKunai[counter] = new BadBall(0, 0, 0, 0, randRow, randCol);
				brick[randRow][randCol] = new NinjaBrick(brick[randRow][randCol].getX(), brick[randRow][randCol].getY());
				counter++;
			}
		}

		// Sets all bricks to exist
		for (int row = 0; row < ROWS; row++)
			for (int col = 0; col < COLUMNS; col++)
			{
				brick[row][col].setExist(true);
			}
	}

	// All the movement
	public void update()
	{

		// ball respawn if it leaves the screen
		if (!ball.getExist())
		{
			if (ball.getNonExistDelay() == 80)
			{
				lives--;
				ball.setSpeedY(-5);
				ball.setY(420);
				ball.setX(paddle.getX());
				ball.reflectX();
			}

			if (ball.getNonExistDelay() > 0)
			{
				ball.setNonExistDelay((ball.getNonExistDelay() - 1));
			} else
			{
				ball.setExist(true);
			}
		}

		winLoseCheck();
		// Checks if the player lost the game or continue with movement
		// functions
		if (win || lose)
		{
			ResetGame();
		} else
		{
			paddle.move();

			// prevents ball from being trapped in the paddle
			if (paddle.getImmuneTimer() > 0)
			{
				paddle.setImmuneTimer((paddle.getImmuneTimer() - 1));
			}

			// Reallows paddle to move after a delay if it was hit by a badball
			// recently
			if (paddle.getHurtTimer() > 0)
				paddle.setHurtTimer((paddle.getHurtTimer() - 1));
			else if (paddle.getSpeedX() != Paddle.getNormalSpeed())
				paddle.setSpeedX(Paddle.getNormalSpeed());

			// ball movement
			if (ball.getExist())
				ball.move();

			// Bad kunai movement
			for (int i = 0; i < ninjaKunai.length; i++)
			{
				if (ninjaKunai[i].getExist())
					ninjaKunai[i].move();
				else if (((NinjaBrick) brick[ninjaKunai[i].getRow()][ninjaKunai[i].getCol()]).getExist())
				{
					// Controls when bad kunais are thrown
					((NinjaBrick) brick[ninjaKunai[i].getRow()][ninjaKunai[i].getCol()]).ninjaThrow();
					if (((NinjaBrick) brick[ninjaKunai[i].getRow()][ninjaKunai[i].getCol()]).getBallThrow())
					{
						ninjaKunai[i].thrown(((NinjaBrick) brick[ninjaKunai[i].getRow()][ninjaKunai[i].getCol()]).getX(),
								((NinjaBrick) brick[ninjaKunai[i].getRow()][ninjaKunai[i].getCol()]).getY());
						((NinjaBrick) brick[ninjaKunai[i].getRow()][ninjaKunai[i].getCol()]).setBallThrow(false);
					}
				}
			}
		}

	}

	// Gives the player the decision to quit, continue playing (score is kept),
	// or start again.
	public void ResetGame()
	{
		Object[] options = { "YES", "NO" };
		int n = -1;

		// Display window giving the options based off whether the player won or
		// lost
		if (win)
		{
			n = JOptionPane.showOptionDialog(null, "Winner! Do you wish to continue?", "Game Over", JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		} else if (lose)
		{
			n = JOptionPane.showOptionDialog(null, "You Lost! Do you want to play again?", "Game Over", JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		}

		// if player selected to continue playing or play again
		if (n == 0)
		{
			// Reset game back to level 1 (All that score you collected last
			// game is gone)
			if (lose)
			{
				score = 0;
				lives = 2;
				level = 0;
			} else
				level++;

			brickGenerator();
			paddle = new Paddle(100, 460, 5);
			ball = new Ball(50, 420, 3, -6);

			win = false;
			lose = false;
		}
		// Quits the program if the player decided not to continue
		else if (n == 1)
		{
			System.exit(0);
		}
	}

	// Checks if the player has won or lost
	public void winLoseCheck()
	{
		for (int row = 0; row < ROWS; row++)
			for (int col = 0; col < COLUMNS; col++)
			{
				if (brick[row][col].getExist())
				{
					row = ROWS;
					col = COLUMNS;
				}
				if (row == ROWS - 1 && col == COLUMNS - 1)
					win = true;
			}

		if (lives < 0)
			lose = true;

	}

	// Checks if objects have collided and adjust them as suited
	public void checkCollisions()
	{
		paddle.wallCollision(HEIGHT, WIDTH);

		// GOOD KUNAI (BALL)
		if (ball.getExist())
		{
			//WALL
			ball.wallCollision(HEIGHT, WIDTH);

			Rectangle testCase1;
			Rectangle testCase2;
			
			//PADDLE
			if (ball.getPaddleDelay() == 0)
			{
				if (ball.getBounds().intersects(paddle.getBounds()))
				{
					ball.setPaddleDelay(30);
					testCase1 = new Rectangle(ball.getX() - ball.getSpeedX(), ball.getY(), ball.getDiameter(), ball.getDiameter());
					testCase2 = new Rectangle(ball.getX(), ball.getY() - ball.getSpeedY(), ball.getDiameter(), ball.getDiameter());
					
					//if paddle moves into a ball that is "running away")
					if (ball.getY() > paddle.getY() - (ball.getDiameter() - Math.abs(ball.getSpeedY()) - 1)
							&& (ball.getSpeedX() < 0 && ball.getX() < paddle.getX() || ball.getSpeedX() > 0 && ball.getX() > paddle.getX()))
					{
						
					}
					//Sides
					else if (!testCase1.intersects(paddle.getBounds()))
					{
						ball.reflectX();
						ball.reflectY();	
					} 
					//Top
					else if (!testCase2.intersects(paddle.getBounds()))
						ball.reflectY();
					//Diagonally
					else
					{
						ball.reflectX();
						ball.reflectY();
					}
				}
			}

			//BRICKS
			brickCheck:
			{
				for (int row = 0; row < ROWS; row++)
					for (int col = 0; col < COLUMNS; col++)
						if (ball.getBounds().intersects(brick[row][col].getBounds()) && brick[row][col].getExist())
						{
							score++;
							brick[row][col].setExist(false);

							testCase1 = new Rectangle(ball.getX() - ball.getSpeedX(), ball.getY(), ball.getDiameter(), ball.getDiameter());
							testCase2 = new Rectangle(ball.getX(), ball.getY() - ball.getSpeedY(), ball.getDiameter(), ball.getDiameter());
							
							//LEFT OR RIGHT SIDES
							if (!testCase1.intersects(brick[row][col].getBounds()))
								ball.reflectX();
							//TOP OR BOTTOM
							else if (!testCase2.intersects(brick[row][col].getBounds()))
								ball.reflectY();
							//DIAGONALLY
							else
							{
								ball.reflectX();
								ball.reflectY();
							}

							break brickCheck;

						}
			}

		}

		//BAD KUNAI (BadBalls)
		for (int i = 0; i < ninjaKunai.length; i++)
		{
			if (ninjaKunai[i].getExist())
			{
				ninjaKunai[i].wallCollision(HEIGHT, WIDTH);

				if (ninjaKunai[i].getBounds().intersects(paddle.getBounds()) && paddle.getImmuneTimer() == 0)
				{
					paddle.setSpeedX(0);
					paddle.setHurtTimer(30);
					paddle.setImmuneTimer(120);
				}
			}
		}

	}

	/* ALL drawing will be done here */
	public void drawBuffer()
	{
		Graphics2D b = buffer.createGraphics();

		b.drawImage(background, 0, 0, null);

		// Draws all the bricks (both dummy targets and ninjas)
		for (int row = 0; row < ROWS; row++)
			for (int col = 0; col < COLUMNS; col++)
			{
				try
				{
					if (((NinjaBrick) brick[row][col]).getExist())
						b.drawImage(NinjaImage, brick[row][col].getX(), brick[row][col].getY(), null);
				} catch (Exception e)
				{
					if (brick[row][col].getExist())
						b.drawImage(brickImage, brick[row][col].getX(), brick[row][col].getY(), null);
				}
			}

		// Draws the paddle image, will flash if hit by red kunai recently
		if (paddle.getImmuneTimer() % 5 <= 3)
			b.drawImage(paddleImage, paddle.getX(), paddle.getY(), null);

		// Draws the ball (in this case Kunai) based on direction it is moving
		for (int i = 0; i < ninjaKunai.length; i++)
		{
			if (ninjaKunai[i].getExist())
			{
				if (ninjaKunai[i].getSpeedX() > 0 && ninjaKunai[i].getSpeedY() > 0)
					b.drawImage(BadKunaiImageDownRight, ninjaKunai[i].getX(), ninjaKunai[i].getY(), null);
				else if (ninjaKunai[i].getSpeedX() < 0 && ninjaKunai[i].getSpeedY() > 0)
					b.drawImage(BadKunaiImageDownLeft, ninjaKunai[i].getX(), ninjaKunai[i].getY(), null);
			}
		}

		// Draws the Good Kunai based on it's direction
		if (ball.getExist())
		{
			if (ball.getSpeedX() > 0 && ball.getSpeedY() < 0)
				b.drawImage(KunaiImageTopRight, ball.getX(), ball.getY(), null);
			else if (ball.getSpeedX() < 0 && ball.getSpeedY() < 0)
				b.drawImage(KunaiImageTopLeft, ball.getX(), ball.getY(), null);
			else if (ball.getSpeedX() > 0 && ball.getSpeedY() > 0)
				b.drawImage(KunaiImageDownRight, ball.getX(), ball.getY(), null);
			else if (ball.getSpeedX() < 0 && ball.getSpeedY() > 0)
				b.drawImage(KunaiImageDownLeft, ball.getX(), ball.getY(), null);
		}

		// Draws various texts around the screen
		b.setFont(new Font("Times New Roman", Font.BOLD, 20));
		b.setColor(Color.black);
		b.drawString("Score: " + score, 10, HEIGHT - 50);
		b.drawString("Remaining Kunais: " + lives, WIDTH - 200, HEIGHT - 50);
		b.drawString("Level: " + (level + 1), WIDTH / 2 - 50, 20);
	}

	/* takes what was drawn in the RAM and places it on the screen */
	public void drawScreen()
	{
		Graphics2D g = (Graphics2D) this.getGraphics();

		g.drawImage(buffer, 0, 0, this);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	/* GAME LOOP */
	public void startGame()
	{
		initialize();
		while (true)
		{
			try
			{
				update();
				checkCollisions();
				drawBuffer();
				drawScreen();
				Thread.sleep(15);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
