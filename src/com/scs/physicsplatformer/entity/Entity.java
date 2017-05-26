package com.scs.physicsplatformer.entity;

public abstract class Entity { 
	
	public int id;
	private static int next_id = 0;

	public String name;
	
	public Entity(String _name) { // todo - add name
		super();
		
		id = next_id++;
		name =_name;
	}
	

}
