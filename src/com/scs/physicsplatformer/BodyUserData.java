package com.scs.physicsplatformer;

import java.awt.Color;

public class BodyUserData { // For attaching to bodies
	
	public enum Type {Player, Crate, Floor, Rock, Rope, StickyRope, Tanker};
	
	public String name;
	public Type type;
	public Color col;

	
	public BodyUserData(String _name, Type t, Color c) {
		name = _name;
		type = t;
		col = c;
	}


	@Override
	public String toString() {
		return name;
	}

}
