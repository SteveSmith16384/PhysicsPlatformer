package com.scs.physicsplatformer.entity.components;

import com.scs.physicsplatformer.entity.Entity;

public interface ICollideable {

	void collided(Entity other);
}
