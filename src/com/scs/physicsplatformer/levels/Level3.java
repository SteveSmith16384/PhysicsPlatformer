package com.scs.physicsplatformer.levels;

import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Barrel;
import com.scs.physicsplatformer.entity.EndOfLevel;
import com.scs.physicsplatformer.entity.Ground;

/*
 * Player must get to the top while avoiding rolling barrels
 */
public class Level3 extends AbstractLevel {

	private static final int BARREL_INTERVAL = 10 * 1000;

	private int nextBarrel = 0;

	public Level3(Main main) {
		super(main);
	}

	@Override
	public Point getPlayerStartPos() {
		return new Point(5, Statics.WORLD_HEIGHT_LOGICAL-5);

	}

	@Override
	public void createWorld(World world, Main main) {
		super.addFrame(world, main);

		// Platforms
		for (int i=0 ; i<6 ; i++) {
			Ground platform = new Ground(main, "platform", world, 0, 0, Statics.WORLD_WIDTH_LOGICAL*.8f, .3f); // todo - make more bouncy
			float x = Statics.WORLD_WIDTH_LOGICAL/2;
			float ang = 0.07f;
			if (i % 2 == 0) {
				ang = ang * -1;
				x += Statics.WORLD_WIDTH_LOGICAL*.2f;
			} else {
				x -= Statics.WORLD_WIDTH_LOGICAL*.2f;
			}
			platform.body.setTransform(new Vec2(x, (Statics.WORLD_HEIGHT_LOGICAL/6)*i), ang);
			main.addEntity(platform);
		}

		EndOfLevel eol = new EndOfLevel(main, world, Statics.WORLD_WIDTH_LOGICAL-3, 0, 2, 2);
		main.addEntity(eol);

	}


	@Override
	public void preprocess(long interpol) {
		this.nextBarrel -= interpol;
		if (nextBarrel < 0) {		
			Barrel barrel = new Barrel(main, main.world, Statics.WORLD_WIDTH_LOGICAL/6, 3, .5f); // todo - barrels harm
			main.addEntity(barrel);
			
			nextBarrel = BARREL_INTERVAL;
		}
	}



}
