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

public class MovingPlatform extends Entity implements IDrawable, IProcessable {
	
	private static final int MAX_MOVES = 50;

	private Body body;
	private Interval interval = new Interval(100);
	private int leftCount;
	
	public MovingPlatform(World world, float x, float y, float w, float h) {
		super();
		
		BodyUserData bud = new BodyUserData(this.getClass().getSimpleName(), BodyUserData.Type.Irrelevant, Color.red, this);
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.STATIC, .1f, .2f, 1f);
		
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		//drawableBody.draw(g, system, cam_centre);
		system.drawShape(g, body, cam_centre);
		
	}


	@Override
	public void process() {
		if (interval.hitInterval()) {
			Vec2 pos = body.getTransform().p;
			if (leftCount > 0) {
				leftCount--;
				pos.x++;
			} else {
				
			}
			body.setTransform(pos, 0);
		}
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
