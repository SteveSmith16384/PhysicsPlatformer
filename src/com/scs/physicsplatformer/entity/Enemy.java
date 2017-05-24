package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import ssmith.util.Interval;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class Enemy extends Entity implements IDrawable, IProcessable {

	private Body drawableBody;
	private Interval interval = new Interval(1000);
	
	public Enemy(World world, float x, float y, float w, float h) {
		super();
		
		BodyUserData bud = new BodyUserData(this.getClass().getSimpleName(), BodyUserData.Type.Irrelevant, Color.red, this, false);
		drawableBody = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.DYNAMIC, .1f, .2f, 1f);
		//drawableBody = new DrawableBody(crate);
		
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		//drawableBody.draw(g, system, cam_centre);
		system.drawShape(g, drawableBody, cam_centre);
		
	}


	@Override
	public void postprocess() {
		if (interval.hitInterval()) {
			Vec2 force = new Vec2();
			force.y = -Statics.PLAYER_FORCE;//20f;//(float)Math.sin(chopper.getAngle());
			drawableBody.applyLinearImpulse(force, Statics.VEC_CENTRE, true);

		}
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}



	@Override
	public void preprocess() {
		// Do nothing
		
	}

}
