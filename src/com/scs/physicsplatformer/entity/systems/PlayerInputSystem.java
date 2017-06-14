package com.scs.physicsplatformer.entity.systems;

import com.scs.physicsplatformer.entity.components.IPlayerControllable;

public class PlayerInputSystem {

	public PlayerInputSystem() {
	}


	public void process(IPlayerControllable avatar) {
		avatar.processInput();
	}
	
}
