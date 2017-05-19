package krakatoa;

import java.awt.Color;

public class MyUserData { // For attaching to bodies
	
	public enum Type {Player, Crate, Floor, Rock, Rope, StickyRope, Tanker};
	
	public String name; // todo
	public Type type;
	public Color col;
	//public Body body;

	public MyUserData(String _name, Type t, Color c) {
		name = _name;
		type = t;
		col = c;
	}

}
