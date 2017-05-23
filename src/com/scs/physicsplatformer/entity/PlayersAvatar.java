package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.ICollideable;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IPlayerControllable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;
import com.scs.physicsplatformer.input.IInputDevice;

public class PlayersAvatar extends Entity implements IPlayerControllable, IDrawable, ICollideable {

	private IInputDevice input;
	private Body body;
	//public boolean canJump = false;

	public PlayersAvatar(IInputDevice _input, World world) {
		super();

		input = _input;

		BodyUserData bud = new BodyUserData("Player", BodyUserData.Type.Player, Color.black, this);
		//Body body = JBox2DFunctions.AddRectangle(bud, world, 50, 10, 4, 4, BodyType.DYNAMIC, .2f, .2f, .4f);
		body = JBox2DFunctions.AddCircle(bud, world, 50, 10, .5f, BodyType.DYNAMIC, .1f, .1f, .5f);
		body.setBullet(true);

	}


	@Override
	public void processInput() {
		if (input.isLeftPressed()) {
			//Statics.p("Left pressed");
			Vec2 force = new Vec2();
			force.x = -Statics.PLAYER_FORCE;//20f;//(float)Math.sin(chopper.getAngle());
			//body.body.applyForceToCenter(force);//, v);
			body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
			//drawableBody.setLinearVelocity(force);
		} else if (input.isRightPressed()) {
			Vec2 force = new Vec2();
			force.x = Statics.PLAYER_FORCE;//20f;//(float)Math.sin(chopper.getAngle());
			//body.body.applyForceToCenter(force);//, v);
			body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
			//drawableBody.setLinearVelocity(force);
		} else if (input.isJumpPressed()) {
			if (this.isOnGround()) {
				Statics.p("JUMP!");
				//if (canJump) {
				Vec2 force = new Vec2();
				force.y = -Statics.PLAYER_FORCE*4;//20f;//(float)Math.sin(chopper.getAngle());
				//drawableBody.applyForceToCenter(force);//, v);
				body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
			}
		}
		//canJump = false; // Set by collision

	}


	private boolean isOnGround() {
		ContactEdge list = body.getContactList();
		while (list != null) {
			BodyUserData bud = (BodyUserData) list.other.getUserData();
			if (bud.entity instanceof Ground || bud.entity instanceof Trampoline) {
				return true;
			}
			list = list.next;
		}
		return false;
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		//drawableBody.draw(g, system, cam_centre);
		system.drawShape(g, body, cam_centre);

	}


	@Override
	public void collided(Entity other) {
		Statics.p(this.toString() + "Collided!");
		//this.canJump = true;
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
