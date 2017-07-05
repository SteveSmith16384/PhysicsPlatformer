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

public class Crate extends PhysicalEntity implements IDrawable {

	public Crate(PhysicsPlatformer_Main main, World world, float x, float y, float w, float h, float weight) {
		super(main, Crate.class.getSimpleName());
		
		BodyUserData bud = new BodyUserData("Crate", Color.cyan, this, true);
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.DYNAMIC, .1f, .5f, weight);
	
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		system.drawShape(tmpPoint, g, body, cam_centre);
		
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
