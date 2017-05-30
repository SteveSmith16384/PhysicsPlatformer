package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.ContactEdge;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.ICollideable;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IPlayerControllable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;
import com.scs.physicsplatformer.input.IInputDevice;

public class PlayersAvatar extends Entity implements IPlayerControllable, IDrawable, ICollideable, IProcessable {

	private static final float RAD = 0.5f;
	private static final float MAX_VELOCITY = 5;//7f;	

	private IInputDevice input;
	private Body body;
	private boolean isOnGround = false;
	private long lastJumpTime = 0;
	private boolean jetpac = false; // todo - test
	private Fixture feetFixture;


	public PlayersAvatar(IInputDevice _input, World world, float x, float y) {
		super(PlayersAvatar.class.getSimpleName());

		input = _input;

		BodyUserData bud = new BodyUserData("Player_Body", Color.black, this, false);
		//Body body = JBox2DFunctions.AddRectangle(bud, world, 50, 10, 4, 4, BodyType.DYNAMIC, .2f, .2f, .4f);
		body = JBox2DFunctions.AddCircle(bud, world, x, y, RAD, BodyType.DYNAMIC, .1f, .6f, .5f);
		body.setFixedRotation(true);
		body.setBullet(true);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(RAD/2, RAD/10, new Vec2(0, RAD), 0);

		BodyUserData bud2 = new BodyUserData("Player_Feet", Color.black, this, false);
		bud2.isFeet = true;
		feetFixture = body.createFixture(ps, 0f);
		//Fixture fixture = new Fixture();
		feetFixture.setUserData(bud2);
		feetFixture.setSensor(true);

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
			if (jetpac || isOnGround) { //this.isOnGround()) {
				// Did we just jump?
				if (System.currentTimeMillis() - lastJumpTime > 100) {
					Vec2 force = new Vec2();
					force.y = -Statics.PLAYER_FORCE/2;//20f;//(float)Math.sin(chopper.getAngle());
					//drawableBody.applyForceToCenter(force);//, v);

					// Move slightly up
					Vec2 pos = body.getPosition();
					pos.y += 0.01f;
					body.setTransform(pos, 0);
					body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);

					lastJumpTime = System.currentTimeMillis();
				}
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
		//this.feetFixture;
	}


	@Override
	public void collided(Contact contact, boolean weAreA) {
		/*Statics.p(this.toString() + " Collided!");
		Fixture feet = null; 
		Fixture platform = null;
		if (weAreA) {
			feet = contact.m_fixtureA;
			platform = contact.m_fixtureB;
		} else {
			feet = contact.m_fixtureB;
			platform = contact.m_fixtureA;
		}
		BodyUserData feetBUD = (BodyUserData) feet.getUserData();
		if (feetBUD != null && feetBUD.isFeet) {
			BodyUserData groundBUD = (BodyUserData) platform.getUserData();
			if (groundBUD != null && groundBUD.canJumpFrom) {
				Statics.p(this.toString() + " isOnGround!");
				this.isOnGround = true;
			}			
		}*/


		//BodyUserData bud = (BodyUserData) body.getUserData();
		//if (bud.canJumpFrom) {
		/*Vec2 pos = body.getPosition();
			Manifold manifold = contact.getManifold();
			boolean below = true;
			for(int j = 0; j < manifold.pointCount ; j++) {
				ManifoldPoint p = manifold.points[j]; 
				if (p.localPoint.x != 0 || p.localPoint.y != 0) {
					Statics.p("Here!");
				}
				below &= (p.localPoint.y < pos.y - 0.5f);
			}
			this.canJump = below;*/
		//this.isOnGround = true;
		//	}
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}


	@Override
	public void preprocess() {

	}


	@Override
	public void postprocess() {
		isOnGround = false;

		ContactEdge edge = feetFixture.getBody().getContactList();
		while (edge != null) {
			Contact contact = edge.contact;
			if (contact.isTouching()) {
				BodyUserData feetBUD = (BodyUserData) contact.m_fixtureA.getUserData();
				BodyUserData groundBUD = (BodyUserData) contact.m_fixtureB.getUserData();
				if (feetBUD != null && groundBUD != null) {
					if (feetBUD.isFeet || groundBUD.isFeet) {
						if (feetBUD.canJumpFrom || groundBUD.canJumpFrom) {
							Statics.p(this.toString() + " isOnGround!");
							this.isOnGround = true;
							break;
						}			
					}
				}
			}

			edge = edge.next;
		}
	}


}
