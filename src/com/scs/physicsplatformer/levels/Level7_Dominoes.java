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

public class Level7_Dominoes extends AbstractLevel {

	private Point playerStart;
	
	public Level7_Dominoes(PhysicsPlatformer_Main main) {
		super(main);
	}

	
	@Override
	public void createWorld(World world, PhysicsPlatformer_Main main) {
		super.addFrame(world, main);
		
		float landWidth = Statics.WORLD_WIDTH_LOGICAL/5;
		float landHeight = Statics.WORLD_WIDTH_LOGICAL/5;

		playerStart = new Point((int)(landWidth/2), (int)(landHeight-(PlayersAvatar.RAD*2)));
		
		// Left land
		Ground platform = new Ground(main, "left ground", world, landWidth/2, Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight, Color.white, 0.1f);
		main.addEntity(platform);

		// Right land
		platform = new Ground(main, "right ground", world, Statics.WORLD_WIDTH_LOGICAL-(landWidth/2), Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight, Color.white, 0.1f);
		main.addEntity(platform);
		
		
		// dominoes
		float sx = landWidth;
		float xInc = (Statics.WORLD_WIDTH_LOGICAL-(landWidth*2)) / 10f;
		for (int i=0 ; i<7 ; i++) {
			sx += xInc;
			Crate crate = new Crate(main, world, sx, Statics.WORLD_HEIGHT_LOGICAL - 6, .3f, landHeight, 1f);
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
