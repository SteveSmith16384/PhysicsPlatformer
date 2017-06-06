package com.scs.physicsplatformer.levels;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Crate;
import com.scs.physicsplatformer.entity.Ground;
import com.scs.physicsplatformer.entity.HarmfulBlock;
import com.scs.physicsplatformer.entity.PlayersAvatar;

/*
 * Player must push crates into harm to use as stepping stones
 * 
 */
public class Level2 extends AbstractLevel {

	private Point playerStart;
	
	public Level2(Main main) {
		super(main);
	}

	
	@Override
	public void createWorld(World world, Main main) {
		super.addFrame(world, main);
		
		float landWidth = Statics.WORLD_WIDTH_LOGICAL/3;
		float landHeight = Statics.WORLD_WIDTH_LOGICAL/10;

		playerStart= new Point((int)(landWidth/4), (int)(landHeight-(PlayersAvatar.RAD*2)));
		
		// Left land
		Ground platform = new Ground(main, "left ground", world, landWidth/2, Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight);
		main.addEntity(platform);

		// Right land
		platform = new Ground(main, "right ground", world, Statics.WORLD_WIDTH_LOGICAL-(landWidth/2), Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight);
		main.addEntity(platform);
		
		// Harm
		HarmfulBlock harm = new HarmfulBlock(main, world, Statics.WORLD_WIDTH_LOGICAL/2, landHeight, Statics.WORLD_WIDTH_LOGICAL-(landWidth*2), landHeight/2);
		main.addEntity(harm);
		
		
		// Crates
		int CRATE_SIZE = 3;
		for (int i=0 ; i<5 ; i++) {
			Crate crate = new Crate(main, world, landWidth, i*(CRATE_SIZE+1), CRATE_SIZE, CRATE_SIZE);
			main.addEntity(crate);
		}

	}
	

	@Override
	public Point getPlayerStartPos() {
		return playerStart;
	}

}
