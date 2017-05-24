package com.scs.physicsplatformer.entity.components;

import org.jbox2d.dynamics.Body;

import com.scs.physicsplatformer.entity.Entity;

public interface ICollideable {

	void collided(Entity other, Body body);
}
