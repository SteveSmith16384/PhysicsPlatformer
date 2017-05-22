package com.scs.physicsplatformer.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class HelloWorld_BallDrop extends JFrame implements ContactListener {

	private static final long serialVersionUID = 1L;

	private static final int HEIGHT = 600;

	private BufferStrategy BS;

	public static void main(String[] args) {
		HelloWorld_BallDrop hw = new HelloWorld_BallDrop();
		hw.doStuff();
	}


	public HelloWorld_BallDrop() {
		this.setSize(800, HEIGHT);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}


	private void doStuff() {
		Vec2 gravity = new Vec2(0f, 1.0f);
		World world = new World(gravity);

		world.setContactListener(this);

		BodyDef bd = new BodyDef();
		bd.position.set(50, 50);  
		bd.type = BodyType.DYNAMIC;
		bd.userData = "Ball2";

		CircleShape cs = new CircleShape();
		cs.m_radius = 0.5f;  

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 0.5f;
		fd.friction = 0.3f;
		fd.restitution = 0.5f;

		Body ball = world.createBody(bd);
		ball.createFixture(fd);
		ball.setUserData("Ball");
		//ball.setUserData(data);

		float timeStep = 1.0f / 60.f;
		int velocityIterations = 6;
		int positionIterations = 2;

		addGround(world, 0, HEIGHT-50, 800, 10);

		Vec2 force = new Vec2(.01f, 0.0f);
		Vec2 point = ball.getWorldPoint(ball.getWorldCenter());
		//ball.applyLinearImpulse(force, point);
		//ball.applyForce(force, point);

		for (int i = 0; i < 6000; ++i) {
			//while (true) {
			/*if (i > 3000) {
				//ball.applyLinearImpulse(force, point);
				ball.applyForce(force, point);
			}*/
			world.step(timeStep, velocityIterations, positionIterations);
			Vec2 ball_pos = ball.getPosition();
			//float angle = ball.getAngle();

			Graphics g = BS.getDrawGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 800, 600);

			g.setColor(Color.black);
			g.drawOval((int)ball_pos.x, (int)ball_pos.y, 20, 20);
			BS.show();
		}

	}


	public static void addGround(World world, float x, float y, float width, float height){
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width,height);

		FixtureDef fd = new FixtureDef();
		fd.restitution = .6f;
		fd.shape = ps;

		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		bd.position= new Vec2(x, y)	;//HEIGHT);//-10f);
		bd.userData = "Floor2";

		Body b = world.createBody(bd);
		b.createFixture(fd);
		b.setUserData("Floor");
	}


	public static void p(String s) {
		System.out.println(s);
	}
	@Override
	public void beginContact(Contact contact) {
		p("BeginContact A:" + contact.getFixtureA().getBody().getUserData());
		p("BeginContact B:" + contact.getFixtureB().getBody().getUserData());

	}


	@Override
	public void endContact(Contact contact) {
		p("EndContact");

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
