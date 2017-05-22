package com.scs.physicsplatformer.entity.systems;

import com.scs.physicsplatformer.entity.components.IPlayerControllable;

public class PlayerInputSystem {

	public PlayerInputSystem() {
	}


	public void process(IPlayerControllable chopper) {
		chopper.process();
		//p("Applying force: " + chopper.body.getAngle());
		/*if (keys[KeyEvent.VK_UP]) {
			Vec2 force = new Vec2();
			force.y = (float)Math.cos(chopper.getAngle()) * -1;
			force.x = (float)Math.sin(chopper.getAngle());
			force.mulLocal(ROTOR_FORCE);
			chopper.applyForceToCenter(force);//, v);
		}
		if (keys[KeyEvent.VK_LEFT]) {
			chopper.applyTorque(-Statics.TURN_TORQUE);
		} else if (keys[KeyEvent.VK_RIGHT]) {
			chopper.applyTorque(Statics.TURN_TORQUE);
		} else {
			//chopper.applyTorque(0);
			chopper.m_torque = 0;
		}

		if (keys[KeyEvent.VK_W]) {
			ropeAllUp();
		} else if (keys[KeyEvent.VK_S]) {
			ropeAllDown();
		}
*/
	}
}
