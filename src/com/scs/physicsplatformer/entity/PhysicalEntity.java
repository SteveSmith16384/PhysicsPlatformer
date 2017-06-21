package com.scs.physicsplatformer.entity;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;

public class PhysicalEntity extends Entity {

	public Body body;
	
	public PhysicalEntity(Main _main, String _name) {
		super(_main, _name);
	}


	@Override
	public void cleanup(World world) {
		world.destroyBody(body);
	}

}
