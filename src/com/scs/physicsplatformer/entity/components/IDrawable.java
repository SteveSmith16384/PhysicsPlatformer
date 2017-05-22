package com.scs.physicsplatformer.entity.components;

import java.awt.Graphics;

import org.jbox2d.common.Vec2;

import com.scs.physicsplatformer.entity.systems.DrawingSystem;

public interface IDrawable {

	void draw(Graphics g, DrawingSystem system, Vec2 cam_centre);
}
