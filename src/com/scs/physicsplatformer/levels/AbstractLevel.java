package com.scs.physicsplatformer.levels;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Entity;
import com.scs.physicsplatformer.entity.Ground;
import com.scs.physicsplatformer.entity.components.IProcessable;

public abstract class AbstractLevel extends Entity implements IProcessable {

	public static AbstractLevel GetLevel(int id, Main main) {
		switch (id) {
		case 1:
			return new Level1(main);
		case 2:
			return new Level2(main);
		case 3:
			return new Level3(main);
		default:
			throw new RuntimeException("No such level: " + id);
		}
	}


	public AbstractLevel(Main main) {
		super(main, "level");
	}


	public abstract Point getPlayerStartPos();

	public abstract void createWorld(World world, Main main);

	@Override
	public void preprocess(long interpol) {

	}


	@Override
	public void postprocess(long interpol) {

	}


	protected void addFrame(World world, Main main) {
		// Borders
		Ground ground = new Ground(main, "ground", world, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL-1, Statics.WORLD_WIDTH_LOGICAL, 1);
		main.addEntity(ground);

		Ground leftWall = new Ground(main, "leftWall", world, .5f, Statics.WORLD_HEIGHT_LOGICAL/2, 1, Statics.WORLD_HEIGHT_LOGICAL);
		main.addEntity(leftWall);

		Ground rightWall = new Ground(main, "rightWall", world, Statics.WORLD_WIDTH_LOGICAL-.5f, Statics.WORLD_HEIGHT_LOGICAL/2, 1, Statics.WORLD_HEIGHT_LOGICAL);
		main.addEntity(rightWall);

		Ground ceiling = new Ground(main, "ceiling", world, Statics.WORLD_WIDTH_LOGICAL/2, 1, Statics.WORLD_WIDTH_LOGICAL, 1);
		main.addEntity(ceiling);


	}

}
