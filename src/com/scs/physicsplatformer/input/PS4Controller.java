package com.scs.physicsplatformer.input;

import org.gamepad4j.ButtonID;
import org.gamepad4j.IController;
import org.gamepad4j.StickID;
import org.gamepad4j.StickPosition;

public final class PS4Controller implements IInputDevice {

	private IController gamepad;

	public PS4Controller(IController _gamepad) {
		gamepad = _gamepad;
	}


	@Override
	public boolean isLeftPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		//return pos.getDirection() == DpadDirection.LEFT;
		//Statics.p("Left=" + pos.getDegree());
		return pos.getDegree() > 252 && pos.getDegree() < 360; // 90=right, 270=left

	}


	@Override
	public boolean isRightPressed() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		//return pos.getDirection() == DpadDirection.RIGHT;
		return pos.getDegree() > 17 && pos.getDegree() < 152; // 90=right, 270=left
	}


	@Override
	public boolean isJumpPressed() {
		return gamepad.isButtonPressed(ButtonID.FACE_DOWN);
	}


	@Override
	public float getStickDistance() {
		StickPosition pos = gamepad.getStick(StickID.LEFT).getPosition();
		/*if (Statics.DEBUG) {
			Statics.p("Dist=" + pos.getDistanceToCenter());
		}*/
		return pos.getDistanceToCenter();
	}

	
}
