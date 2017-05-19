package krakatoa;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import krakatoa.entity.Entity;
import krakatoa.entity.components.IDrawable;
import krakatoa.entity.systems.DrawingSystem;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import ssmith.util.TSArrayList;

public class Main extends JFrame implements IObjectList, KeyListener, ContactListener {

	private static final long serialVersionUID = 1L;

	private static final int FPS = 30;
	public static final int WORLD_TO_PIXELS = 10;
	private static final float TURN_TORQUE = 80*3f;
	private static final float ROTOR_FORCE = 80*5f;
	private static final int MAX_ROPE_LENGTH = 8;

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;

	private static final int WORLD_WIDTH_LOGICAL = (WINDOW_WIDTH * 4) / WORLD_TO_PIXELS;
	private static final int WORLD_HEIGHT_LOGICAL = WINDOW_HEIGHT / WORLD_TO_PIXELS;

	private BufferStrategy BS;
	private TSArrayList<Entity> objects = new TSArrayList<Entity>();
	private World world;
	private boolean keys[] = new boolean[256];
	private Body chopper;
	private List<RevoluteJointDef> new_joints_waiting = new ArrayList<RevoluteJointDef>(); // todo - rename
	//private List<RevoluteJointDef> waiting = new ArrayList<RevoluteJointDef>();
	private List<RevoluteJointDef> ropelist;
	private int rope_length;
	private DrawingSystem drawingSystem;
	
	private static final Random rnd = new Random();

	public static void main(String[] args) {
		Main hw = new Main();
		hw.start();
	}


	public Main() {
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}


	private void start() {
		drawingSystem = new DrawingSystem();
		
		Vec2 gravity = new Vec2(0f, 10.0f);
		world = new World(gravity);

		world.setContactListener(this);

		createObjects();
		gameLoop();
	}


