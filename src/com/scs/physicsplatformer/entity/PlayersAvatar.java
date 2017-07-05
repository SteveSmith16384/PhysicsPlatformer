package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.ContactEdge;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.PhysicsPlatformer_Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.ICollideable;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IPlayerControllable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;
import com.scs.physicsplatformer.input.IInputDevice;

public class PlayersAvatar extends PhysicalEntity implements IPlayerControllable, IDrawable, ICollideable, IProcessable {

	private static final int GRENADE_INT = 2000;
	public static final float RAD = 0.6f;
	private static final float MAX_VELOCITY = 5;//7f;	

	public IInputDevice input;
	private boolean isOnGround = false;
	private long lastJumpTime = 0;
	private boolean jetpac = false;
	private Fixture feetFixture;
	private BufferedImage imgl, imgr, img;
	private long lastGrenadeTime;

	public PlayersAvatar(IInputDevice _input, PhysicsPlatformer_Main main, World world, float x, float y) {
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
		feetFixture.setUserData(bud2);
		feetFixture.setSensor(true);

		float imgW = RAD * Statics.LOGICAL_TO_PIXELS * 1.8f;
		float imgH = RAD * Statics.LOGICAL_TO_PIXELS * 2.2f;
		imgl = Statics.img_cache.getImage("ninja0_l0", imgW, imgH);
		imgr = Statics.img_cache.getImage("ninja0_r0", imgW, imgH);
		img = imgl;
	}


	@Override
	public void processInput() {
		if (input.isLeftPressed()) {
			img = imgl;
			Vec2 vel = body.getLinearVelocity();
			if (vel.x > -MAX_VELOCITY) {
				//Statics.p("Left pressed");
				Vec2 force = new Vec2();
				force.x = -Statics.PLAYER_MOVE_FORCE;
				body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
			} else {
				Statics.p("Max speed reached");
			}
		} else if (input.isRightPressed()) {
			img = imgr;
			Vec2 vel = body.getLinearVelocity();
			if (vel.x < MAX_VELOCITY) {
				Vec2 force = new Vec2();
				force.x = Statics.PLAYER_MOVE_FORCE;//20f;
				body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
			} else {
				Statics.p("Max speed reached");
			}
		}
		if (input.isJumpPressed()) {
			if (jetpac || isOnGround) {
				// Did we just jump?
				if (System.currentTimeMillis() - lastJumpTime > 100) {
					Vec2 force = new Vec2();
					force.y = -Statics.PLAYER_JUMP_FORCE/2;//20f;

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
		if (input.isThrowPressed()) {
			if (System.currentTimeMillis() > lastGrenadeTime + GRENADE_INT) {
				Grenade grenade = new Grenade(main, this.body.getWorldCenter(), 1);
				main.addEntity(grenade);
				lastGrenadeTime = System.currentTimeMillis();
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
		//Statics.p("Player collided with " + f.getBody());
		BodyUserData bud = (BodyUserData)f.getBody().getUserData();
		if (bud != null) {
			Statics.p("Player collided with " + bud.name + " at " + contact.getTangentSpeed());
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
