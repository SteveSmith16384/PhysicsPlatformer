package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import ssmith.lang.Functions;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.PhysicsPlatformer_Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class ExplosionShard extends PhysicalEntity implements IDrawable, IProcessable {

	private long removeTime = 3000;

	public ExplosionShard(PhysicsPlatformer_Main main, Vec2 pos) {
		super(main, ExplosionShard.class.getSimpleName());

		BodyUserData bud = new BodyUserData("ExplosionShard", Color.red, this, false);
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, .1f, BodyType.DYNAMIC, .01f, 1f, 100f);
		
		Vec2 force = new Vec2(Functions.rndFloat(-10, 10), Functions.rndFloat(-10, 10));
		force.normalize();
		force.mulLocal(100);
		body.setBullet(true);
		body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
	}

	
	@Override
	public void preprocess(long interpol) {
		removeTime -= interpol;
		if (removeTime <= 0) {
			main.removeEntity(this);
		}		
	}

	@Override
	public void postprocess(long interpol) {
		
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		system.drawShape(tmpPoint, g, body, cam_centre);
	}

}
