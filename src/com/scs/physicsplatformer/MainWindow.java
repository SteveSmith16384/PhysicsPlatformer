package com.scs.physicsplatformer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainWindow extends JFrame implements KeyListener, WindowListener {

	public BufferStrategy BS;

	public MainWindow() {
		this.setSize(Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	
	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.setVisible(false);
			//this.dispose();
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.dispose();
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}


}
