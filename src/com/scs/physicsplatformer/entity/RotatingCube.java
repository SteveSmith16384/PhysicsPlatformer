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

public class RotatingCube extends PhysicalEntity implements IDrawable {
	
	private static final float FRICTION = .1f;
	

	public RotatingCube(PhysicsPlatformer_Main main, World world, float x, float y, float w, float h) {
		super(main, RotatingCube.class.getSimpleName());
		
		BodyUserData bud = new BodyUserData(this.getClass().getSimpleName(), Color.cyan, this, true);
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.KINEMATIC, .1f, FRICTION, .1f);
		
		//body.applyTorque(100f);
		body.setAngularVelocity(1f);
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
