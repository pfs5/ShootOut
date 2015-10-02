package hr.patrik;

import hr.patrik.Board.DIR;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/*
 * Tank object
 */

public class Tank {

	//Local variables
	private int x;
	private int y;
	private int dxL;
	private int dxR;
	private int dyU;
	private int dyD;

	private int shotsNumber;

	Image image;
	private DIR direction;
	private int directionIndex;
	private int[][] mapMatrix;
	private ArrayList<Shot> shots;

	//Constants
	private String COLOUR;
	private int TANK_SIZE;
	private int SPEED = 1;
	private int EDGE = 7;
	private int BLOCK_SIZE;
	private int MAX_SHOTS = 5;

	public Tank (int x, int y, String COLOUR, int [][] mapMatrix, int BLOCK_SIZE) {
		this.x = x;
		this.y = y;
		this.COLOUR = COLOUR;
		this.mapMatrix = mapMatrix;
		this.BLOCK_SIZE = BLOCK_SIZE;

		shots = new ArrayList<Shot>();
		shotsNumber=0;

		//Initial settings
		direction = DIR.N;
		directionIndex = 0;
		setImage(direction);

		TANK_SIZE = image.getWidth(null)-EDGE;
	}

	private void setImage(DIR direction) {
		String dir;

		switch (direction) {
		case N: dir = "n";
		break;
		case E: dir = "e";
		break;
		case NE: dir = "ne";
		break;
		case NW: dir = "nw";
		break;
		case S: dir = "s";
		break;
		case SE: dir = "se";
		break;
		case SW: dir = "sw" ;
		break;
		case W: dir = "w";
		break;
		default: dir = "";
		break;
		}

		String path = "/resource/" + COLOUR.toLowerCase() + "_tank_" + dir + ".png";
		ImageIcon ii = new ImageIcon(getClass().getResource(path));
		image = ii.getImage();
	}

	public int getX () {
		return x;
	}

	public int getY () {
		return y;
	}

	public Image getImage() {
		return image;
	}

	public ArrayList<Shot> getShots () {
		return shots;
	}

	public void setShots(ArrayList<Shot> shots) {
		this.shots = shots;
	}

	public void shoot () {
		int shotX = x + (TANK_SIZE + EDGE)/2 -2;
		int shotY = y + (TANK_SIZE + EDGE)/2 -2;

		if (shotsNumber<MAX_SHOTS) {
			Shot newShot = new Shot(shotX, shotY, COLOUR, direction, mapMatrix, BLOCK_SIZE);
			shots.add(newShot);
			shotsNumber++;
		}
	}

	public void move () {

		boolean possible;
		int newX = x+dxR+dxL;
		int newY = y+dyU+dyD;
		possible = checkMove(newX, newY);

		if (possible == true) {
			x += (dxR+dxL);
			y += (dyU+dyD);
		}
		
		checkShots();
	}
	
	public void checkShots() {
		for (int i=0; i<shots.size(); i++) {
			Shot current = shots.get(i);
			if (current.isVisible() == 0) {
				shots.remove(i);
				shotsNumber--;
			}
		}
	}

	public boolean checkMove(int newX, int newY) {

		int blockNumber = mapMatrix[0].length;
		int maxX = blockNumber*BLOCK_SIZE;
		int maxY = maxX;

		if (newX+EDGE<0 || newX + TANK_SIZE >maxX )
			return false;
		if (newY+EDGE<0 || newY + TANK_SIZE>maxY)
			return false;

		for (int i=0; i<blockNumber; i++)
			for (int j=0; j<blockNumber; j++) {
				int blockX = i*BLOCK_SIZE;
				int blockY = j*BLOCK_SIZE;

				if (mapMatrix[i][j] == 1) {

					//top left corner
					if (newX+EDGE>blockX && newX+EDGE<blockX+BLOCK_SIZE && newY+EDGE>blockY && newY+EDGE<blockY+BLOCK_SIZE)
						return false;
					//top right corner
					if (newX+TANK_SIZE>blockX && newX+TANK_SIZE<blockX+BLOCK_SIZE && newY+EDGE>blockY && newY+EDGE<blockY+BLOCK_SIZE)
						return false;
					//bottom left corner
					if (newX+EDGE>blockX && newX+EDGE<blockX+BLOCK_SIZE && newY+TANK_SIZE>blockY && newY+TANK_SIZE<blockY+BLOCK_SIZE)
						return false;
					//bottom right
					if (newX+TANK_SIZE>blockX && newX+TANK_SIZE<blockX+BLOCK_SIZE && newY+TANK_SIZE>blockY && newY+TANK_SIZE<blockY+BLOCK_SIZE)
						return false;
				}
			}
		return true;
	}

	public void incIndex(){
		directionIndex = (directionIndex+1) % 8;
		setDirection();
		setImage(direction);
	}

	public void decIndex() {
		if (directionIndex>0)
			directionIndex--;
		else
			directionIndex = 7;
		setDirection();
		setImage(direction);
	}

	public void setDirection(){
		switch (directionIndex) {
		case 0: direction = DIR.N;
		break;
		case 1: direction = DIR.NE;
		break;
		case 2: direction = DIR.E;
		break;
		case 3: direction = DIR.SE;
		break;
		case 4: direction = DIR.S;
		break;
		case 5: direction = DIR.SW;
		break;
		case 6: direction = DIR.W;
		break;
		case 7: direction = DIR.NW;
		break;
		}
	}

	public void KeyPressed (KeyEvent e) {
		int key = e.getKeyCode();

		if (COLOUR.equals("RED"))
			switch (key) {
			case KeyEvent.VK_I: dyU = -1*SPEED;
			break;
			case KeyEvent.VK_J: dxL = -1*SPEED;
			break;
			case KeyEvent.VK_K: dyD = 1*SPEED;
			break;
			case KeyEvent.VK_L: dxR = 1*SPEED;
			break;
			case KeyEvent.VK_U: decIndex();
			break;
			case KeyEvent.VK_O: incIndex();
			break;
			case KeyEvent.VK_ENTER: shoot();
			break;
			}

		if (COLOUR.equals("BLUE"))
			switch (key) {
			case KeyEvent.VK_W: dyU = -1*SPEED;
			break;
			case KeyEvent.VK_A: dxL = -1*SPEED;
			break;
			case KeyEvent.VK_S: dyD = 1*SPEED;
			break;
			case KeyEvent.VK_D: dxR = 1*SPEED;
			break;
			case KeyEvent.VK_Q: decIndex();
			break;
			case KeyEvent.VK_E: incIndex();
			break;
			case KeyEvent.VK_SPACE: shoot();
			break;
			}
	}

	public void KeyReleased (KeyEvent e) {
		int key = e.getKeyCode();

		if (COLOUR.equals("RED"))
			switch (key) {
			case KeyEvent.VK_I: dyU = 0;
			break;
			case KeyEvent.VK_J: dxL = 0;
			break;
			case KeyEvent.VK_K: dyD = 0;
			break;
			case KeyEvent.VK_L: dxR = 0;
			break;
			}

		if (COLOUR.equals("BLUE"))
			switch (key) {
			case KeyEvent.VK_W: dyU = 0;
			break;
			case KeyEvent.VK_A: dxL = 0;
			break;
			case KeyEvent.VK_S: dyD = 0;
			break;
			case KeyEvent.VK_D: dxR = 0;
			break;
			}
	}
}
