package com.scs.physicsplatformer;

import java.awt.Color;

import com.scs.physicsplatformer.entity.Entity;

public class BodyUserData { // For attaching to bodies
	
	public String name;
	public Entity entity;
	public Color col;
	public boolean canJumpFrom;
	public boolean isFeet = false;

	
	public BodyUserData(String _name, Color c, Entity e, boolean _canJumpFrom) {
		super();
		
		name = _name;
		col = c;
		entity = e;
		canJumpFrom = _canJumpFrom;
	}


	@Override
	public String toString() {
		return name;
	}

}
