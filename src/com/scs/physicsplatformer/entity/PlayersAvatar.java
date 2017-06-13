package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.ICollideable;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IPlayerControllable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;
import com.scs.physicsplatformer.input.IInputDevice;

public class PlayersAvatar extends PhysicalEntity implements IPlayerControllable, IDrawable, ICollideable, IProcessable {

	public static final float RAD = 0.5f;
	private static final float MAX_VELOCITY = 5;//7f;	

	private IInputDevice input;
	private boolean isOnGround = false;
	private long lastJumpTime = 0;
	private boolean jetpac = false;
	private Fixture feetFixture;
	private BufferedImage imgl, imgr, img;

	public PlayersAvatar(IInputDevice _input, Main main, World world, float x, float y) {
		super(main, PlayersAvatar.class.getSimpleName());

		input = _input;

		BodyUserData bud = new BodyUserData("Player_Body", Color.black, this, false);
		//Body body = JBox2DFunctions.AddRectangle(bud, world, 50, 10, 4, 4, BodyType.DYNAMIC, .2f, .2f, .4f);
		body = JBox2DFunctions.AddCircle(bud, world, x, y, RAD, BodyType.DYNAMIC, .1f, .6f, .5f);
		body.setFixedRotation(true);
		body.setBullet(true);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(RAD/2, RAD/10, new Vec2(0, RAD), 0);

		BodyUserData bud2 = new BodyUserData("Player_Feet", null, this, false);
		bud2.isFeet = true;
		feetFixture = body.createFixture(ps, 0f);
		//Fixture fixture = new Fixture();
		feetFixture.setUserData(bud2);
		feetFixture.setSensor(true);

		imgl = Statics.img_cache.getImage("ninja0_l0", RAD * Statics.LOGICAL_TO_PIXELS*2, RAD * Statics.LOGICAL_TO_PIXELS*2);
		imgr = Statics.img_cache.getImage("ninja0_r0", RAD * Statics.LOGICAL_TO_PIXELS*2, RAD * Statics.LOGICAL_TO_PIXELS*2);
		img = imgl;
	}


	@Override
	public void processInput() {
		/*if (this.canJump) {
			Statics.p("Can Jump");
		}*/

		if (input.isLeftPressed()) {
			img = imgl;
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
			img = imgr;
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

	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		//system.drawShape(g, body, cam_centre);
		system.drawImage(tmpPoint, img, g, body, cam_centre);
	}


	@Override
	public void collided(Contact contact, boolean weAreA) {
		Fixture f = null;
		if (weAreA) {
			f = contact.getFixtureB();
		} else {
			f = contact.getFixtureA();
		}
		//Statics.p("Player collided with " + f);
		BodyUserData bud = (BodyUserData)f.getUserData();
		if (bud != null) {
			if (bud.harmsPlayer) {
				Statics.p("Death!");
				main.restartAvatar(this);
			} else if (bud.endOfLevel) {
				main.removeEntity(this);
				//main.nextLevel(); NO as there might be other avatars!
			}
		}
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}


	@Override
	public void preprocess(long interpol) {

	}


	@Override
	public void postprocess(long interpol) {
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
							//Statics.p(this.toString() + " isOnGround!");
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
