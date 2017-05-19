package krakatoa.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.ChainShape;
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
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.RopeJointDef;

public class HW_Rope extends JFrame implements ContactListener {

	private static final long serialVersionUID = 1L;

	private static final int FPS = 30;

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;

	public static final int WORLD_TO_PIXELS = 10;
	private static final int WALL_HEIGHT = 20;
	private static final float BALL_RAD = 0.5f;

	private static final Random rnd = new Random();

	private BufferStrategy BS;
	List<Body> objects = new ArrayList<Body>();
	Body wall;

	public static void main(String[] args) {
		HW_Rope hw = new HW_Rope();
		hw.doStuff();
	}


	public HW_Rope() {
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
		wall = addGround("Wall", world, WINDOW_WIDTH/2/WORLD_TO_PIXELS, (WINDOW_HEIGHT/WORLD_TO_PIXELS)-(WALL_HEIGHT*2), 2, WALL_HEIGHT, BodyType.DYNAMIC);
		//Body wall = addGround("Wall", world, 200/WORLD_TO_PIXELS, WALL_HEIGHT, 2, WALL_HEIGHT, BodyType.DYNAMIC);

		// Chain
		Vec2[] v = new Vec2[10];
		for (int i=0 ; i<10 ; i++) {
			v[i] = new Vec2(i*5, rnd.nextInt(3));
		}

		addRopeShape(world);

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

			DrawShape(g, ground, Color.green);
			DrawShape(g, wall, Color.red);
			//DrawShape(g, chain, Color.GRAY);

			for(Body b : objects) {
				DrawShape(g, b, Color.green);
			}

			BS.show();

			try {
				Thread.sleep(1000/FPS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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


	public static void DrawShape(Graphics g, Body b, Color c) {
		g.setColor(c);
		if (b.getFixtureList().getShape() instanceof PolygonShape) {
			PolygonShape shape = (PolygonShape)b.getFixtureList().getShape();
			Vec2 prev = shape.getVertex(shape.getVertexCount()-1);
			for (int i=0 ; i<shape.getVertexCount() ; i++) {
				Vec2 worldpos = b.getWorldPoint(prev);
				int x1 = (int)(worldpos.x*WORLD_TO_PIXELS);
				int y1 = (int)(worldpos.y*WORLD_TO_PIXELS);
				Vec2 v = shape.getVertex(i);
				worldpos = b.getWorldPoint(v);
				int x2 = (int)(worldpos.x*WORLD_TO_PIXELS);
				int y2 = (int)(worldpos.y*WORLD_TO_PIXELS);
				g.drawLine(x1, y1, x2, y2);
				prev = v;
			}
		} else if (b.getFixtureList().getShape() instanceof EdgeShape) {
			EdgeShape shape = (EdgeShape)b.getFixtureList().getShape();
			Vec2 prev = shape.m_vertex1;
			Vec2 v = shape.m_vertex2;
			Vec2 worldpos = b.getWorldPoint(prev);
			int x1 = (int)(worldpos.x*WORLD_TO_PIXELS);
			int y1 = (int)(worldpos.y*WORLD_TO_PIXELS);

			worldpos = b.getWorldPoint(v);
			int x2 = (int)(worldpos.x*WORLD_TO_PIXELS);
			int y2 = (int)(worldpos.y*WORLD_TO_PIXELS);

			g.drawLine(x1, y1, x2, y2);
		} else if (b.getFixtureList().getShape() instanceof ChainShape) {
			ChainShape shape2 = (ChainShape)b.getFixtureList().getShape();
			//Vec2 prev = shape.g.getVertex(shape.getVertexCount()-1);
			EdgeShape shape = new EdgeShape();
			for (int i=0 ; i<shape2.getChildCount() ; i++) {
				shape2.getChildEdge(shape, i);

				Vec2 prev = shape.m_vertex1;
				Vec2 v = shape.m_vertex2;
				Vec2 worldpos = b.getWorldPoint(prev);
				int x1 = (int)(worldpos.x*WORLD_TO_PIXELS);
				int y1 = (int)(worldpos.y*WORLD_TO_PIXELS);

				worldpos = b.getWorldPoint(v);
				int x2 = (int)(worldpos.x*WORLD_TO_PIXELS);
				int y2 = (int)(worldpos.y*WORLD_TO_PIXELS);

				g.drawLine(x1, y1, x2, y2);
			}
		}

	}


	public static void DrawShape_OLD(Graphics g, Body b) {
		PolygonShape shape = (PolygonShape)b.getFixtureList().getShape();
		//Vec2 pos = b.getWorldCenter();
		Vec2 prev = shape.getVertex(shape.getVertexCount()-1);
		for (int i=0 ; i<shape.getVertexCount() ; i++) {
			// todo - only convert once
			int x1 = (int)(b.getWorldPoint(prev).x*WORLD_TO_PIXELS);
			int y1 = (int)(b.getWorldPoint(prev).y*WORLD_TO_PIXELS);
			Vec2 v = shape.getVertex(i);
			int x2 = (int)(b.getWorldPoint(v).x*WORLD_TO_PIXELS);
			int y2 = (int)(b.getWorldPoint(v).y*WORLD_TO_PIXELS);
			/*int x1 = (int)prev.x + (int)pos.x;
            int y1 = (int)prev.y +  + (int)pos.y;
            int x2 = (int)v.x + (int)pos.x;
            int y2 = (int)v.y + (int)pos.y;*/
			g.drawLine(x1, y1, x2, y2);
			prev = v;
		}

	}


	public void addRopeShape(World world) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f, 0.125f);

		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = 20.0f;
		fd.friction = 0.2f;
		fd.filter.categoryBits = 0x0001;
		fd.filter.maskBits = 0xFFFF & ~0x0002;

		RevoluteJointDef jd = new RevoluteJointDef();
		jd.collideConnected = false;

		final int N = 20;
		final float y = 15.0f;
		RopeJointDef m_ropeDef = new RopeJointDef();
		m_ropeDef.localAnchorA.set(0.0f, y);

		Body prevBody = null;
		for (int i = 0; i < N; ++i) {
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DYNAMIC;
			bd.position.set(0.5f + 1.0f * i, y);
			if (i == N - 1) {
				// Create anchor box
				shape.setAsBox(1.5f, 1.5f);
				fd.density = 100.0f;
				fd.filter.categoryBits = 0x0002;
				bd.position.set(1.0f * i, y);
				bd.angularDamping = 0.4f;
				bd.type = BodyType.STATIC;
			}

			Body body = world.createBody(bd);

			//if (i < N-1) {
			objects.add(body);
			//}

			body.createFixture(fd);

			Vec2 anchor = new Vec2(i, y);
			if (prevBody != null) {
				jd.initialize(prevBody, body, anchor);
				world.createJoint(jd);
			}

			prevBody = body;
		}

		//m_ropeDef.localAnchorB.setZero();

		float extraLength = 0.01f;
		m_ropeDef.maxLength = N - 1.0f + extraLength;
		m_ropeDef.bodyB = prevBody;

		//m_ropeDef.bodyA = wall;
		//Joint m_rope = world.createJoint(m_ropeDef);

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

