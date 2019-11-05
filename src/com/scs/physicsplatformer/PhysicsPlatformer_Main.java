package com.scs.physicsplatformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.gamepad4j.Controllers;
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

public class PhysicsPlatformer_Main implements ContactListener, NewControllerListener, KeyListener {

	public World world;
	private MainWindow window;

	private TSArrayList<Entity> entities;
	private List<IInputDevice> newControllers = new ArrayList<>();
	private List<IInputDevice> controllersToRemove = new ArrayList<>();
	private List<Player> players = new ArrayList<>();

	private DrawingSystem drawingSystem;
	private PlayerInputSystem playerInputSystem;
	private List<Contact> collisions = new LinkedList<>();
	private AbstractLevel level;
	private boolean restartLevel = false;
	private int levelNum = Statics.RELEASE_MODE ? 2 : 4; // Level 1 is too hard


	public static void main(String[] args) {
		new PhysicsPlatformer_Main();
	}


	public PhysicsPlatformer_Main() {
		super();

		window = new MainWindow(this);

		try {
			DeviceThread dt = new DeviceThread(window);
			dt.addListener(this);
			dt.start();

			Statics.img_cache = ImageCache.GetInstance(null);
			Statics.img_cache.c = window;

			drawingSystem = new DrawingSystem();
			playerInputSystem = new PlayerInputSystem();

			startLevel();
			this.gameLoop();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(window, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	private void gameLoop() {
		long interpol = 30;
		final float timeStep = 1.0f / Statics.FPS;//10.f;
		final int velocityIterations = 6;//8;//6;
		final int positionIterations = 4;//3;//2;
		
		while (window.isVisible()) {
			long start = System.currentTimeMillis();

			synchronized (newControllers) {
				while (this.newControllers.isEmpty() == false) {
					this.loadPlayer(this.newControllers.remove(0));
					//this.entities.refresh(); // To add avatars to the main list
				}
			}

			synchronized (this.controllersToRemove) {
				while (this.controllersToRemove.isEmpty() == false) {
					IInputDevice id = this.controllersToRemove.remove(0);
					for (Player player : this.players) {
						if (player.input == id) {
							this.players.remove(player);
							break;
						}
					}
					for (Entity e : this.entities) {
						if (e instanceof PlayersAvatar) {
							PlayersAvatar av = (PlayersAvatar)e;
							if (av.input == id) {
								this.entities.remove(e);
								break;
							}
						}
					}
				}
			}

			this.entities.refresh();

			boolean anyAvatars = false;

			// Preprocess
			for (Entity e : this.entities) {
				if (e instanceof IProcessable) {
					IProcessable id = (IProcessable)e;
					id.preprocess(interpol);
				}
			}

			// Player input first
			if (DeviceThread.USE_CONTROLLERS) {
				Controllers.checkControllers();
			}
			for (Entity e : this.entities) {
				if (e instanceof IPlayerControllable) {
					IPlayerControllable id = (IPlayerControllable)e;
					this.playerInputSystem.process(id);
					anyAvatars = true;
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


			// Draw screen
			Graphics g = window.BS.getDrawGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);

			g.setColor(Color.white);
			g.drawString("Level " + this.levelNum, 20, 60);
			g.drawString("Press Fire to join game", 20, 80);
			g.drawString("Press ESC to Restart", 20, 100);

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

			if (this.players.size() > 0 && anyAvatars == false) {
				this.nextLevel();
			} else if (restartLevel) {
				restartLevel = false;
				this.startLevel();
			}

			window.BS.show();

			long end = System.currentTimeMillis();
			long diff = end-start;
			try {
				interpol = (1000/Statics.FPS) - diff;
				if (interpol > 0) {
					Thread.sleep(interpol);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}


	private void startLevel() {
		entities = new TSArrayList<Entity>();

		Vec2 gravity = new Vec2(0f, 10.0f);
		world = new World(gravity);
		world.setContactListener(this);

		level = AbstractLevel.GetLevel(levelNum, this);//new Level3(this);// new TestLevel(this);//  
		level.createWorld(world, this);
		this.addEntity(level);

		// Create avatars
		for (Player player : this.players) {
			this.createAvatar(player);
		}

		//gameLoop();
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

		if (ba_ud != null && bb_ud != null) {
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



	public void removeEntity(Entity b) {
		b.cleanup(world);
		/*if (b instanceof PhysicalEntity) {
			PhysicalEntity pe = (PhysicalEntity)b;
			world.destroyBody(pe.body);
		}*/

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
		Player player = new Player(input);
		synchronized (players) {
			this.players.add(player);
		}
		this.createAvatar(player);

	}


	private void createAvatar(Player player) {
		Point p = this.level.getPlayerStartPos();
		PlayersAvatar avatar = new PlayersAvatar(player.input, this, world, p.x, p.y);
		this.addEntity(avatar);
	}


	public void restartAvatar(PlayersAvatar avatar) {
		Point p = this.level.getPlayerStartPos();
		avatar.body.setTransform(new Vec2(p.x, p.y), 0);
	}


	public void nextLevel() {
		levelNum++;
		this.startLevel();
	}


	public void addEntity(Entity o) {
		synchronized (entities) {
			if (!Statics.RELEASE_MODE) {
				if (this.entities.contains(o)) {
					throw new RuntimeException(o + " has already been added");
				}
			}
			this.entities.add(o);
		}
	}


	@Override
	public void keyPressed(KeyEvent arg0) {

	}


	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			//this.setVisible(false);
			//this.dispose();
			restartLevel = true;
			//startLevel();
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}


	@Override
	public void controllerRemoved(IInputDevice input) {
		// TODO Auto-generated method stub

	}

}

