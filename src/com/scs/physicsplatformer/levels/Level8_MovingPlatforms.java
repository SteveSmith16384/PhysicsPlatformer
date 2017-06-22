package com.scs.physicsplatformer.levels;

import java.awt.Color;
import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.EndOfLevel;
import com.scs.physicsplatformer.entity.Ground;
import com.scs.physicsplatformer.entity.MovingPlatform;
import com.scs.physicsplatformer.entity.PlayersAvatar;

public class Level8_MovingPlatforms extends AbstractLevel {

	private Point playerStart;
	
	public Level8_MovingPlatforms(Main main) {
		super(main);
	}

	@Override
	public void createWorld(World world, Main main) {
		super.addFrame(world, main);
		
		float landWidth = Statics.WORLD_WIDTH_LOGICAL/4;
		float landHeight = Statics.WORLD_WIDTH_LOGICAL/3;

		playerStart= new Point((int)(landWidth/2), (int)(landHeight-(PlayersAvatar.RAD*2)));
		
		// Left land
		Ground platform = new Ground(main, "left ground", world, landWidth/2, Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight, Color.white, 0.1f);
		main.addEntity(platform);

		// Right land
		platform = new Ground(main, "right ground", world, Statics.WORLD_WIDTH_LOGICAL-(landWidth/2), Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight, Color.white, 0.1f);
		main.addEntity(platform);

		// Moving platform
		MovingPlatform moving = new MovingPlatform(main, world, Statics.WORLD_WIDTH_LOGICAL * .5f, landHeight, 3, 1, Color.cyan, Statics.WORLD_WIDTH_LOGICAL-(landWidth*2));
		main.addEntity(moving);

		EndOfLevel eol = new EndOfLevel(main, world, Statics.WORLD_WIDTH_LOGICAL-3, 3, 2, 2);
		main.addEntity(eol);

	}
	

	@Override
	public Point getPlayerStartPos() {
		return playerStart;
	}


}
