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

public class Trampoline extends PhysicalEntity implements IDrawable {

	private static final float FRICTION = 1f;
	
	public Trampoline(PhysicsPlatformer_Main main, World world, float x, float y, float w, float h) {
		super(main, Trampoline.class.getSimpleName());
		
		BodyUserData bud = new BodyUserData("Trampoline", Color.pink, this, true);
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.STATIC, 1f, FRICTION, 0.8f);
		//drawableBody = new DrawableBody(ground);
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
