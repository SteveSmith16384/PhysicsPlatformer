package com.scs.physicsplatformer;

import java.util.Random;

public class Statics {

	public static final boolean DEBUG = true;
	
	public static final int FPS = 30;
	public static final int LOGICAL_TO_PIXELS = 10; // todo - rename to LOGICAL_TO_PIXELS
	public static final float TURN_TORQUE = 80*3f;
	public static final float ROTOR_FORCE = 80*5f;
	public static final int MAX_ROPE_LENGTH = 8;

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	public static final int WORLD_WIDTH_LOGICAL = (WINDOW_WIDTH) / LOGICAL_TO_PIXELS;
	public static final int WORLD_HEIGHT_LOGICAL = WINDOW_HEIGHT / LOGICAL_TO_PIXELS;

	public static final Random rnd = new Random();

	private Statics() {

	}


	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ":" + s);
	}


}
