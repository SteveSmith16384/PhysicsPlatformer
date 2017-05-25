package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.ICollideable;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IPlayerControllable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;
import com.scs.physicsplatformer.input.IInputDevice;

public class PlayersAvatar extends Entity implements IPlayerControllable, IDrawable, ICollideable {//, IProcessable {

	final static float MAX_VELOCITY = 5;//7f;		

	private IInputDevice input;
	private Body body;
	public boolean canJump = false;

	public PlayersAvatar(IInputDevice _input, World world, float x, float y) {
		super();

		input = _input;

		BodyUserData bud = new BodyUserData("Player", BodyUserData.Type.Player, Color.black, this, false);
		//Body body = JBox2DFunctions.AddRectangle(bud, world, 50, 10, 4, 4, BodyType.DYNAMIC, .2f, .2f, .4f);
		body = JBox2DFunctions.AddCircle(bud, world, x, y, .5f, BodyType.DYNAMIC, .1f, .6f, .5f);
		body.setFixedRotation(true);
		body.setBullet(true);

	}


	@Override
	public void processInput() {
		/*if (this.canJump) {
			Statics.p("Can Jump");
		}*/

		if (input.isLeftPressed()) {
			Vec2 vel = body.getLinearVelocity();
			if (vel.x > -MAX_VELOCITY) {
				//Statics.p("Left pressed");
				Vec2 force = new Vec2();
				force.x = -Statics.PLAYER_FORCE;//20f;
				//body.body.applyForceToCenter(force);//, v);
				body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
				//drawableBody.setLinearVelocity(force);
			} else {
				//Statics.p("Max speed reached");
			}
		} else if (input.isRightPressed()) {
			Vec2 vel = body.getLinearVelocity();
			if (vel.x < MAX_VELOCITY) {
				Vec2 force = new Vec2();
				force.x = Statics.PLAYER_FORCE;//20f;
				//body.body.applyForceToCenter(force);//, v);
				body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
				//drawableBody.setLinearVelocity(force);
			} else {
				//Statics.p("Max speed reached");
			}
		}
		if (input.isJumpPressed()) {
			if (canJump) { //this.isOnGround()) {
				//if (canJump) {
				Vec2 force = new Vec2();
				force.y = -Statics.PLAYER_FORCE/2;//20f;//(float)Math.sin(chopper.getAngle());
				//drawableBody.applyForceToCenter(force);//, v);

				// Move slightly up
				Vec2 pos = body.getPosition();
				pos.y += 0.01f;
				body.setTransform(pos, 0);
				body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
				canJump = false; // todo - remove?  unset somehow
			} else {
				//Statics.p("Not on ground");
			}
		}

		Vec2 vel = body.getLinearVelocity();
		// cap max velocity on x		
		if(Math.abs(vel.x) > MAX_VELOCITY) {			
			vel.x = Math.signum(vel.x) * MAX_VELOCITY;
			body.setLinearVelocity(vel);
		}

		//canJump = false; // Set by collision

	}


	/*private boolean isOnGround() {
		ContactEdge list = body.getContactList();
		while (list != null) {
			if (list.contact.isTouching())
			BodyUserData bud = (BodyUserData) list.other.getUserData();
			if (bud.entity instanceof Ground || bud.entity instanceof Trampoline) { // todo - check more
				Statics.p("JUMP!" + bud.entity);
				return true;
			}
			list = list.next;
		}
		return false;
	}*/


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		//drawableBody.draw(g, system, cam_centre);
		system.drawShape(g, body, cam_centre);

	}


	@Override
	public void collided(Entity other, Body body) {
		Statics.p(this.toString() + " Collided!");
		BodyUserData bud = (BodyUserData) body.getUserData();
		if (bud.canJumpFrom) {
			this.canJump = true; // todo - check the platform is below us!
		}
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}


}
