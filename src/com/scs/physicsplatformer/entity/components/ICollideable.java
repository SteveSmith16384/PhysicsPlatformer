package com.scs.physicsplatformer.entity.components;

import org.jbox2d.dynamics.contacts.Contact;

public interface ICollideable {

	void collided(Contact contact, boolean weAreA);
}
