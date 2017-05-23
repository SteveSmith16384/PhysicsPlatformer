package com.scs.physicsplatformer.input;

public interface IInputDevice {

	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isJumpPressed();

	float getStickDistance();
	
}
