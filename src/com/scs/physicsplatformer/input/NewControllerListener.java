package com.scs.physicsplatformer.input;

public interface NewControllerListener {

	void newController(IInputDevice input);

	void controllerRemoved(IInputDevice input);

}
