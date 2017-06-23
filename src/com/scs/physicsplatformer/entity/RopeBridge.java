package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.RopeJointDef;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class RopeBridge extends Entity implements IDrawable {
	
	private List<Body> bodies = new ArrayList<>();

	public RopeBridge(Main _main, float x, float y, int segments) {
		super(_main, "RopeBridge");
		
		addRopeShape(main.world, x, y, segments);
	}


	private void addRopeShape(World world, float sx, float y, int segments) {
		BodyUserData bud = new BodyUserData("RopeBridge", Color.yellow, this, true);

		PolygonShape ropeShape = new PolygonShape();
		ropeShape.setAsBox(0.5f, 0.125f);

		FixtureDef fd = new FixtureDef();
		fd.shape = ropeShape;
		fd.density = 2.0f;
		fd.friction = 0.2f;
		fd.filter.categoryBits = 0x0001;
		fd.filter.maskBits = 0xFFFF & ~0x0002;

		RevoluteJointDef jd = new RevoluteJointDef();
		jd.collideConnected = false;

		//final float y = sy;//15.0f;
		RopeJointDef m_ropeDef = new RopeJointDef();
		m_ropeDef.localAnchorA.set(0.0f, y);

		Body prevBody = null;
		for (int i = 0; i < segments; ++i) {
			BodyDef bd = new BodyDef();
			if (i == 0) {
				bd.type = BodyType.STATIC;
			} else {
				bd.type = BodyType.DYNAMIC;
			}
			bd.position.set(sx + i, y);

			if (i == segments - 1) {
				// Create anchor box
				ropeShape.setAsBox(1.5f, 1.5f);
				fd.density = 100.0f;
				fd.filter.categoryBits = 0x0002;
				bd.position.set(sx + i, y);
				bd.angularDamping = 0.4f;
				bd.type = BodyType.STATIC;
			}

			Body body = world.createBody(bd);
			body.setUserData(bud);
			bodies.add(body);
			
			body.createFixture(fd);

			Vec2 anchor = new Vec2(sx+i, y);
			if (prevBody != null) {
				jd.initialize(prevBody, body, anchor);
				world.createJoint(jd);
			}

			prevBody = body;
		}

		float extraLength = 0.01f;
		m_ropeDef.maxLength = segments - 1.0f + extraLength;
		m_ropeDef.bodyB = prevBody;
	}


	@Override
	public void cleanup(World world) {
		for (Body body : bodies) {
			world.destroyBody(body);
		}

	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		for (Body body : bodies) {
			system.drawShape(tmpPoint, g, body, cam_centre);
		}
		
	}



}
