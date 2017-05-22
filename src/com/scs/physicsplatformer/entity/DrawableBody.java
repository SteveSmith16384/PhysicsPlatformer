package com.scs.physicsplatformer.entity;

import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class DrawableBody implements IDrawable {

	public Body body;
	//private BodyUserData data;
	
	public DrawableBody(Body _body) {//, BodyUserData _data) {
		super();
		
		body = _body;
		//data = _data;
	}

	
	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		system.drawShape(g, body, cam_centre);
		
	}
	
	
	@Override
	public String toString() {
		return this.body.getUserData().toString();
	}
	
}

