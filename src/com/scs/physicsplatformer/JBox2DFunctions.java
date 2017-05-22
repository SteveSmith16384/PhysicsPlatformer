package com.scs.physicsplatformer;

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
import org.jbox2d.particle.ParticleGroupDef;
import org.jbox2d.particle.ParticleType;

public class JBox2DFunctions {

	public static Body AddCircle(Object name, World world, float centre_x, float centre_y, float rad, float weight_kgm2, float friction, float restitution) {
		BodyDef bd = new BodyDef();
		bd.position.set(centre_x, centre_y);  
		bd.type = BodyType.DYNAMIC;
		bd.userData = name;

		CircleShape cs = new CircleShape();
		cs.m_radius = rad;  

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = weight_kgm2;
		fd.friction = friction;//0.3f;
		fd.restitution = restitution;//0.5f;

		Body ball = world.createBody(bd);
		ball.createFixture(fd);
		ball.setUserData(name);

		return ball;
	}


	public static Body AddRectangleTL(BodyUserData name, World world, float x, float y, 
			float width, float height, BodyType bodytype, float restitution, float friction, float weight_kgm2) {
		
		return AddRectangle(name, world, x+(width/2), y+(height/2), width, height, bodytype, restitution, friction, weight_kgm2);
	}
	
	
	public static Body AddRectangle(BodyUserData name, World world, float centre_x, float centre_y, float width, float height, BodyType bodytype, float restitution, float friction, float weight_kgm2) {
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width/2,height/2);

		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
		fd.restitution = restitution;
		fd.density = weight_kgm2;//2f; // Weight kg/m2
		fd.friction = friction;

		BodyDef bd = new BodyDef();
		bd.type = bodytype;//BodyType.STATIC;
		bd.position= new Vec2(centre_x, centre_y);//HEIGHT);//-10f);
		//bd.userData = name + "_BodyDef";

		Body b = world.createBody(bd);
		b.createFixture(fd);
		b.setUserData(name);

		return b;
	}


	public static Body AddEdgeShapeByMiddle(World world, float x1, float y1, float x2, float y2, BodyType bodyType, 
			float density, float restitution, float friction){
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


	public static Body AddEdgeShapeByTL(World world, Object name, float x1, float y1, float x2, float y2, 
			BodyType bodyType, float restitution, float friction) {
		EdgeShape es=new EdgeShape();
		es.set(new Vec2(0, 0), new Vec2(x2-x1, y2-y1));

		FixtureDef fixtureDef=new FixtureDef();
		fixtureDef.density=1;
		fixtureDef.restitution = restitution;//0.4f;
		fixtureDef.friction = friction;
		fixtureDef.shape = es;

		BodyDef bd = new BodyDef();
		bd.type = bodyType;
		bd.position= new Vec2(x1, y1);
		//bd.userData = name + "_BodyDef";

		Body b = world.createBody(bd);
		b.createFixture(fixtureDef);
		b.setUserData(name);
		return b;
	}


	public static Body AddChainShape(World world, Object name, float x, float y, Vec2 vertices[], 
			BodyType bodyType, float restitution, float friction) {
		
		ChainShape es = new ChainShape();
		es.createChain(vertices, vertices.length);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density=1;
		fixtureDef.restitution = restitution;//0.4f;
		fixtureDef.friction = friction;//0.4f;
		fixtureDef.shape = es;

		BodyDef bd = new BodyDef();
		bd.type = bodyType;
		bd.position= new Vec2(x, y);
		//bd.userData = name + "_BodyDef";

		Body b = world.createBody(bd);
		b.createFixture(fixtureDef);
		b.setUserData(name);
		return b;
	}


	/*
	 * Giving rope too many segments compared to length makes it too elastic
	 */
	/*public static List<RevoluteJointDef> AddRopeShape(Object name, Object name_end, World world, Body anchorBody, 
			float tot_len, int num_segments) {
		final float len = tot_len/num_segments;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.125f, len);

		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = 1f; // Was 0.1, made rope strtech like crazy
		fd.friction = 0.2f;
		fd.restitution = .1f;
		fd.filter.categoryBits = 0x0001;
		fd.filter.maskBits = 0xFFFF & ~0x0002;

		List<RevoluteJointDef> jointlist = new ArrayList<RevoluteJointDef>();
		
		Body prev = anchorBody;
		for (int i = 0; i < num_segments; ++i) {
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DYNAMIC;
			bd.position.set(prev.getPosition().x, prev.getPosition().y+(len*2));
			Body body = world.createBody(bd);
			body.createFixture(fd);
			if (i+1 == num_segments) {
				body.setUserData(name_end);
			} else {
				body.setUserData(name);
			}
			list.add(body);

			RevoluteJointDef jd = new RevoluteJointDef();
			jd.collideConnected = false;
			jd.initialize(prev, body, new Vec2(prev.getPosition().x, prev.getPosition().y+(len)));
			world.createJoint(jd);
			jointlist.add(jd);
			prev = body;
		}
		return jointlist;
	}*/
	

	public static void AddWater(World world, Vec2 centre) {
		// Water
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(40, 8, centre, 0);
		ParticleGroupDef pd = new ParticleGroupDef();
		pd.flags = ParticleType.b2_tensileParticle;// | ParticleType.b2_viscousParticle;
		pd.shape = shape;
		pd.strength = .01f;

		world.createParticleGroup(pd);
		world.setParticleRadius(.5f);
		//world.setParticleDamping(.5f);
		world.setParticleDensity(1f);
		//world.setParticleGravityScale(2f);
		//world.setpa
	}
	
}
