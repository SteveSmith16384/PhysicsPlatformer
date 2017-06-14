package com.scs.physicsplatformer.input;

public interface IInputDevice {

	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isJumpPressed();

	boolean isFirePressed();

	float getStickDistance();
	
}
