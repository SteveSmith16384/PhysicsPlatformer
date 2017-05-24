package com.scs.physicsplatformer;

import java.awt.Color;

import com.scs.physicsplatformer.entity.Entity;

public class BodyUserData { // For attaching to bodies
	
	public enum Type {Irrelevant, Player, Crate, Floor, Rock, Rope, StickyRope};
	
	public String name;
	public Entity entity;
	public Type type;
	public Color col;
	public boolean canJumpFrom;

	
	public BodyUserData(String _name, Type t, Color c, Entity e, boolean _canJumpFrom) {
		super();
		
		name = _name;
		type = t;
		col = c;
		entity = e;
		canJumpFrom = _canJumpFrom;
	}


	@Override
	public String toString() {
		return name;
	}

}
