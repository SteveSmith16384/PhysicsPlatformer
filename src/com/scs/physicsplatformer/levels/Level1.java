package com.scs.physicsplatformer.levels;

import java.awt.Color;
import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.PhysicsPlatformer_Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Crate;
import com.scs.physicsplatformer.entity.EndOfLevel;
import com.scs.physicsplatformer.entity.Ground;
import com.scs.physicsplatformer.entity.PlayersAvatar;

/*
 * Player must jump across chasm using a precarious pile of crates.
 * Use the grenade to complete this level by launching the player 
 */
public class Level1 extends AbstractLevel {

	private Point playerStart;
	
	public Level1(PhysicsPlatformer_Main main) {
		super(main);
	}

	
	@Override
	public void createWorld(World world, PhysicsPlatformer_Main main) {
		super.addFrame(world, main);
		
		float landWidth = Statics.WORLD_WIDTH_LOGICAL/3;
		float landHeight = Statics.WORLD_WIDTH_LOGICAL/3;

		playerStart= new Point((int)(landWidth/2), (int)(landHeight-(PlayersAvatar.RAD*2)));
		
		// Left land
		Ground platform = new Ground(main, "left ground", world, landWidth/2, Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight, Color.white,  0.1f);
		main.addEntity(platform);

		// Right land
		platform = new Ground(main, "right ground", world, Statics.WORLD_WIDTH_LOGICAL-(landWidth/2), Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight, Color.white, 0.1f);
		main.addEntity(platform);
		
		
		// Crates
		float CRATE_SIZE = 2;//2.5f;
		for (int i=0 ; i<45 ; i++) {
			Crate crate = new Crate(main, world, Statics.WORLD_WIDTH_LOGICAL/2, i*(CRATE_SIZE+.2f), CRATE_SIZE, CRATE_SIZE, .5f);
			main.addEntity(crate);
		}
		
		EndOfLevel eol = new EndOfLevel(main, world, Statics.WORLD_WIDTH_LOGICAL-3, 3, 2, 2);
		main.addEntity(eol);

	}
	

	@Override
	public Point getPlayerStartPos() {
		return playerStart;
	}


}
