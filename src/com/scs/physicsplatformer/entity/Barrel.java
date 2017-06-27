package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class Barrel extends PhysicalEntity implements IDrawable {

	public Barrel(Main main, World world, float x, float y, float rad, float weight) {
		super(main, "Barrel");
		
		BodyUserData bud = new BodyUserData("Barrel", Color.red, this, true);
		body = JBox2DFunctions.AddCircle(bud, world, x, y, rad, BodyType.DYNAMIC, .1f, .2f, weight);
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
