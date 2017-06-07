package com.scs.physicsplatformer.levels;

import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Barrel;
import com.scs.physicsplatformer.entity.EndOfLevel;
import com.scs.physicsplatformer.entity.Ground;

public class Level4_Zub extends AbstractLevel { // todo

	//private static final int BARREL_INTERVAL = 10 * 1000;

	//private int nextBarrel = 0;

	public Level4_Zub(Main main) {
		super(main);
	}

	@Override
	public Point getPlayerStartPos() {
		return new Point(5, Statics.WORLD_HEIGHT_LOGICAL-5);

	}

	@Override
	public void createWorld(World world, Main main) {
		super.addFrame(world, main);

		//EndOfLevel eol = new EndOfLevel(main, world, Statics.WORLD_WIDTH_LOGICAL-3, 0, 2, 2);
		//main.addEntity(eol);

	}


	@Override
	public void preprocess(long interpol) {
	}



}
