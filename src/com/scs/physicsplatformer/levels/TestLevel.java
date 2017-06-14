package com.scs.physicsplatformer.levels;

import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Crate;
import com.scs.physicsplatformer.entity.Ground;
import com.scs.physicsplatformer.entity.HarmfulBlock;
import com.scs.physicsplatformer.entity.MovingPlatform;
import com.scs.physicsplatformer.entity.Trampoline;

public class TestLevel extends AbstractLevel {

	public TestLevel(Main main) {
		super(main);
	}


	public void createWorld(World world, Main main) {
		super.addFrame(world, main);
		
		//Platforms
		
		Ground platform = new Ground(main, "platform", world, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL/2, Statics.WORLD_WIDTH_LOGICAL/2, .3f, 0.1f);
		platform.body.setTransform(new Vec2(Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL/2), 0.06f);
		main.addEntity(platform);
		
		Trampoline tramp = new Trampoline(main, world, Statics.WORLD_WIDTH_LOGICAL/4, Statics.WORLD_HEIGHT_LOGICAL-3, 2, .5f);
		main.addEntity(tramp);

		MovingPlatform moving = new MovingPlatform(main, world, Statics.WORLD_WIDTH_LOGICAL * .5f, Statics.WORLD_HEIGHT_LOGICAL*.8f, 3, 1);
		main.addEntity(moving);
		
		// Rope bridge
		Crate crate1 = new Crate(main, world, 10, 20, 1, 1);
		main.addEntity(crate1);
		Crate crate2 = new Crate(main, world, 30, 20, 1, 1);
		main.addEntity(crate2);
		
/*		JBox2DFunctions.AddRopeShape(world, new BodyUserData("Rope", Color.yellow, null, true), crate1.drawableBody, crate2.drawableBody, 20f, 7, .1f, .2f, 1);
		this.entities.add(crate2); - -todo make entity
*/

		// Moving stuff
		
		/*Barrel barrel = new Barrel(world, Statics.WORLD_WIDTH_LOGICAL*.3f, Statics.WORLD_HEIGHT_LOGICAL*.4f, 1f);
		main.addEntity(barrel);*/

		/*Enemy enemy = new Enemy(main, world, Statics.WORLD_WIDTH_LOGICAL * .75f, Statics.WORLD_HEIGHT_LOGICAL/2, 1, 1);
		main.addEntity(enemy);*/
		
		/*Crate crate = new Crate(world, 40, 20, 3, 3);
		main.addEntity(crate);
*/
		HarmfulBlock harm = new HarmfulBlock(main, world, 30, 20, 3, 3);
		main.addEntity(harm);

		//JBox2DFunctions.AddWater(world, new Vec2(160, 40));

	}


	@Override
	public Point getPlayerStartPos() {
		return new Point((int)Statics.WORLD_WIDTH_LOGICAL/2, (int)(Statics.WORLD_HEIGHT_LOGICAL * 0.65f));
	}


}
