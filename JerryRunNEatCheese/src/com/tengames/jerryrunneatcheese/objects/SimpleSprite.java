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

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SimpleSprite extends Sprite {
	private Vector2 bodyPosition = new Vector2();

	// for normal camera
	public SimpleSprite(TextureRegion textureRegion, float x, float y) {
		this.setRegion(textureRegion);
		this.setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		this.setPosition(x, y);
	}

	// for box camera
	public SimpleSprite(float x, float y, float width, float height) {
		this.setSize(width * BoxUtility.WORLD_TO_BOX, height * BoxUtility.WORLD_TO_BOX);
		this.setPosition(x, y);
	}

	// for box camera
	public SimpleSprite(TextureRegion textureRegion, float x, float y, float width, float height) {
		this.setRegion(textureRegion);
		this.setSize(width * BoxUtility.WORLD_TO_BOX, height * BoxUtility.WORLD_TO_BOX);
		this.setPosition(x, y);
	}

	/**
	 * render Sprite
	 * 
	 * @param texture: type textureRegion
	 * @return Sprite
	 */
	public Sprite renderSprite(TextureRegion textureRegion) {
		this.setRegion(textureRegion);
		return this;
	}

	/**
	 * Render Flip Sprite
	 * 
	 * @param frameKey: TextureRegion
	 * @param xVector:  true if flip horizon
	 * @param yVector:  true if flip vertical
	 * @return Sprite
	 */
	public Sprite renderFlipSprite(TextureRegion textureRegion, boolean xVector, boolean yVector) {
		this.flip(xVector, yVector);
		this.setRegion(textureRegion);
		return this;
	}

	/**
	 * Set sprite center position
	 * 
	 * @param sprite
	 * @param x:     x
	 * @param y:     y
	 */
	public void setSpriteCenter(Sprite sprite, float x, float y) {
		sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
	}

	/**
	 * Set origin center for rotation
	 * 
	 * @param sprite
	 */
	public void setOriginCenter(Sprite sprite) {
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}

	/**
	 * get position of sprite
	 * 
	 * @return Vector2
	 */
	public Vector2 getPosition() {
		Vector2 position = new Vector2(this.getX(), this.getY());
		return position;
	}

	/**
	 * update follow model: position
	 * 
	 * @param model
	 * @param isUpdateRotation
	 */
	public void updateFollowModel(SimpleModel model, boolean isUpdateRotation) {
		// polygon, circle
		bodyPosition.x = model.getBody().getPosition().x - BoxUtility.ConvertToBox(model.parameter1.x / 2);
		bodyPosition.y = model.getBody().getPosition().y - BoxUtility.ConvertToBox(model.parameter1.y / 2);

		if (isUpdateRotation)
			this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
		this.setPosition(bodyPosition.x, bodyPosition.y);
		if (isUpdateRotation)
			this.setRotation(model.getBody().getAngle() * MathUtils.radiansToDegrees);
	}

}
