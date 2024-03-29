package com.scs.physicsplatformer;

import java.util.Random;

import org.jbox2d.common.Vec2;

import ssmith.awt.ImageCache;

public class Statics {

	public static final boolean RELEASE_MODE = true;
	
	public static final float PLAYER_MOVE_FORCE = .5f; // 5f;//.2f; // 100f;
	public static final float PLAYER_JUMP_FORCE = 8f;//.2f; // 100f;
	public static final int FPS = 30;
	public static final int LOGICAL_TO_PIXELS = 20;
	public static final int MAX_ROPE_LENGTH = 8;
	public static final Vec2 VEC_CENTRE = new Vec2(0, 0);

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	public static final int WORLD_WIDTH_LOGICAL = (WINDOW_WIDTH) / LOGICAL_TO_PIXELS;
	public static final int WORLD_HEIGHT_LOGICAL = WINDOW_HEIGHT / LOGICAL_TO_PIXELS;

	public static final Random rnd = new Random();

	public static ImageCache img_cache;

	private Statics() {

	}


	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ":" + s);
	}


}
