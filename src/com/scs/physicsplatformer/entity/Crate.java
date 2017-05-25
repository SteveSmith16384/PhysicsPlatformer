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

public class Crate extends Entity implements IDrawable {

	private Body drawableBody;
	
	public Crate(World world, float x, float y, float w, float h) {
		super();
		
		BodyUserData bud = new BodyUserData("Crate", BodyUserData.Type.Crate, Color.darkGray, this, true);
		drawableBody = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.DYNAMIC, .1f, .2f, 1f);
		//drawableBody = new DrawableBody(crate);
		
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		system.drawShape(g, drawableBody, cam_centre);
		//drawableBody.draw(g, system, cam_centre);
		
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}