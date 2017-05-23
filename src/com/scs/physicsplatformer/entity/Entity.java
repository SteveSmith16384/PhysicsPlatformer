package com.scs.physicsplatformer.entity;

public abstract class Entity { 
	
	public int id;
	private static int next_id = 0;

	public Entity() {
		id = next_id++;
	}
	

}
