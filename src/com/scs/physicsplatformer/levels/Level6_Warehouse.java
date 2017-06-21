package com.scs.physicsplatformer.levels;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsplatformer.Main;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.Crate;
import com.scs.physicsplatformer.entity.EndOfLevel;
import com.scs.physicsplatformer.entity.PlayersAvatar;

/*
 * Player must get over a high wall of crates
 */
public class Level6_Warehouse extends AbstractLevel {

	public Level6_Warehouse(Main main) {
		super(main);
	}
	

	@Override
	public void createWorld(World world, Main main) {
		super.addFrame(world, main);

		// Crates
		float sy = Statics.WORLD_HEIGHT_LOGICAL/4;
		int CRATE_SIZE = 2;
		for (int y=0 ; y<8 ; y++) {
			float sx = Statics.WORLD_WIDTH_LOGICAL/4;
			for (int x=0 ; x<4 ; x++) {
				//if (y <= x) {
				Crate crate = new Crate(main, world, sx, sy, CRATE_SIZE, CRATE_SIZE, 1f);
				main.addEntity(crate);
				//}
				sx += CRATE_SIZE+1;
			}
			sy += CRATE_SIZE+1;
		}

		EndOfLevel eol = new EndOfLevel(main, world, Statics.WORLD_WIDTH_LOGICAL-3, 3, 2, 2);
		main.addEntity(eol);

	}


	@Override
	public Point getPlayerStartPos() {
		return new Point((int)(5), (int)(Statics.WORLD_HEIGHT_LOGICAL-(PlayersAvatar.RAD*2)));
	}


}
