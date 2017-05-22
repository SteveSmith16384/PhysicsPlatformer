package com.scs.physicsplatformer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import ssmith.util.TSArrayList;

import com.scs.physicsplatformer.entity.DrawableBody;
import com.scs.physicsplatformer.entity.PlayersAvatar;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IPlayerControllable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;
import com.scs.physicsplatformer.entity.systems.PlayerInputSystem;
import com.scs.physicsplatformer.input.DeviceThread;
import com.scs.physicsplatformer.input.IInputDevice;
import com.scs.physicsplatformer.input.NewControllerListener;

public class Main implements ContactListener, NewControllerListener {

	private static final long serialVersionUID = 1L;

	private TSArrayList<Object> objects = new TSArrayList<Object>();
	private World world;
	private List<RevoluteJointDef> new_joints_waiting = new ArrayList<RevoluteJointDef>(); // todo - rename
	private List<RevoluteJointDef> ropelist;
	private int rope_length;

	private DrawingSystem drawingSystem;
	private PlayerInputSystem playerInputSystem;

	private MainWindow window;
	private List<PlayersAvatar> avatars = new ArrayList<>();

	public static void main(String[] args) {
		Main hw = new Main();
		hw.start();
	}


	public Main() {
		super();

		window = new MainWindow();

		DeviceThread dt = new DeviceThread(window);
		dt.addListener(this);
		dt.start();

		start();
	}


	private void start() {
		drawingSystem = new DrawingSystem();
		playerInputSystem = new PlayerInputSystem();

		Vec2 gravity = new Vec2(0f, 10.0f);
		world = new World(gravity);

		world.setContactListener(this);

		createObjects();
		gameLoop();
	}


