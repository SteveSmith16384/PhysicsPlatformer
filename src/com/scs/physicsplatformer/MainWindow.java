package com.scs.physicsplatformer;

import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public BufferStrategy BS;

	public MainWindow(PhysicsPlatformer_Main main) {
		this.setSize(Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		this.setVisible(true);
		this.addKeyListener(main);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}

}
