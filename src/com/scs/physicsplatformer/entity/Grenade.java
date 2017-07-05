package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.PhysicsPlatformer_Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class Grenade extends PhysicalEntity implements IDrawable, IProcessable {
	
	private long explode = 3000;

	public Grenade(PhysicsPlatformer_Main main, Vec2 pos, int dir) {
		super(main, PlayersAvatar.class.getSimpleName());

		BodyUserData bud = new BodyUserData("Grenade", Color.yellow, this, false);
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, .1f, BodyType.DYNAMIC, .1f, .2f, 1f);
		
		Vec2 force = new Vec2(0, -1);
		if (dir == 1) {
			force.x = -1;
		} else {
			force.x = 1;
		}
		force.normalize();
		body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
	}

	
	@Override
	public void preprocess(long interpol) {
		// Explode?
		explode -= interpol;
		if (explode <= 0) {
			for (int i=0 ; i<30 ; i++) {
				ExplosionShard shard = new ExplosionShard(main, this.body.getWorldCenter());
				main.addEntity(shard);
			}
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
