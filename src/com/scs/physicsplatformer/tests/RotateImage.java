package com.scs.physicsplatformer.tests;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class RotateImage extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private BufferStrategy BS;

	public static void main(String[] args) {
		RotateImage hw = new RotateImage();
		hw.run();
	}


	public RotateImage() {
		this.setSize(800, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}


	private void run() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("./res/pig.gif"));
			int i = 0;
			while (true) {
				Graphics g = BS.getDrawGraphics();
				Graphics2D g2d = (Graphics2D)g; 

				int drawLocationX = 300;
				int drawLocationY = 300;

				// Rotation information
				double rotationRequired = Math.toRadians(i+=2);// (45);
				double locationX = image.getWidth() / 2;
				double locationY = image.getHeight() / 2;
				AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

				// Drawing the rotated image at the required drawing locations
				g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
				
				BS.show();

				Thread.sleep(50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}

