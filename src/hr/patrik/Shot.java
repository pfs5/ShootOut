package hr.patrik;

import hr.patrik.Board.DIR;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Shot {

	private int x;
	private int y;
	private int dx;
	private int dy;
	private int visible;

	private int CLK;
	private int TIME = 500;
	private int SIZE;
	private String COLOUR;
	private int SPEED = 1;
	private DIR direction;
	private int[][] mapMatrix;
	private int BLOCK_SIZE;

	private Image image;

	public Shot (int x, int y, String COLOUR, DIR direction, int[][] mapMatrix, int BLOCK_SIZE) {
		this.x = x;
		this.y = y;
		this.COLOUR = COLOUR;
		this.direction = direction;
		this.mapMatrix = mapMatrix;
		this.BLOCK_SIZE = BLOCK_SIZE;
		
		CLK = 0;
		visible = 1;

		setDirection();

		//Init picture
		String path = "/resource/" + COLOUR.toLowerCase() + "_shot.png";
		ImageIcon ii = new ImageIcon(getClass().getResource(path));
		image = ii.getImage();
		SIZE = image.getHeight(null);
	}

	public void setDirection() {

		dx = 0;
		dy = 0;

		switch (direction) {
		case E: dx = SPEED; 
		break;
		case N: dy = -SPEED;
		break;
		case NE: dy = -SPEED; dx = SPEED;
		break;
		case NW: dy = -SPEED; dx = -SPEED;
		break;
		case S: dy = SPEED;
		break;
		case SE: dy = SPEED; dx = SPEED;
		break;
		case SW: dy = SPEED; dx = -SPEED;
		break;
		case W: dx = -SPEED;
		break;
		default:
			break;

		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Image getImage() {
		return image;
	}

	public int isVisible() {
		return visible;
	}
	
	public void move() {
		//TODO add collisions
		CLK++;

		x += dx;
		y += dy;

		checkCollision();
		checkTime();
	}

	public void checkCollision() {
		int newX = x+dx;
		int newY = y+dy;
		int matrixSize = mapMatrix[0].length;

		for (int i=0; i<matrixSize; i++)
			for (int j=0; j<matrixSize; j++) 
				if (mapMatrix[i][j] == 1){
					
					int blockX = i*BLOCK_SIZE;
					int blockY = j*BLOCK_SIZE;
					
					//Straight
					if (direction == DIR.N)
						if (newX+SIZE>=blockX && newX<=blockX+BLOCK_SIZE && newY==blockY+BLOCK_SIZE) {
							direction = DIR.S;
							setDirection();
						}
					if (direction == DIR.S)
						if (newX+SIZE>=blockX && newX<=blockX+BLOCK_SIZE && newY+SIZE==blockY) {
							direction = DIR.N;
							setDirection();
						}
					if (direction == DIR.E)
						if (newY+SIZE>=blockY && newY<=blockY+BLOCK_SIZE && newX+SIZE==blockX) {
							direction = DIR.W;
							setDirection();
						}
					if (direction == DIR.W)
						if (newY+SIZE>=blockY && newY<=blockY+BLOCK_SIZE && newX==blockX+BLOCK_SIZE) {
							direction = DIR.E;
							setDirection();
						}
					
					//Diagonal
					if (direction == DIR.NE){
						if (newX+SIZE>=blockX && newX<=blockX+BLOCK_SIZE && newY==blockY+BLOCK_SIZE) {
							direction = DIR.SE;
							setDirection();
						}
						if (newX+SIZE==blockX && newY+SIZE>=blockY && newY<=blockY+BLOCK_SIZE) {
							direction = DIR.NW;
							setDirection();
						}
					}
					if (direction == DIR.SW){
						if (newX+SIZE>=blockX && newX<=blockX+BLOCK_SIZE && newY+SIZE==blockY) {
							direction = DIR.NW;
							setDirection();
						}
						if (newX==blockX+BLOCK_SIZE && newY+SIZE>=blockY && newY<=blockY+BLOCK_SIZE) {
							direction = DIR.SE;
							setDirection();
						}
					}
					if (direction == DIR.SE){
						if (newX+SIZE>=blockX && newX<=blockX+BLOCK_SIZE && newY==blockY) {
							direction = DIR.NE;
							setDirection();
						}
						if (newX+SIZE==blockX && newY+SIZE>=blockY && newY<=blockY+BLOCK_SIZE) {
							direction = DIR.SW;
							setDirection();
						}
					}
					if (direction == DIR.NW){
						if (newX+SIZE>=blockX && newX<=blockX+BLOCK_SIZE && newY==blockY+BLOCK_SIZE) {
							direction = DIR.SW;
							setDirection();
						}
						if (newX+SIZE==blockX+BLOCK_SIZE && newY+SIZE>=blockY && newY<=blockY+BLOCK_SIZE) {
							direction = DIR.NE;
							setDirection();
						}
					}
						
				}
	}
	
	public void checkTime() {
		if (CLK>=TIME)
			visible = 0;
	}


}
