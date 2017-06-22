package com.scs.physicsplatformer.levels;

import java.awt.Color;
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
			return new Level3_DonkeyKong(main);
		case 4:
			return new Level4_FallingCrates(main);
		case 5:
			return new Level5_BigSpinner(main);
		case 6:
			return new Level6_Warehouse(main);
		case 7:
			return new Level7_Dominoes(main);
		case 8:
			return new Level8_MovingPlatforms(main);
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
		Color c = Color.lightGray;
		
		// Borders
		Ground ground = new Ground(main, "ground", world, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL-1, Statics.WORLD_WIDTH_LOGICAL, 1, c, 0.1f);
		main.addEntity(ground);

		Ground leftWall = new Ground(main, "leftWall", world, .5f, Statics.WORLD_HEIGHT_LOGICAL/2, 1, Statics.WORLD_HEIGHT_LOGICAL, c, 0.1f);
		main.addEntity(leftWall);

		Ground rightWall = new Ground(main, "rightWall", world, Statics.WORLD_WIDTH_LOGICAL-.5f, Statics.WORLD_HEIGHT_LOGICAL/2, 1, Statics.WORLD_HEIGHT_LOGICAL, c, 0.1f);
		main.addEntity(rightWall);

		Ground ceiling = new Ground(main, "ceiling", world, Statics.WORLD_WIDTH_LOGICAL/2, 1, Statics.WORLD_WIDTH_LOGICAL, 1, c, 0.1f);
		main.addEntity(ceiling);


	}


	@Override
	public void cleanup(World world) {
		// Do nothing
	}

}
