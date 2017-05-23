package com.scs.physicsplatformer.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class HelloWorld_Edge extends JFrame implements ContactListener {

	private static final long serialVersionUID = 1L;

	private static final int FPS = 30;

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;

	public static final int WORLD_TO_PIXELS = 10;
	private static final int WALL_HEIGHT = 20;
	private static final float BALL_RAD = 0.5f;

	private BufferStrategy BS;

	public static void main(String[] args) {
		HelloWorld_Edge hw = new HelloWorld_Edge();
		hw.doStuff();
	}


	public HelloWorld_Edge() {
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}


	private void doStuff() {
		Vec2 gravity = new Vec2(0f, 10.0f);
		World world = new World(gravity);

		world.setContactListener(this);

		//world.setAllowSleep(true);
		//world.setWarmStarting(true);
		//world.setSubStepping(true);
		//world.setContinuousPhysics(true);


		// Add ball
		BodyDef bd = new BodyDef();
		bd.position.set(10, 10);
		bd.type = BodyType.DYNAMIC;
		bd.userData = "Ball2";

		CircleShape cs = new CircleShape();
		cs.m_radius = BALL_RAD;

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 10f;//0.5f;
		fd.friction = 0.3f;
		fd.restitution = 0.0f;

		Body ball = world.createBody(bd);
		ball.createFixture(fd);
		ball.setUserData("Ball");


		float timeStep = 1.0f / FPS;//10.f;
		int velocityIterations = 6;//8;//6;
		int positionIterations = 4;//3;//2;

		// Ground
		Body ground = addGround("Ground", world, WINDOW_WIDTH/WORLD_TO_PIXELS/2, (WINDOW_HEIGHT-50)/WORLD_TO_PIXELS, WINDOW_WIDTH/WORLD_TO_PIXELS, 25/WORLD_TO_PIXELS, BodyType.STATIC);

		// Wall
		Body wall = addGround("Wall", world, WINDOW_WIDTH/2/WORLD_TO_PIXELS, (WINDOW_HEIGHT/WORLD_TO_PIXELS)-(WALL_HEIGHT*2), 2, WALL_HEIGHT, BodyType.DYNAMIC);
		//Body wall = addGround("Wall", world, 200/WORLD_TO_PIXELS, WALL_HEIGHT, 2, WALL_HEIGHT, BodyType.DYNAMIC);

		// Edge
		Body edge = this.MakeEdgeShape(world, 5, 25, 20, 30, BodyType.STATIC, 1f, 1f, 1f);

		Vec2 linimp_force = new Vec2(0.1f, 0.0f);
		Vec2 point = ball.getWorldPoint(ball.getWorldCenter());
		//ball.applyLinearImpulse(force, point); // Imediately applies force
		//ball.applyForce(force, point); // Adds to force

		//for (int i = 0; i < 6000; ++i) {
		//ball.applyLinearImpulse(force, point);

		while (true) {
			//if (i > 3000) {
			ball.applyLinearImpulse(linimp_force, point, true);
			//ball.applyForce(force, point);
			//}
			world.step(timeStep, velocityIterations, positionIterations);
			//float angle = ball.getAngle();

			Graphics g = BS.getDrawGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 800, 600);

			// Draw ball
			g.setColor(Color.black);
			Vec2 ball_pos = ball.getPosition();
			g.drawOval((int)(ball_pos.x*WORLD_TO_PIXELS), (int)(ball_pos.y*WORLD_TO_PIXELS), (int)(BALL_RAD*WORLD_TO_PIXELS), (int)(BALL_RAD*WORLD_TO_PIXELS));

			DrawShape(g, ground);
			DrawShape(g, wall);
			DrawShape(g, edge);

			BS.show();

			try {
				Thread.sleep(1000/FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


	public static Body addGround(String name, World world, float centre_x, float centre_y, float width, float height, BodyType bodytype) {
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width/2,height/2);

		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
		fd.restitution = 0.8f;
		fd.density = 2f; // Weight kg/m2

		BodyDef bd = new BodyDef();
		bd.type = bodytype;//BodyType.STATIC;
		bd.position= new Vec2(centre_x, centre_y);//HEIGHT);//-10f);
		bd.userData = name + "_BodyDef";

		Body b = world.createBody(bd);
		b.createFixture(fd);
		b.setUserData(name);

		return b;
	}


	public Body MakeEdgeShape(World world, float x1, float y1, float x2, float y2, BodyType bodyType, float density, float restitution,float friction){
		Vec2 centre = new Vec2((x1+x2)/2, (y1+y2)/2);
		EdgeShape es=new EdgeShape();
		//SETTING THE POINTS AS OFFSET DISTANCE FROM CENTER
		es.set(new Vec2(x1-centre.x, y1-centre.y), new Vec2(x2-centre.x, y2-centre.y));

		FixtureDef fixtureDef=new FixtureDef();
		fixtureDef.density=density;
		fixtureDef.restitution=restitution;
		fixtureDef.friction=friction;
		fixtureDef.shape=es;

		BodyDef bd = new BodyDef();
		bd.type = bodyType;//BodyType.STATIC;
		bd.position= centre;// new Vec2((x2-x1)/2, (y2-y1)/2);
		//bd.userData = name + "_BodyDef";

		Body b = world.createBody(bd);
		b.createFixture(fixtureDef);
		//b.setUserData(name);
		return b;
	}


	public static void DrawShape(Graphics g, Body b) {
		if (b.getFixtureList().getShape() instanceof PolygonShape) {
			PolygonShape shape = (PolygonShape)b.getFixtureList().getShape();
			Vec2 prev = shape.getVertex(shape.getVertexCount()-1);
			for (int i=0 ; i<shape.getVertexCount() ; i++) {
				// todo - only convert once
				int x1 = (int)(b.getWorldPoint(prev).x*WORLD_TO_PIXELS);
				int y1 = (int)(b.getWorldPoint(prev).y*WORLD_TO_PIXELS);
				Vec2 v = shape.getVertex(i);
				int x2 = (int)(b.getWorldPoint(v).x*WORLD_TO_PIXELS);
				int y2 = (int)(b.getWorldPoint(v).y*WORLD_TO_PIXELS);
				g.drawLine(x1, y1, x2, y2);
				prev = v;
			}
		} else if (b.getFixtureList().getShape() instanceof EdgeShape) {
			EdgeShape shape = (EdgeShape)b.getFixtureList().getShape();
			Vec2 prev = shape.m_vertex1;
			Vec2 v = shape.m_vertex2;
			// todo - only convert once
			int x1 = (int)(b.getWorldPoint(prev).x*WORLD_TO_PIXELS);
			int y1 = (int)(b.getWorldPoint(prev).y*WORLD_TO_PIXELS);
			//Vec2 v = shape.getVertex(i);
			int x2 = (int)(b.getWorldPoint(v).x*WORLD_TO_PIXELS);
			int y2 = (int)(b.getWorldPoint(v).y*WORLD_TO_PIXELS);
			g.drawLine(x1, y1, x2, y2);
			prev = v;
		}

	}



	public static void p(String s) {
		System.out.println(s);
	}


	@Override
	public void beginContact(Contact contact) {
		//p("BeginContact A:" + contact.getFixtureA().getBody().getUserData());
		//p("BeginContact B:" + contact.getFixtureB().getBody().getUserData());

	}


	@Override
	public void endContact(Contact contact) {
		//p("EndContact");

	}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}


	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}