	private void gameLoop() {
		float timeStep = 1.0f / FPS;//10.f;
		int velocityIterations = 6;//8;//6;
		int positionIterations = 4;//3;//2;

		long rock_counter = 1000;
		while (true) {
			
			Graphics g = BS.getDrawGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 800, 600);

			for (Entity e : this.objects) {
				if (e instanceof IDrawable) {
					IDrawable id = (IDrawable)e;
					drawingSystem.process(g, id);
				}
			}
			
			//p("Applying force: " + chopper.body.getAngle());
			if (keys[KeyEvent.VK_UP]) {
				Vec2 force = new Vec2();
				force.y = (float)Math.cos(chopper.getAngle()) * -1;
				force.x = (float)Math.sin(chopper.getAngle());
				force.mulLocal(ROTOR_FORCE);
				chopper.applyForceToCenter(force);//, v);
			}
			if (keys[KeyEvent.VK_LEFT]) {
				chopper.applyTorque(-TURN_TORQUE);
			} else if (keys[KeyEvent.VK_RIGHT]) {
				chopper.applyTorque(TURN_TORQUE);
			} else {
				//chopper.applyTorque(0);
				chopper.m_torque = 0;
			}

			if (keys[KeyEvent.VK_W]) {
				ropeAllUp();
			} else if (keys[KeyEvent.VK_S]) {
				ropeAllDown();
			}

			world.step(timeStep, velocityIterations, positionIterations);

			while (this.new_joints_waiting.size() > 0){
				RevoluteJointDef wjd = this.new_joints_waiting.remove(0);
				world.createJoint(wjd);
			}
			
			/*while (objects_to_remove.size() > 0) {
				Body b = objects_to_remove.get(0);
				this.remove(b);
			}*/
			this.objects.refresh();

			// do stuff
			Vec2 cam_centre = this.chopper.getWorldCenter().clone();
			cam_centre.x -= (WINDOW_WIDTH / 2 / WORLD_TO_PIXELS);
			cam_centre.y -= (WINDOW_HEIGHT / 2 / WORLD_TO_PIXELS);

			// Clamp
			//p("y=" + cam_centre.y);
			if (cam_centre.y < -WINDOW_HEIGHT/2) {
				cam_centre.y = -WINDOW_HEIGHT/2;
			} else if (cam_centre.y > 0) {//WORLD_HEIGHT_LOGICAL /2) {
				cam_centre.y = 0;//WORLD_HEIGHT_LOGICAL/2;
			}
			for(Body b : this.objects) {
				drawShape(g, b, cam_centre);
			}

			DrawParticles(g, world, cam_centre);

			BS.show();

			rock_counter -= FPS;
			if (rock_counter < 0) {
				rock_counter = 1000;
				//this.launchRock();
			}

			try {
				Thread.sleep(1000/FPS); // todo -calc start-end diff
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


	private void createObjects() {
		//Body ground = AddRectangle("Ground", world, WORLD_WIDTH/2, WORLD_HEIGHT-3, WORLD_WIDTH, 3, BodyType.STATIC, 10);
		//this.objects.add(ground);

		chopper = JBox2DFunctions.AddRectangle("Chopper", world, 50, 10, 10, 4, BodyType.DYNAMIC, .2f, .2f, .4f);
		chopper.setUserData(new MyUserData("Player", MyUserData.Type.Player, Color.black));
		this.objects.add(chopper);

		/*JBox2DFunctions.AddRopeShape(new MyUserData("Rope", MyUserData.Type.Rope, Color.yellow),
				new MyUserData("Rope_End", MyUserData.Type.StickyRope, Color.yellow),
						world, this, chopper, 8, 10);
		 */
		// Create floor
		int land_data[]   = {2,2,4,6,5,5,5,4,4,4,3,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,5,7,9,9,9,8,6,7,4,4,3,3,4,5,6,4,2,2,2};
		int object_data[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

		int NUM = land_data.length;
		final int len = WORLD_WIDTH_LOGICAL/NUM;
		float prev_y = 0;
		float width = 0;
		float last_x = 0;
		for (int i=0 ; i<NUM ; i++) {
			float act_y = ((land_data[i]/10f)*WORLD_HEIGHT_LOGICAL);
			if (prev_y != act_y) {
				float x = last_x;
				float y = WORLD_HEIGHT_LOGICAL-prev_y;
				Body body = JBox2DFunctions.AddRectangleTL(new MyUserData("Floor", MyUserData.Type.Floor, Color.green), world, 
						x, y, width, 20f, BodyType.STATIC, .1f, .2f, 0.8f);
				//p("x, y = " + i + ": " + x + "," + y);
				this.objects.add(body);
				last_x = last_x + width;
				width = len;
			} else {
				width += len;
			}
			prev_y = act_y;
		}

		// create edges
		Body left_wall = JBox2DFunctions.AddEdgeShapeByTL(world, new MyUserData("Edge", MyUserData.Type.Floor, Color.black), 
				0, -WORLD_HEIGHT_LOGICAL, 0, WORLD_HEIGHT_LOGICAL*2, BodyType.STATIC, 
				0.1f, 0f);
		this.objects.add(left_wall);
		Body right_wall = JBox2DFunctions.AddEdgeShapeByTL(world, new MyUserData("Edge", MyUserData.Type.Floor, Color.black), 
				WORLD_WIDTH_LOGICAL, prev_y, WORLD_WIDTH_LOGICAL, WORLD_HEIGHT_LOGICAL-prev_y, BodyType.STATIC, 
				0.1f, 0f);
		this.objects.add(right_wall);

		this.launchCrate();

		JBox2DFunctions.AddWater(world, new Vec2(160, 40));

		Body tanker = JBox2DFunctions.AddRectangleTL(new MyUserData("Tanker", MyUserData.Type.Tanker, Color.darkGray), world, 
				130, 0, 40, 5, BodyType.DYNAMIC, .2f, .2f, 0.6f);
		this.objects.add(tanker);

	}


	private void launchRock() {
		Body rock = JBox2DFunctions.AddCircle(new MyUserData("Rock", MyUserData.Type.Rock, Color.gray), world, 60, 10, 1, .4f, 1f, .1f);
		this.objects.add(rock);
		int offx = rnd.nextInt(70)+35;
		int offy = rnd.nextInt(50);
		Vec2 force = new Vec2(-offx, -offy);
		rock.applyForceToCenter(force);
	}


	private void launchCrate() {
		Body crate = JBox2DFunctions.AddRectangle(new MyUserData("Crate", MyUserData.Type.Crate, Color.red), world, 
				40, 20, 3f, 3f, BodyType.DYNAMIC, .1f, .2f, 1f);
		this.objects.add(crate);
	}


	public void drawShape(Graphics g, Body b, Vec2 cam_centre) {
		MyUserData userdata = (MyUserData)b.getUserData();
		if (userdata != null) {
			g.setColor(userdata.col);
			/*if (userdata.type == MyUserData.Type.Tanker) {
				p("Tanker");
			}*/
		} else {
			g.setColor(Color.gray);
		}
		if (b.getFixtureList().getShape() instanceof PolygonShape) {
			Polygon p = new Polygon();
			PolygonShape shape = (PolygonShape)b.getFixtureList().getShape();
			//Vec2 prev = shape.getVertex(shape.getVertexCount()-1);
			for (int i=0 ; i<shape.getVertexCount() ; i++) {
				Vec2 v = shape.getVertex(i);
				Vec2 worldpos = b.getWorldPoint(v);
				int x1 = (int)((worldpos.x-cam_centre.x)*WORLD_TO_PIXELS);
				int y1 = (int)((worldpos.y-cam_centre.y)*WORLD_TO_PIXELS);
				/*Vec2 v = shape.getVertex(i);
				worldpos = b.getWorldPoint(v);
				int x2 = (int)((worldpos.x-cam_centre.x)*WORLD_TO_PIXELS);
				int y2 = (int)((worldpos.y-cam_centre.y)*WORLD_TO_PIXELS);
				g.drawLine(x1, y1, x2, y2);
				prev = v;*/
				p.addPoint(x1, y1);
			}
			g.fillPolygon(p);
		} else if (b.getFixtureList().getShape() instanceof EdgeShape) {
			EdgeShape shape = (EdgeShape)b.getFixtureList().getShape();
			Vec2 prev = shape.m_vertex1;
			Vec2 v = shape.m_vertex2;
			Vec2 worldpos = b.getWorldPoint(prev);
			int x1 = (int)((worldpos.x-cam_centre.x)*WORLD_TO_PIXELS);
			int y1 = (int)((worldpos.y-cam_centre.y)*WORLD_TO_PIXELS);

			worldpos = b.getWorldPoint(v);
			int x2 = (int)((worldpos.x-cam_centre.x)*WORLD_TO_PIXELS);
			int y2 = (int)((worldpos.y-cam_centre.y)*WORLD_TO_PIXELS);

			g.drawLine(x1, y1, x2, y2);
		} else if (b.getFixtureList().getShape() instanceof ChainShape) {
			ChainShape shape2 = (ChainShape)b.getFixtureList().getShape();
			EdgeShape shape = new EdgeShape();
			for (int i=0 ; i<shape2.getChildCount() ; i++) {
				shape2.getChildEdge(shape, i);

				Vec2 worldpos = b.getWorldPoint(shape.m_vertex1);
				int x1 = (int)((worldpos.x-cam_centre.x)*WORLD_TO_PIXELS);
				int y1 = (int)((worldpos.y-cam_centre.y)*WORLD_TO_PIXELS);

				worldpos = b.getWorldPoint(shape.m_vertex2);
				int x2 = (int)((worldpos.x-cam_centre.x)*WORLD_TO_PIXELS);
				int y2 = (int)((worldpos.y-cam_centre.y)*WORLD_TO_PIXELS);

				g.drawLine(x1, y1, x2, y2);
			}
		} else if (b.getFixtureList().getShape() instanceof CircleShape) {
			CircleShape shape2 = (CircleShape)b.getFixtureList().getShape();
			Vec2 worldpos = b.getPosition();//b.getWorldPoint(b.getPosition());
			int x = (int)(((worldpos.x-cam_centre.x)-shape2.getRadius()) * WORLD_TO_PIXELS);
			int y = (int)(((worldpos.y-cam_centre.y)-shape2.getRadius()) * WORLD_TO_PIXELS);
			int h = (int)(shape2.getRadius()*2 * WORLD_TO_PIXELS);
			g.fillOval(x, y, h, h);
		} else {
			throw new RuntimeException("Cannot draw " + b);
		}

	}


	private void DrawParticles(Graphics g, World world, Vec2 cam_centre) {
		int particleCount = world.getParticleCount();// system.getParticleCount();
		if (particleCount != 0) {
			float particleRadius = world.getParticleRadius();
			Vec2[] positionBuffer = world.getParticlePositionBuffer();// system.getParticlePositionBuffer();
			//ParticleColor[] colorBuffer = null;
			drawParticles(g, positionBuffer, particleRadius, particleCount, cam_centre);
		}

	}

	public static void drawParticles(Graphics g, Vec2[] centers, float radius, int count, Vec2 cam_centre) {
		for (int i = 0; i < count; i++) {
			Vec2 center = centers[i];
			int draw_rad = (int)(radius*23f);
			g.setColor(Color.blue);
			int x = (int)(((center.x-cam_centre.x)-radius)*WORLD_TO_PIXELS);
			int y = (int)(((center.y-cam_centre.y)-radius)*WORLD_TO_PIXELS);
			g.fillOval(x, y, draw_rad, draw_rad);

		}
	}

	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ":" + s);
	}


	@Override
	public void beginContact(Contact contact) {
		Body ba = contact.getFixtureA().getBody();
		Body bb = contact.getFixtureB().getBody();

		MyUserData ba_ud = (MyUserData)ba.getUserData();
		MyUserData bb_ud = (MyUserData)bb.getUserData();
		try {
			if (ba_ud.type == MyUserData.Type.Crate || bb_ud.type == MyUserData.Type.Crate) {
				if (ba_ud.type == MyUserData.Type.StickyRope || bb_ud.type == MyUserData.Type.StickyRope) {
					p("BeginContact A:" + ba.getUserData());
					p("BeginContact B:" + bb.getUserData());

					RevoluteJointDef wjd = new RevoluteJointDef();
					wjd.collideConnected = false;
					wjd.initialize(ba, bb, new Vec2(ba.getPosition().x, ba.getPosition().y));
					new_joints_waiting.add(wjd);
				} else if (ba_ud.type == MyUserData.Type.Tanker || bb_ud.type == MyUserData.Type.Tanker) {
					p("BeginContact A:" + ba.getUserData());
					p("BeginContact B:" + bb.getUserData());
					if (ba_ud.type == MyUserData.Type.Crate) {
						this.objects.remove(ba);
					}
				}
			}
		} catch (java.lang.NullPointerException ex) {
			// Do nothing
		}

	}


	/*private Body getBody(MyUserData bodyA, MyUserData bodyB, Type type) {
		return bodyA.type == type ? bodyA.
	}*/


	@Override
	public void endContact(Contact contact) {
		//p("EndContact");

	}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}


	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = true;

	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = false;

	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void add(Body body) {
		this.objects.add(body);

	}


