package com.scs.physicsplatformer.levels;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.EndOfLevel;
import com.scs.physicsplatformer.entity.Ground;
import com.scs.physicsplatformer.entity.PlayersAvatar;
import com.scs.physicsplatformer.entity.RotatingCube;

/*
 * Player must get across chasm using a big spinning square
 */
public class Level5 extends AbstractLevel {

	private Point playerStart;
	
	public Level5(Main main) {
		super(main);
	}

	@Override
	public void createWorld(World world, Main main) {
		super.addFrame(world, main);
		
		float landWidth = Statics.WORLD_WIDTH_LOGICAL/4;
		float landHeight = Statics.WORLD_WIDTH_LOGICAL/3;

		playerStart= new Point((int)(landWidth/2), (int)(landHeight-(PlayersAvatar.RAD*2)));
		
		// Left land
		Ground platform = new Ground(main, "left ground", world, landWidth/2, Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight);
		main.addEntity(platform);

		// Right land
		platform = new Ground(main, "right ground", world, Statics.WORLD_WIDTH_LOGICAL-(landWidth/2), Statics.WORLD_HEIGHT_LOGICAL-(landHeight/2), landWidth, landHeight);
		main.addEntity(platform);

		// Giant square
		RotatingCube cube = new RotatingCube(main, world, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL/2, 3f, Statics.WORLD_WIDTH_LOGICAL/2);
		main.addEntity(cube);

		EndOfLevel eol = new EndOfLevel(main, world, Statics.WORLD_WIDTH_LOGICAL-3, 3, 2, 2);
		main.addEntity(eol);

	}
	

	@Override
	public Point getPlayerStartPos() {
		return playerStart;
	}


}
