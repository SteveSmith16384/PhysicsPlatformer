package com.scs.physicsplatformer;

import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainWindow extends JFrame {//implements KeyListener {

	public BufferStrategy BS;
//	private boolean keys[] = new boolean[256];

	public MainWindow() {
		this.setSize(Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.addKeyListener(this);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}

/*
	@Override
	public void keyPressed(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = true;

	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = false;

	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}*/


}
