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
	private int WIDTH ;
	private int HEIGTH;
	private int DELAY = 20;
	private int BLOCK_SIZE;
	private int blockNumber;

	private STATE state;

	private int redTankX;
	private int redTankY;
	private int blueTankX;
	private int blueTankY;

	private int[][] mapMatrix;

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

		//Initialize map
		initMap();

		setTankLocation("RED");
		setTankLocation("BLUE");

		redTank = new Tank(redTankX, redTankY, "RED", mapMatrix, BLOCK_SIZE);
		blueTank = new Tank(blueTankX, blueTankX, "BLUE", mapMatrix, BLOCK_SIZE);
	}

	public void setTankLocation(String colour) {

		if (colour.equals("RED"))
		for (int i=0; i<blockNumber; i++)
			for (int j=0; j<blockNumber; j++) {
				if (mapMatrix[i][j] == 0) {
					redTankX = i*BLOCK_SIZE;
					redTankY = j*BLOCK_SIZE;
					return;
				}
			}
		if (colour.equals("BLUE"))
			for (int i=blockNumber-2;i>0;i--)
				for (int j=blockNumber-2; j>0; j--) {
					if (mapMatrix[i][j] == 0) {
						blueTankX = i*BLOCK_SIZE;
						blueTankY = j*BLOCK_SIZE;
						return;
					}
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

		//Move tanks
		redTank.move();
		blueTank.move();

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
