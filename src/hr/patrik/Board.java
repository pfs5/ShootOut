package hr.patrik;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;




public class Board extends JPanel implements ActionListener {

	public enum DIR {
		N,
		NE,
		E,
		SE,
		S,
		SW,
		W,
		NW
	}

	public enum STATE {
		GAME
	}

	//Variables
	private int CLK;
	private int WIDTH ;
	private int HEIGTH;
	private int DELAY = 5;
	private int BLOCK_SIZE;
	private int blockNumber;
	private int TANK_PERIOD = 2;

	private STATE state;

	private int redTankX;
	private int redTankY;
	private int blueTankX;
	private int blueTankY;
	private boolean playable;

	private int[][] mapMatrix;
	private ArrayList<Shot> redShots;
	private ArrayList<Shot> blueShots;

	Timer timer;
	Image blockImage;
	Tank redTank;
	Tank blueTank;
	Random randomNumber;

	public Board (int width, int heigth) {
		this.WIDTH = width;
		this.HEIGTH = heigth;
		initBoard();
	}

	public void initBoard() {

		//Necessary part
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.WHITE);
		setDoubleBuffered(true);

		timer = new Timer(DELAY, this);
		timer.start();

		randomNumber = new Random();
		initGame();
	}

	public void initGame() {
		//Set state
		state = STATE.GAME;
		CLK = 0;

		//Initialize map
		initMap();

		setTankLocation();

		redTank = new Tank(redTankX, redTankY, "RED", mapMatrix, BLOCK_SIZE);
		blueTank = new Tank(blueTankX, blueTankY, "BLUE", mapMatrix, BLOCK_SIZE);
	}

	public void setTankLocation() {

		playable = false;

		while (playable == false) {
			while (true) {
				int newX = randomNumber.nextInt(blockNumber); 
				int newY = randomNumber.nextInt(blockNumber);
				if (mapMatrix[newX][newY] == 0) {
					redTankX = newX * BLOCK_SIZE;
					redTankY = newY * BLOCK_SIZE;
					break;
				}
			}
			while (true) {
				int newX = randomNumber.nextInt(blockNumber); 
				int newY = randomNumber.nextInt(blockNumber);
				if (mapMatrix[newX][newY] == 0) {
					blueTankX = newX * BLOCK_SIZE;
					blueTankY = newY * BLOCK_SIZE;
					break;
				}
			}
			
			if (blueTankX<redTankX && blueTankY<redTankY)
				checkPlayable1(blueTankX/BLOCK_SIZE,blueTankY/BLOCK_SIZE);
			if (blueTankX>redTankX && blueTankY>redTankY)
				checkPlayable2(redTankX/BLOCK_SIZE,redTankY/BLOCK_SIZE);
			if (blueTankX>redTankX && blueTankY<redTankY)
				checkPlayable3(blueTankX/BLOCK_SIZE,blueTankY/BLOCK_SIZE);
			if (blueTankX<redTankX && blueTankY>redTankY)
				checkPlayable4(redTankX/BLOCK_SIZE,redTankY/BLOCK_SIZE);
		}

	}

	public void checkPlayable1(int x, int y) {
		System.out.println("1");
		int matrixSize = mapMatrix[0].length;
		if (x==matrixSize-1 || y==matrixSize-1)
			return;
		
		if (mapMatrix[x+1][y] == 0)
			checkPlayable1(x+1, y);
		if (mapMatrix[x][y+1] == 0)
			checkPlayable1(x, y+1);
		int currentX = x*BLOCK_SIZE;
		int currentY = y*BLOCK_SIZE;
		if (currentX==redTankX && currentY==redTankY) {
			playable = true;
			return;
		}
	}
	
	public void checkPlayable2(int x, int y) {
		System.out.println("2");
		int matrixSize = mapMatrix[0].length;
		if (x==matrixSize-1 || y==matrixSize-1)
			return;
		
		if (mapMatrix[x+1][y] == 0)
			checkPlayable2(x+1, y);
		if (mapMatrix[x][y+1] == 0)
			checkPlayable2(x, y+1);
		int currentX = x*BLOCK_SIZE;
		int currentY = y*BLOCK_SIZE;
		if (currentX==blueTankX && currentY==blueTankY) {
			playable = true;
			return;
		}
	}
	
	public void checkPlayable3(int x, int y) {
		System.out.println("3");
		int matrixSize = mapMatrix[0].length;
		if (x==0 || y==matrixSize-1)
			return;
		
		if (mapMatrix[x-1][y] == 0)
			checkPlayable3(x-1, y);
		if (mapMatrix[x][y+1] == 0)
			checkPlayable3(x, y+1);
		int currentX = x*BLOCK_SIZE;
		int currentY = y*BLOCK_SIZE;
		if (currentX==redTankX && currentY==redTankY) {
			playable = true;
			return;
		}
	}
	
	public void checkPlayable4(int x, int y) {
		System.out.println("4");
		int matrixSize = mapMatrix[0].length;
		if (x==0 || y==matrixSize-1)
			return;
		
		if (mapMatrix[x-1][y] == 0)
			checkPlayable4(x-1, y);
		if (mapMatrix[x][y+1] == 0)
			checkPlayable4(x, y+1);
		int currentX = x*BLOCK_SIZE;
		int currentY = y*BLOCK_SIZE;
		if (currentX==blueTankX && currentY==blueTankY) {
			playable = true;
			return;
		}
	}
	
	public void initMap() {
		//Block image
		String path = "/resource/block.png";
		ImageIcon ii = new ImageIcon(getClass().getResource(path));
		blockImage = ii.getImage();
		BLOCK_SIZE = blockImage.getHeight(null);
		blockNumber = WIDTH/BLOCK_SIZE;

		//Create matrix
		int blockNumber = WIDTH/BLOCK_SIZE;
		mapMatrix = new int[blockNumber][blockNumber];
		for (int i=0;i<blockNumber;i++)
			for (int j=0; j<blockNumber; j++) {
				int randomValue = randomNumber.nextInt(2);
				mapMatrix[i][j] = randomValue;
			}
	}

	//*** Drawing part ***//
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if (state == STATE.GAME)
			paintGame(g2d);

		Toolkit.getDefaultToolkit().sync();
	}

	public void paintGame(Graphics2D g2d) {

		//Draw map
		int blockNumber = WIDTH/BLOCK_SIZE;

		for (int i=0 ; i<blockNumber; i++)
			for (int j=0; j<blockNumber; j++) {
				int blockX = i*BLOCK_SIZE;
				int blockY = j*BLOCK_SIZE;
				if (mapMatrix[i][j] == 1)
					g2d.drawImage(blockImage, blockX, blockY, this);
			}

		//Draw shots
		redShots = redTank.getShots();
		blueShots = blueTank.getShots();

		for (Shot current : redShots)
			g2d.drawImage (current.getImage(), current.getX(), current.getY(), this);
		for (Shot current : blueShots)
			g2d.drawImage (current.getImage(), current.getX(), current.getY(), this);

		//Draw tanks
		Image redTankImage = redTank.getImage();
		Image blueTankImage = blueTank.getImage();

		redTankX = redTank.getX();
		redTankY = redTank.getY();
		blueTankX = blueTank.getX();
		blueTankY = blueTank.getY();

		g2d.drawImage (redTankImage, redTankX, redTankY, this);
		g2d.drawImage (blueTankImage, blueTankX, blueTankY, this);
	}

	//*** Periodic action ***//
	@Override
	public void actionPerformed(ActionEvent e) {

		CLK++;

		//Move tanks
		if (CLK % TANK_PERIOD == 0) {
			redTank.move();
			blueTank.move();
		}

		//Move shots
		redShots = redTank.getShots();
		blueShots = blueTank.getShots();

		for (Shot current : redShots)
			current.move();
		for (Shot current : blueShots)
			current.move();

		repaint();
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (state == STATE.GAME){
				redTank.KeyReleased(e);
				blueTank.KeyReleased(e);
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (state == STATE.GAME) {
				redTank.KeyPressed(e);
				blueTank.KeyPressed(e);
			}
		}

	}

}
