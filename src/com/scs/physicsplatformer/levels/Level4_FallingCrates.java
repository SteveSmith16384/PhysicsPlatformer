package com.scs.physicsplatformer.levels;

import java.awt.Color;
import java.awt.Point;

import org.jbox2d.dynamics.World;

import ssmith.lang.Functions;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Crate;
import com.scs.physicsplatformer.entity.EndOfLevel;
import com.scs.physicsplatformer.entity.Ground;

/*
 * Barrels fall from the sky
 */
public class Level4_FallingCrates extends AbstractLevel {

	private static final int BARREL_INTERVAL = 5 * 1000;

	private int nextBarrel = 0;

	public Level4_FallingCrates(Main main) {
		super(main);
	}

	
	@Override
	public Point getPlayerStartPos() {
		return new Point(5, Statics.WORLD_HEIGHT_LOGICAL-5); // todo - move to below to avoid strrating in ground

	}

	
	@Override
	public void createWorld(World world, Main main) {
		super.addFrame(world, main);

		float landWidth = Statics.WORLD_WIDTH_LOGICAL/3;
		float landHeight = Statics.WORLD_WIDTH_LOGICAL/5;

		// Left land
		Ground platform = new Ground(main, "left ground", world, landWidth/2, Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight, Color.white, 0.1f);
		main.addEntity(platform);

		// Right land
		platform = new Ground(main, "right ground", world, Statics.WORLD_WIDTH_LOGICAL-(landWidth/2), Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight, Color.white, 0.1f);
		main.addEntity(platform);

		EndOfLevel eol = new EndOfLevel(main, world, Statics.WORLD_WIDTH_LOGICAL-3, 3, 2, 2);
		main.addEntity(eol);

	}


	@Override
	public void preprocess(long interpol) {
		this.nextBarrel -= interpol;
		if (nextBarrel < 0) {		
			float rad = Functions.rndFloat(.8f, 3f);
			//Barrel barrel = new Barrel(main, main.world, Statics.WORLD_WIDTH_LOGICAL/2, 3, rad, .5f);
			//main.addEntity(barrel);
			float x = Functions.rndFloat(Statics.WORLD_WIDTH_LOGICAL*.2f, Statics.WORLD_WIDTH_LOGICAL*.8f);
			Crate crate = new Crate(main, main.world, x, 4, rad, rad, .5f);
			main.addEntity(crate);

			nextBarrel = BARREL_INTERVAL;
		}
	}



}
