package hr.patrik;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	//Variables
	public int WIDTH;
	public int HEIGTH;
	public int DELAY;
	
	Timer timer;
	
	public Board (int width, int heigth) {
		this.WIDTH = width;
		this.HEIGTH = heigth;
		initBoard();
	}

	public void initBoard() {
		
		//Necessary part
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		
		timer = new Timer(DELAY, this);
		timer.start();
		
		//Global values
		DELAY = 20;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private class TAdapter extends KeyAdapter {
	
		public void KeyPressed (KeyEvent e) {
			
		}
		
		public void KeyReleased (KeyEvent e) {
			
		}
		
	}
	
}
