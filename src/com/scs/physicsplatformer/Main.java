package com.scs.physicsplatformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import ssmith.awt.ImageCache;
import ssmith.util.TSArrayList;

import com.scs.physicsplatformer.entity.Entity;
import com.scs.physicsplatformer.entity.PlayersAvatar;
import com.scs.physicsplatformer.entity.components.ICollideable;
import com.scs.physicsplatformer.entity.components.IDrawable;
import com.scs.physicsplatformer.entity.components.IPlayerControllable;
import com.scs.physicsplatformer.entity.components.IProcessable;
import com.scs.physicsplatformer.entity.systems.DrawingSystem;
import com.scs.physicsplatformer.entity.systems.PlayerInputSystem;
import com.scs.physicsplatformer.input.DeviceThread;
import com.scs.physicsplatformer.input.IInputDevice;
import com.scs.physicsplatformer.input.NewControllerListener;
import com.scs.physicsplatformer.levels.AbstractLevel;
import com.scs.physicsplatformer.levels.Level2;

public class Main implements ContactListener, NewControllerListener {

	public World world;
	private MainWindow window;

	private TSArrayList<Entity> entities = new TSArrayList<Entity>();
	private List<IInputDevice> newControllers = new ArrayList<>();
	private List<PlayersAvatar> avatars = new ArrayList<>();
	private List<Player> players = new ArrayList<>();

	private DrawingSystem drawingSystem;
	private PlayerInputSystem playerInputSystem;
	private List<Contact> collisions = new LinkedList<>();
	private AbstractLevel level;

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

		Statics.img_cache = ImageCache.GetInstance(null);
		Statics.img_cache.c = window;

