package com.scs.physicsplatformer.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.JBox2DFunctions;
import com.scs.physicsplatformer.PhysicsPlatformer_Main;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public class HarmfulBlock extends PhysicalEntity implements IDrawable {

	public HarmfulBlock(PhysicsPlatformer_Main main, World world, float x, float y, float w, float h) {
		super(main, HarmfulBlock.class.getSimpleName());
		
		BodyUserData bud = new BodyUserData("HarmfulBlock", Color.red, this, true);
		bud.harmsPlayer= true;
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.DYNAMIC, 0f, .2f, 1f);
		
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		system.drawShape(tmpPoint, g, body, cam_centre);
	
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
