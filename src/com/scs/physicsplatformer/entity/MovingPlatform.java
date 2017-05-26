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
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class MovingPlatform extends Entity implements IDrawable, IProcessable {
	
	private static final float FRICTION = 1f;
	
	private static final int MAX_DIST = 5;

	private Body body;
	private Interval interval = new Interval(1000);

	//Vec2 pos = new Vec2();
	Vec2 dir = new Vec2();
	float dist = 0;
	//float maxDist = 0;		
	
	public MovingPlatform(World world, float x, float y, float w, float h) {
		super(MovingPlatform.class.getSimpleName());
		
		BodyUserData bud = new BodyUserData(this.getClass().getSimpleName(), Color.red, this, true);
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.KINEMATIC, .1f, FRICTION, 1f);
		
		dir.x = 1;
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		//drawableBody.draw(g, system, cam_centre);
		system.drawShape(g, body, cam_centre);
		
	}


	@Override
	public void postprocess() {
		if (interval.hitInterval()) {
			dist += dir.length();
			if(dist > MAX_DIST) {
				dir.mulLocal(-1);
				dist = 0;
			}
			
			body.setLinearVelocity(dir);			

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
