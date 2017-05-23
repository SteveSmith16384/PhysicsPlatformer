package com.scs.physicsplatformer;

import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

	public BufferStrategy BS;

	public MainWindow() {
		this.setSize(Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.addKeyListener(this);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}


}
