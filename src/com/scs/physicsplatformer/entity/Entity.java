package com.scs.physicsplatformer.entity;

import java.awt.Point;

import com.scs.physicsplatformer.Main;

public abstract class Entity { 
	
	public int id;
	private static int next_id = 0;
	public Point tmpPoint = new Point();

	protected Main main;
	public String name;
	
	public Entity(Main _main, String _name) {
		super();
		
		main = _main;
		id = next_id++;
		name =_name;
	}
	
	
	@Override
	public String toString() {
		return name;
	}
	

}
