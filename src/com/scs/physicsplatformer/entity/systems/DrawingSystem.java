package com.scs.physicsplatformer.entity.systems;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import com.scs.physicsplatformer.BodyUserData;
import com.scs.physicsplatformer.Statics;
import com.scs.physicsplatformer.entity.components.IDrawable;

public class DrawingSystem {

	public DrawingSystem() {

	}


	public void process(Graphics g, IDrawable sprite, Vec2 cam_centre) {
		sprite.draw(g, this, cam_centre);
	}


	private void getPixelPos(Point ret, Vec2 worldpos, Vec2 cam_centre) {
		//Vec2 worldpos = b.getWorldPoint(v);
		int x1 = (int)((worldpos.x * Statics.LOGICAL_TO_PIXELS)-cam_centre.x + (Statics.WINDOW_WIDTH/2));
		int y1 = (int)((worldpos.y * Statics.LOGICAL_TO_PIXELS)-cam_centre.y + (Statics.WINDOW_HEIGHT/2));
		//return new Point(x1, y1);
		ret.x = x1;
		ret.y = y1;
	}


	public void drawImage(Point tmp, BufferedImage img, Graphics g, Body b, Vec2 cam_centre) {
		//CircleShape shape2 = (CircleShape)f.getShape();
		Vec2 worldpos = b.getPosition();
		getPixelPos(tmp, worldpos, cam_centre);
		//int rad = (int)(shape2.getRadius() * Statics.LOGICAL_TO_PIXELS);
		//g.fillOval((int)(p2.x-rad), (int)(p2.y-rad), rad*2, rad*2);
		int rad = img.getWidth()/2;
		g.drawImage(img, (int)(tmp.x-rad), (int)(tmp.y-rad), null);

	}
	
	
	public void drawShape(Point tmp, Graphics g, Body b, Vec2 cam_centre) {
		/*BodyUserData userdata = (BodyUserData)b.getUserData();
		if (userdata != null) {
			g.setColor(userdata.col);
		} else {
			g.setColor(Color.gray);
		}*/

		Fixture f = b.getFixtureList();
		while (f != null) {
			BodyUserData userdata = (BodyUserData)f.getUserData();
			if (userdata != null) {
				if (userdata.col == null) {
					continue;
				}
				g.setColor(userdata.col);
			} else {
				g.setColor(Color.gray);
			}

			if (f.getShape() instanceof PolygonShape) {
				Polygon polygon = new Polygon();
				PolygonShape shape = (PolygonShape)f.getShape();
				for (int i=0 ; i<shape.getVertexCount() ; i++) {
					Vec2 v = shape.getVertex(i);
					getPixelPos(tmp, b.getWorldPoint(v), cam_centre);
					polygon.addPoint(tmp.x, tmp.y);
				}
				g.fillPolygon(polygon);

			} else if (f.getShape() instanceof EdgeShape) {
				EdgeShape shape = (EdgeShape)f.getShape();
				Vec2 prev = shape.m_vertex1;
				Vec2 v = shape.m_vertex2;
				Vec2 worldpos = b.getWorldPoint(prev);
				Point p = new Point();
				getPixelPos(p, worldpos, cam_centre);

				//int x1 = (int)((worldpos.x-cam_centre.x)*Statics.WORLD_TO_PIXELS);
				//int y1 = (int)((worldpos.y-cam_centre.y)*Statics.WORLD_TO_PIXELS);

				worldpos = b.getWorldPoint(v);
				//int x2 = (int)((worldpos.x-cam_centre.x)*Statics.WORLD_TO_PIXELS);
				//int y2 = (int)((worldpos.y-cam_centre.y)*Statics.WORLD_TO_PIXELS);
				getPixelPos(tmp, worldpos, cam_centre);

				g.drawLine(p.x, p.y, tmp.x, tmp.y);

				/*todo } else if (f.getShape() instanceof ChainShape) {
			ChainShape shape2 = (ChainShape)f.getShape();
			EdgeShape shape = new EdgeShape();
			for (int i=0 ; i<shape2.getChildCount() ; i++) {
				shape2.getChildEdge(shape, i);

				Vec2 worldpos = b.getWorldPoint(shape.m_vertex1);
				int x1 = (int)((worldpos.x-cam_centre.x)*Statics.WORLD_TO_PIXELS);
				int y1 = (int)((worldpos.y-cam_centre.y)*Statics.WORLD_TO_PIXELS);

				worldpos = b.getWorldPoint(shape.m_vertex2);
				int x2 = (int)((worldpos.x-cam_centre.x)*Statics.WORLD_TO_PIXELS);
				int y2 = (int)((worldpos.y-cam_centre.y)*Statics.WORLD_TO_PIXELS);

				g.drawLine(x1, y1, x2, y2);
			}*/

			} else if (f.getShape() instanceof CircleShape) {
				CircleShape shape2 = (CircleShape)f.getShape();
				Vec2 worldpos = b.getPosition();
				getPixelPos(tmp, worldpos, cam_centre);
				int rad = (int)(shape2.getRadius() * Statics.LOGICAL_TO_PIXELS);
				g.fillOval((int)(tmp.x-rad), (int)(tmp.y-rad), rad*2, rad*2);

			} else {
				throw new RuntimeException("Cannot draw " + b);
			}

			f = f.getNext();

		}
	}

/*
	private void DrawParticles(Graphics g, World world, Vec2 cam_centre) {
		int particleCount = world.getParticleCount();// system.getParticleCount();
		if (particleCount != 0) {
			float particleRadius = world.getParticleRadius();
			Vec2[] positionBuffer = world.getParticlePositionBuffer();// system.getParticlePositionBuffer();
			//ParticleColor[] colorBuffer = null;
			drawParticles(g, positionBuffer, particleRadius, particleCount, cam_centre);
		}

	}

	private static void drawParticles(Graphics g, Vec2[] centers, float radius, int count, Vec2 cam_centre) {
		for (int i = 0; i < count; i++) {
			Vec2 center = centers[i];
			int draw_rad = (int)(radius*23f);
			g.setColor(Color.blue);
			int x = (int)(((center.x-cam_centre.x)-radius)*Statics.WORLD_TO_PIXELS);
			int y = (int)(((center.y-cam_centre.y)-radius)*Statics.WORLD_TO_PIXELS);
			g.fillOval(x, y, draw_rad, draw_rad);

		}
	}
*/
}
