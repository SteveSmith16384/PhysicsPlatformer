package com.scs.physicsplatformer.entity;

import java.awt.Graphics;

import org.jbox2d.common.Vec2;

import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IPlayerControllable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;
import com.scs.physicsplatformer.input.IInputDevice;

public class PlayersAvatar implements IPlayerControllable, IDrawable {

	private IInputDevice input;
	private DrawableBody body; 

	public PlayersAvatar(IInputDevice _input, DrawableBody _body) {
		super();

		input = _input;
		body = _body;
	}
	

	@Override
	public void process() {
		if (input.isLeftPressed()) {
			Vec2 force = new Vec2();
			//force.y = (float)Math.cos(chopper.getAngle()) * -1;
			force.x = -10f;//(float)Math.sin(chopper.getAngle());
			//force.mulLocal(ROTOR_FORCE);
			body.body.applyForceToCenter(force);//, v);
		} else if (input.isRightPressed()) {
			Vec2 force = new Vec2();
			//force.y = (float)Math.cos(chopper.getAngle()) * -1;
			force.x = 10f;//(float)Math.sin(chopper.getAngle());
			//force.mulLocal(ROTOR_FORCE);
			body.body.applyForceToCenter(force);//, v);
		}

	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		body.draw(g, system, cam_centre);
		
	}


}
