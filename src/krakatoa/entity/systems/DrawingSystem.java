package krakatoa.entity.systems;

import java.awt.Graphics;

import krakatoa.entity.components.IDrawable;

public class DrawingSystem {

	public DrawingSystem() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void process(Graphics g, IDrawable d) {
		d.draw(g);
	}


}
