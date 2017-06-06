package com.scs.physicsplatformer.levels;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Entity;
import com.scs.physicsplatformer.entity.Ground;
import com.scs.physicsplatformer.entity.components.IProcessable;

public abstract class AbstractLevel extends Entity implements IProcessable {

	public AbstractLevel(Main main) {
		super(main, "level");
	}
	
	
	public abstract Point getPlayerStartPos();
	
	public abstract void createWorld(World world, Main main);
	
	//public abstract void process(long interpol);
	
	@Override
	public void preprocess(long interpol) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void postprocess(long interpol) {
		// TODO Auto-generated method stub
		
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