		start();
	}


	private void start() {
		drawingSystem = new DrawingSystem();
		playerInputSystem = new PlayerInputSystem();

		Vec2 gravity = new Vec2(0f, 10.0f);
		world = new World(gravity);

		world.setContactListener(this);

		level = new Level2(this);// TestLevel();
		level.createWorld(world, this);
		this.addEntity(level);
		gameLoop();
	}


	private void gameLoop() {
		long interpol = 30;
		while (window.isVisible()) {
			float timeStep = 1.0f / Statics.FPS;//10.f;
			int velocityIterations = 6;//8;//6;
			int positionIterations = 4;//3;//2;

			while (true) {
				synchronized (newControllers) {
					while (this.newControllers.isEmpty() == false) {
						this.loadPlayer(this.newControllers.remove(0));
					}
				}

				// Preprocess
				for (Entity e : this.entities) {
					if (e instanceof IProcessable) {
						IProcessable id = (IProcessable)e;
						id.preprocess(interpol);
					}
				}

				Graphics g = window.BS.getDrawGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);

				// Player input first
				for (Entity e : this.entities) {
					if (e instanceof IPlayerControllable) {
						IPlayerControllable id = (IPlayerControllable)e;
						this.playerInputSystem.process(id);
					}
				}

				collisions.clear();
				world.step(timeStep, velocityIterations, positionIterations);
				while (collisions.isEmpty() == false) {
					processCollision(collisions.remove(0));
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
				for (Entity e : this.entities) {
					if (e instanceof IDrawable) {
						IDrawable sprite = (IDrawable)e;
						sprite.draw(g, drawingSystem, cam_centre);
					}
					if (e instanceof IProcessable) {
						IProcessable id = (IProcessable)e;
						id.postprocess(interpol);
					}
				}

				/*while (this.new_joints_waiting.size() > 0){
				RevoluteJointDef wjd = this.new_joints_waiting.remove(0);
				world.createJoint(wjd);
			}*/

				this.entities.refresh();

				//DrawParticles(g, world, cam_centre);

				window.BS.show();

				try {
					interpol = 1000/Statics.FPS;
					Thread.sleep(interpol); // todo -calc start-end diff
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/*private void launchRock() {
		BodyUserData bud = new BodyUserData("Rock", BodyUserData.Type.Rock, Color.gray);
		Body rock = JBox2DFunctions.AddCircle(bud, world, 60, 10, 1, BodyType.DYNAMIC, .4f, 1f, .1f);
		DrawableBody db = new DrawableBody(rock);
		bud.entity = db;

		this.entities.add(db);

		int offx = Statics.rnd.nextInt(70)+35;
		int offy = Statics.rnd.nextInt(50);
		Vec2 force = new Vec2(-offx, -offy);
		rock.applyForceToCenter(force);
	}*/


	private void processCollision(Contact contact) {
		Body ba = contact.getFixtureA().getBody();
		Body bb = contact.getFixtureB().getBody();

		BodyUserData ba_ud = (BodyUserData)ba.getUserData();
		BodyUserData bb_ud = (BodyUserData)bb.getUserData();

		//Statics.p("BeginContact BodyUserData A:" + ba_ud);
		//Statics.p("BeginContact BodyUserData B:" + bb_ud);

		Entity entityA = ba_ud.entity;
		Entity entityB = bb_ud.entity;

		//Statics.p("BeginContact Entity A:" + entityA);
		//Statics.p("BeginContact Entity B:" + entityB);

		if (entityA instanceof ICollideable) {
			ICollideable ic = (ICollideable) entityA;
			ic.collided(contact, true);
		} else if (entityB instanceof ICollideable) {
			ICollideable ic = (ICollideable) entityB;
			ic.collided(contact, false);
		}
		//Collisions.Collision(entityA, entityB);

		/*try {
			if (ba_ud.type == BodyUserData.Type.Crate || bb_ud.type == BodyUserData.Type.Crate) {
				if (ba_ud.type == BodyUserData.Type.StickyRope || bb_ud.type == BodyUserData.Type.StickyRope) {

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
		}*/

	}


	@Override
	public void beginContact(Contact contact) {
		//Statics.p("Generic collision");

		/*Body ba = contact.getFixtureA().getBody();
		Body bb = contact.getFixtureB().getBody();

		BodyUserData ba_ud = (BodyUserData)ba.getUserData();
		BodyUserData bb_ud = (BodyUserData)bb.getUserData();

		//Statics.p("BeginContact BodyUserData A:" + ba_ud);
		//Statics.p("BeginContact BodyUserData B:" + bb_ud);

		Entity entityA = ba_ud.entity;
		Entity entityB = bb_ud.entity;

		//Statics.p("BeginContact Entity A:" + entityA);
		//Statics.p("BeginContact Entity B:" + entityB);

		//if (entityA)*/

		this.collisions.add(contact);

	}


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
		synchronized (entities) {
			this.entities.remove(b);
		}		
	}


	@Override
	public void newController(IInputDevice input) {
		synchronized (newControllers) {
			this.newControllers.add(input);
		}
	}


	private void loadPlayer(IInputDevice input) {
		//PlayersAvatar avatar = new PlayersAvatar(input, world, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL * 0.65f);
		Point p = this.level.getPlayerStartPos();
		PlayersAvatar avatar = new PlayersAvatar(input, this, world, p.x, p.y);

		synchronized (avatars) {
			this.avatars.add(avatar);
		}
		this.addEntity(avatar);

		Player player = new Player();
		synchronized (players) {
			this.players.add(player);
		}

	}


	public void restartAvatar(PlayersAvatar avatar) {
		Point p = this.level.getPlayerStartPos();
		avatar.body.setTransform(new Vec2(p.x, p.y), 0);
	}


	public void addEntity(Entity o) {
		synchronized (entities) {
			if (Statics.DEBUG) {
				if (this.entities.contains(o)) {
					throw new RuntimeException(o + " has already been added");
				}
				/*if (o instanceof Body) {
					throw new RuntimeException(o + " is a Body - add DrawableBody at least!");
				}*/
			}
			this.entities.add(o);
		}
	}
}