	private void gameLoop() {
		float timeStep = 1.0f / Statics.FPS;//10.f;
		int velocityIterations = 6;//8;//6;
		int positionIterations = 4;//3;//2;

		//long rock_counter = 1000;
		while (true) {
			Graphics g = window.BS.getDrawGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 800, 600);

			// Player input first
			for (Object e : this.objects) {
				if (e instanceof IPlayerControllable) {
					IPlayerControllable id = (IPlayerControllable)e;
					this.playerInputSystem.process(id);
				}
			}

			// Position cam based on players
			Vec2 cam_centre = new Vec2();//this.chopper.getWorldCenter().clone();
			cam_centre.x = (Statics.WINDOW_WIDTH / 2);// / Statics.WORLD_TO_PIXELS);
			cam_centre.y = (Statics.WINDOW_HEIGHT / 2);// / Statics.WORLD_TO_PIXELS);

			// Clamp the cam
			/*if (cam_centre.y < -Statics.WINDOW_HEIGHT/2) {
				cam_centre.y = -Statics.WINDOW_HEIGHT/2;
			} else if (cam_centre.y > 0) {//WORLD_HEIGHT_LOGICAL /2) {
				cam_centre.y = 0;//WORLD_HEIGHT_LOGICAL/2;
			}*/

			// Draw screen
			for (Object e : this.objects) {
				if (e instanceof IDrawable) {
					IDrawable sprite = (IDrawable)e;
					drawingSystem.process(g, sprite, cam_centre);
				}
				if (e instanceof IProcessable) {
					IProcessable id = (IProcessable)e;
					id.process();
				}
			}

			world.step(timeStep, velocityIterations, positionIterations);

			/*while (this.new_joints_waiting.size() > 0){
				RevoluteJointDef wjd = this.new_joints_waiting.remove(0);
				world.createJoint(wjd);
			}*/

			this.objects.refresh();


			//DrawParticles(g, world, cam_centre);

			window.BS.show();

			/*rock_counter -= Statics.FPS;
			if (rock_counter < 0) {
				rock_counter = 1000;
				//this.launchRock();
			}*/

			try {
				Thread.sleep(1000/Statics.FPS); // todo -calc start-end diff
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


	private void createObjects() {
		BodyUserData bud = new BodyUserData("Ground", BodyUserData.Type.Floor, Color.green);
		Body ground = JBox2DFunctions.AddRectangle(bud, world, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL-3, Statics.WORLD_WIDTH_LOGICAL, 3, BodyType.STATIC, .1f, .2f, 0.8f);
		DrawableBody bd = new DrawableBody(ground) ;
		this.addEntity(bd);

		/*chopper = JBox2DFunctions.AddRectangle("Chopper", world, 50, 10, 10, 4, BodyType.DYNAMIC, .2f, .2f, .4f);
		chopper.setUserData(new MyUserData("Player", MyUserData.Type.Player, Color.black));
		this.objects.add(chopper);
		 */

		/*JBox2DFunctions.AddRopeShape(new MyUserData("Rope", MyUserData.Type.Rope, Color.yellow),
				new MyUserData("Rope_End", MyUserData.Type.StickyRope, Color.yellow),
						world, this, chopper, 8, 10);
		 */

		// Create floor
		/*int land_data[]   = {2,2,4,6,5,5,5,4,4,4,3,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,5,7,9,9,9,8,6,7,4,4,3,3,4,5,6,4,2,2,2};
		int object_data[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

		int NUM = land_data.length;
		final int len = Statics.WORLD_WIDTH_LOGICAL/NUM;
		float prev_y = 0;
		float width = 0;
		float last_x = 0;
		for (int i=0 ; i<NUM ; i++) {
			float act_y = ((land_data[i]/10f)*Statics.WORLD_HEIGHT_LOGICAL);
			if (prev_y != act_y) {
				float x = last_x;
				float y = Statics.WORLD_HEIGHT_LOGICAL-prev_y;
				BodyUserData bud = new BodyUserData("Floor", BodyUserData.Type.Floor, Color.green);
				Body body = JBox2DFunctions.AddRectangleTL(bud, world, x, y, width, 20f, BodyType.STATIC, .1f, .2f, 0.8f);
				//p("x, y = " + i + ": " + x + "," + y);
				DrawableBody db = new DrawableBody(body, bud);
				this.addEntity(db);

				last_x = last_x + width;
				width = len;
			} else {
				width += len;
			}
			prev_y = act_y;
		}*/

		// create edges
		/*Body left_wall = JBox2DFunctions.AddEdgeShapeByTL(world, new BodyUserData("Edge", BodyUserData.Type.Floor, Color.black), 
				0, -Statics.WORLD_HEIGHT_LOGICAL, 0, Statics.WORLD_HEIGHT_LOGICAL*2, BodyType.STATIC, 
				0.1f, 0f);
		DrawableBody db = new DrawableBody(left_wall);
		this.objects.add(db);

		Body right_wall = JBox2DFunctions.AddEdgeShapeByTL(world, new BodyUserData("Edge", BodyUserData.Type.Floor, Color.black), 
				Statics.WORLD_WIDTH_LOGICAL, prev_y, Statics.WORLD_WIDTH_LOGICAL, Statics.WORLD_HEIGHT_LOGICAL-prev_y, BodyType.STATIC, 
				0.1f, 0f);
		DrawableBody dbr = new DrawableBody(right_wall);
		this.objects.add(dbr);
		 */
		this.launchCrate();

		//JBox2DFunctions.AddWater(world, new Vec2(160, 40));

		Body tanker = JBox2DFunctions.AddRectangleTL(new BodyUserData("Tanker", BodyUserData.Type.Tanker, Color.darkGray), world, 
				130, 0, 40, 5, BodyType.DYNAMIC, .2f, .2f, 0.6f);
		bd = new DrawableBody(tanker);
		this.addEntity(bd);

	}


	private void launchRock() {
		Body rock = JBox2DFunctions.AddCircle(new BodyUserData("Rock", BodyUserData.Type.Rock, Color.gray), world, 60, 10, 1, .4f, 1f, .1f);
		this.objects.add(rock);
		int offx = Statics.rnd.nextInt(70)+35;
		int offy = Statics.rnd.nextInt(50);
		Vec2 force = new Vec2(-offx, -offy);
		rock.applyForceToCenter(force);
	}


	private void launchCrate() {
		BodyUserData bud = new BodyUserData("Crate", BodyUserData.Type.Crate, Color.red);
		Body crate = JBox2DFunctions.AddRectangle(bud, world, 40, 20, 3f, 3f, BodyType.DYNAMIC, .1f, .2f, 1f);
		DrawableBody db = new DrawableBody(crate);
		this.objects.add(db);
	}


	@Override
	public void beginContact(Contact contact) {
		Body ba = contact.getFixtureA().getBody();
		Body bb = contact.getFixtureB().getBody();

		BodyUserData ba_ud = (BodyUserData)ba.getUserData();
		BodyUserData bb_ud = (BodyUserData)bb.getUserData();
		try {
			if (ba_ud.type == BodyUserData.Type.Crate || bb_ud.type == BodyUserData.Type.Crate) {
				if (ba_ud.type == BodyUserData.Type.StickyRope || bb_ud.type == BodyUserData.Type.StickyRope) {
					Statics.p("BeginContact A:" + ba.getUserData());
					Statics.p("BeginContact B:" + bb.getUserData());

					RevoluteJointDef wjd = new RevoluteJointDef();
					wjd.collideConnected = false;
					wjd.initialize(ba, bb, new Vec2(ba.getPosition().x, ba.getPosition().y));
					new_joints_waiting.add(wjd);
				} else if (ba_ud.type == BodyUserData.Type.Tanker || bb_ud.type == BodyUserData.Type.Tanker) {
					Statics.p("BeginContact A:" + ba.getUserData());
					Statics.p("BeginContact B:" + bb.getUserData());
					if (ba_ud.type == BodyUserData.Type.Crate) {
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


	/*@Override
	public void add(Body body) {
		this.objects.add(body);

	}*/
	/*

	private void ropeAllDown() {
		if (new_joints_waiting.size() == 0) {
			if (this.ropelist == null) {
				rope_length = Statics.MAX_ROPE_LENGTH;
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

	 */
	private void remove(Body b) {
		world.destroyBody(b);
		synchronized (objects) {
			this.objects.remove(b);
		}		
	}


	@Override
	public void newController(IInputDevice input) {
		// todo - create these in the game thread
		BodyUserData bud = new BodyUserData("Player", BodyUserData.Type.Player, Color.black);
		Body body = JBox2DFunctions.AddRectangle(bud, world, 50, 10, 10, 4, BodyType.DYNAMIC, .2f, .2f, .4f);
		DrawableBody b = new DrawableBody(body);
		this.addEntity(b);

		PlayersAvatar avatar = new PlayersAvatar(input, b);
		synchronized (avatars) {
			this.avatars.add(avatar);
		}

		this.addEntity(b);
	}


	private void addEntity(Object o) {
		synchronized (objects) {
			if (Statics.DEBUG) {
				if (this.objects.contains(o)) {
					throw new RuntimeException(o + " has already been added");
				}
				if (o instanceof Body) {
					throw new RuntimeException(o + " is a Body - add DrawableBody at least!");
				}
			}
			this.objects.add(o);
		}
	}
}

