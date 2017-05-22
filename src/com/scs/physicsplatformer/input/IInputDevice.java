package com.scs.physicsplatformer.input;

public interface IInputDevice {

	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isJumpPressed();

	/*boolean isUpPressed();
	
	boolean isDownPressed();*/
	
	float getStickDistance();
	
	/*boolean isThrowPressed();
	
	int getAngle();*/

}
