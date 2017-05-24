package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class Ground extends Entity implements IDrawable {

	private Body body;
	
	public Ground(World world, Vec2[] vertices) {
		super();
		
		BodyUserData bud = new BodyUserData("Ground", BodyUserData.Type.Floor, Color.green, this, true);
		body = JBox2DFunctions.AddComplexRectangle(bud, world, vertices, BodyType.STATIC, .1f, .1f, 0.8f);
	}

	
	public Ground(World world, float x, float y, float w, float h) {
		super();
		
		BodyUserData bud = new BodyUserData("Ground", BodyUserData.Type.Floor, Color.darkGray, this, true);
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.STATIC, .1f, .1f, 0.8f);
	}

	
	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		//drawableBody.draw(g, system, cam_centre);
		system.drawShape(g, body, cam_centre);

	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
