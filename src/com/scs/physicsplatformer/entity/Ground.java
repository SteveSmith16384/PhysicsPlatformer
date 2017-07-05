package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.PhysicsPlatformer_Main;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class Ground extends PhysicalEntity implements IDrawable {

	private static final float FRICTION = 0.8f;
	
	/*public Ground(String name, World world, Vec2[] vertices) {
		super(name);
		
		BodyUserData bud = new BodyUserData("Ground", Color.green, this, true);
		body = JBox2DFunctions.AddComplexRectangle(bud, world, vertices, BodyType.STATIC, .1f, FRICTION, 0.8f);
		
		//body.sett
	}*/

	
	public Ground(PhysicsPlatformer_Main main, String name, World world, float cx, float cy, float w, float h, Color c, float bounciness) {
		super(main, name);
		
		BodyUserData bud = new BodyUserData("Ground", c, this, true);
		body = JBox2DFunctions.AddRectangle(bud, world, cx, cy, w, h, BodyType.STATIC, bounciness, FRICTION, 0.8f);
	}

	
	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		//drawableBody.draw(g, system, cam_centre);
		system.drawShape(tmpPoint, g, body, cam_centre);

	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
