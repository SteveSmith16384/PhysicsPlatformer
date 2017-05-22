package com.scs.physicsplatformer.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class KeyboardInput implements IInputDevice, KeyListener {

	private volatile boolean left, right, jump, up, down, fire;
	private boolean lastMoveWasLeft = false;
	private int id;

	public KeyboardInput(JFrame frame, int _id) {
		super();

		id = _id;
		frame.addKeyListener(this);
	}


	@Override
	public boolean isLeftPressed() {
		if (left) {
			lastMoveWasLeft = true;
		}
		return left;
	}


	@Override
	public boolean isRightPressed() {
		if (right) {
			lastMoveWasLeft = false;
		}
		return right;
	}


	@Override
	public boolean isJumpPressed() {
		return jump;
	}


	@Override
	public void keyPressed(KeyEvent ke) {
		if (this.id == 1) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				left = true;
				break;

			case KeyEvent.VK_RIGHT:
				right = true;
				break;

			case KeyEvent.VK_UP:
				up = true;
				jump = true;
				break;

			case KeyEvent.VK_DOWN:
				down = true;
				break;

			case KeyEvent.VK_CONTROL:
				fire = true;
				//firePressedTime = System.currentTimeMillis();
				break;
			}
		} else if (id == 2) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_A:
				left = true;
				break;

			case KeyEvent.VK_D:
				right = true;
				break;

			case KeyEvent.VK_W:
				up = true;
				jump = true;
				break;

			case KeyEvent.VK_S:
				down = true;
				break;

			case KeyEvent.VK_SPACE:
				fire = true;
				//firePressedTime = System.currentTimeMillis();
				break;
			}

		}

	}


	@Override
	public void keyReleased(KeyEvent ke) {
		if (this.id == 1) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				left = false;
				break;

			case KeyEvent.VK_RIGHT:
				right = false;
				break;

			case KeyEvent.VK_UP:
				up = false;
				jump = false;
				break;

			case KeyEvent.VK_DOWN:
				down = false;
				break;

			case KeyEvent.VK_SPACE:
				fire = false;
				//this.duration = System.currentTimeMillis() - this.firePressedTime;
				break;

			}

		} else if (id == 2) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_A:
				left = false;
				break;

			case KeyEvent.VK_D:
				right = false;
				break;

			case KeyEvent.VK_W:
				up = false;
				jump = false;
				break;

			case KeyEvent.VK_S:
				down = false;
				break;

			case KeyEvent.VK_SPACE:
				fire = false;
				//firePressedTime = System.currentTimeMillis();
				break;
			}

		}
	}


	@Override
	public void keyTyped(KeyEvent ke) {
		// Do nothing
	}


	@Override
	public float getStickDistance() {
		return 1;
	}

}
