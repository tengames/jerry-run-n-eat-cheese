/*
The MIT License

Copyright (c) 2014 kong <tengames.inc@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tengames.jerryrunneatcheese.objects;

import woodyx.basicapi.physics.BoxUtility;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Class Model, usually use dimension in box
 * 
 * @author kong
 *
 */
public class SimpleModel {
	public static final byte CHAIN = 0;
	public static final byte CIRCLE = 1;
	public static final byte EDGE = 2;
	public static final byte POLYGON = 3;

	public static final byte STATIC = 4;
	public static final byte DYNAMIC = 5;
	public static final byte KINEMATIC = 6;

	public Vector2 parameter1, parameter2;
	public float circleRadius;

	protected Body body;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;

	private ChainShape chainShape;
	private CircleShape circleShape;
	private EdgeShape edgeShape;
	private PolygonShape polygonShape;

	private Vector2 bodyOrigin;

	public SimpleModel(World world, byte type, byte shape, Vector2 parameter1, Vector2 parameter2, float circleRadius,
			Vector2 position, float angle, float density, float friction, float restitution, String user) {
		this.parameter1 = parameter1;
		this.parameter2 = parameter2;
		this.circleRadius = circleRadius;

		// create a bodyDef
		bodyDef = new BodyDef();
		bodyDef.type = getBodyType(type);

		// create a fixtureDef
		fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;

		// create bodyShape
		createShape(shape, parameter1, parameter2, circleRadius);

		// create model, set position in box
		body = world.createBody(bodyDef);
		body.setUserData(user);
		body.createFixture(fixtureDef);
		body.setTransform(BoxUtility.ConvertToBox(position.x + parameter1.x / 2),
				BoxUtility.ConvertToBox(position.y + parameter1.y / 2), (angle * MathUtils.degreesToRadians));

		// dispose shape
		disposeShape(shape);

	}

	/**
	 * get body type
	 * 
	 * @param type
	 * @return bodyType
	 */
	private BodyType getBodyType(byte type) {
		switch (type) {
		case STATIC:
			return BodyType.StaticBody;
		case DYNAMIC:
			return BodyType.DynamicBody;
		case KINEMATIC:
			return BodyType.KinematicBody;
		}
		return null;
	}

	/**
	 * get the body of the model
	 * 
	 * @return: body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * make body null
	 */
	public void makeBodyNull() {
		if (body != null)
			body = null;
	}

	/**
	 * get the model origin
	 * 
	 * @return bodyOrigin
	 */
	public Vector2 getBodyOrigin() {
		return bodyOrigin;
	}

	/**
	 * get chain shape in box
	 * 
	 * @return chainShape
	 */
	public ChainShape getChainShape() {
		return chainShape;
	}

	/**
	 * get edge shape in box
	 * 
	 * @return edgeShape
	 */
	public EdgeShape getEdgeShape() {
		return edgeShape;
	}

	/**
	 * get circle shape in box
	 * 
	 * @return circleShape
	 */
	public CircleShape getCircleShape() {
		return circleShape;
	}

	/**
	 * get polygon shape in box
	 * 
	 * @return polygonShape
	 */
	public PolygonShape getPolygonShape() {
		return polygonShape;
	}

	/**
	 * make balloon style for model usually before world.step()
	 * 
	 * @param gravity: gravity of the world
	 * @param against: velocity (x, y) when the gravity is disposed
	 */
	public void makeBalloonStyle(Vector2 gravity, Vector2 against) {
		body.applyForceToCenter(
				new Vector2((-gravity.x + against.x) * body.getMass(), (-gravity.y + against.y) * body.getMass()));
	}

	/**
	 * get present position of body in box
	 * 
	 * @return bodyPosition
	 */
	public Vector2 getPosition() {
		return body.getPosition();
	}

	/**
	 * choose the basic shape
	 * 
	 * @param shape: chain, edge, circle, polygon
	 */
	private void createShape(byte shape, Vector2 parameter1, Vector2 parameter2, float circleRadius) {
		switch (shape) {
		case CHAIN:
			chainShape = new ChainShape();
			chainShape.setNextVertex(BoxUtility.ConvertToBox(parameter1.x), BoxUtility.ConvertToBox(parameter1.y));
			chainShape.setPrevVertex(BoxUtility.ConvertToBox(parameter2.x), BoxUtility.ConvertToBox(parameter2.y));
			fixtureDef.shape = chainShape;
			break;

		case EDGE:
			edgeShape = new EdgeShape();
			edgeShape.set(BoxUtility.ConvertToBox(parameter1.x), BoxUtility.ConvertToBox(parameter1.y),
					BoxUtility.ConvertToBox(parameter2.x), BoxUtility.ConvertToBox(parameter2.y));
			fixtureDef.shape = edgeShape;
			break;

		case CIRCLE:
			circleShape = new CircleShape();
			circleShape.setRadius(BoxUtility.ConvertToBox(circleRadius));
			fixtureDef.shape = circleShape;
			break;

		case POLYGON:
			polygonShape = new PolygonShape();
			polygonShape.setAsBox(BoxUtility.ConvertToBox(parameter1.x / 2), BoxUtility.ConvertToBox(parameter1.y / 2));
			fixtureDef.shape = polygonShape;
			break;
		}
	}

	/**
	 * dispose shape
	 * 
	 * @param shape: shape
	 */
	private void disposeShape(byte shape) {
		switch (shape) {
		case CHAIN:
			chainShape.dispose();
			break;

		case EDGE:
			edgeShape.dispose();
			break;

		case CIRCLE:
			circleShape.dispose();
			break;

		case POLYGON:
			polygonShape.dispose();
			break;
		}
	}

}
