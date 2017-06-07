package com.scs.physicsplatformer;

import com.scs.physicsplatformer.input.IInputDevice;

public class Player {
	
	public int score;
	public IInputDevice input;

	public Player(IInputDevice _input) {
		super();
		
		input = _input;
	}

}
