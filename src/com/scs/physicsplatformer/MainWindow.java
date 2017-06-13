package com.scs.physicsplatformer;

import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

	public BufferStrategy BS;

	public MainWindow(Main main) {
		this.setSize(Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		this.setVisible(true);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.addWindowListener(main);
		this.addKeyListener(main);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}





}