	private void ropeAllDown() {
		if (new_joints_waiting.size() == 0) {
			if (this.ropelist == null) {
				rope_length = MAX_ROPE_LENGTH;
				this.createRope();
			}
		}
	}

	private void ropeAllUp() {
		if (new_joints_waiting.size() == 0) {
			if (this.ropelist != null) {
				this.removeRope();
			}
		}
	}

	private void ropeSegmentDown() {
		if (new_joints_waiting.size() == 0) {
			if (rope_length < 8) {
				rope_length++;
				this.removeRope();
				this.createRope();
			}
		}
	}


	private void ropeSegmentUp() {
		if (new_joints_waiting.size() == 0) {
			if (rope_length > 0) {
				rope_length--;
				this.removeRope();
				if (rope_length > 0) {
					this.createRope();
				}
			}
		}
	}


	private void createRope() {
		ropelist = JBox2DFunctions.AddRopeShape(new MyUserData("Rope", MyUserData.Type.Rope, Color.yellow),
				new MyUserData("Rope_End", MyUserData.Type.StickyRope, Color.yellow),
				world, this, chopper, rope_length, rope_length);

	}

	private void removeRope() {
		if (ropelist != null) {
			for (RevoluteJointDef rjd : ropelist) {
				if (rjd.bodyA != chopper) {
					remove(rjd.bodyA);
				}
				if (rjd.bodyB != chopper) {
					remove(rjd.bodyB);
				}
			}
			ropelist = null;
		}
	}


	private void remove(Body b) {
		world.destroyBody(b);
		synchronized (objects) {
			this.objects.remove(b);
		}		
	}
}

