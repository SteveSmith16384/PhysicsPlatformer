package com.scs.physicsplatformer.entity;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.entity.components.IDrawable;

public abstract class PhysicalEntity extends Entity implements IDrawable {

	public Body body;
	
	public PhysicalEntity(Main _main, String _name) {
		super(_main, _name);
	}


	@Override
	public void cleanup(World world) {
		world.destroyBody(body);
	}

}
