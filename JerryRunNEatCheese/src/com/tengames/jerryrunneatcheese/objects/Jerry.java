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

import woodyx.basicapi.sound.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.tengames.jerryrunneatcheese.main.Assets;

public class Jerry extends SimpleSprite {
	public static final byte STATE_STAND = 1;
	public static final byte STATE_RUN = 2;
	public static final byte STATE_JUMP = 3;

	public static final byte UP_ROUND = 5;
	public static final byte AVG_ROUND = 3;
	public static final byte UP_SPEED = 10;
	public static final byte AVG_SPEED = 5;
	public static final float RESET_ALL = 0.2f;

	public static final short JUMP_SPEED = 320;

	private SimpleModel model;
	private TextureRegion keyFrame;
	private float stateTime, sleepJump, countDie, countCanJump, speed, round;
	private byte state;
	private boolean isStart, isFinish, isDie, isJump;

	public Jerry(World world, Vector2 position, Vector2 size) {
		super(position.x, position.y, size.x, size.y);
		// initialize state
		state = STATE_STAND;
		speed = RESET_ALL;
		round = RESET_ALL;
		keyFrame = null;
		stateTime = 0;
		sleepJump = 0;
		countDie = -1;
		countCanJump = -1;
		isJump = false;
		isStart = false;
		isFinish = false;
		isDie = false;

		// create model
		model = new SimpleModel(world, SimpleModel.DYNAMIC, SimpleModel.CIRCLE, new Vector2(size.x, size.y),
				new Vector2(), size.x / 2, new Vector2(position.x, position.y), 0, 1, 0.5f, 0.1f, "jerry");

	}

	public void startRun() {
		if (!isStart) {
			countCanJump = 0;
			speed = AVG_SPEED;
			round = AVG_ROUND;
			state = STATE_RUN;
			isStart = true;
		}
	}

	public void jump() {
		if ((state == STATE_STAND || state == STATE_RUN) && sleepJump > 0.2f && countCanJump == -2 && !isJump) {
			// play sound
			SoundManager.playSound(Assets.soJump);

			// update state: state jump
			changeState(STATE_JUMP);
			// jump
			model.getBody().setLinearVelocity(
					new Vector2(8 * UP_SPEED * Gdx.graphics.getDeltaTime(), JUMP_SPEED * Gdx.graphics.getDeltaTime()));
			// stop rotation
			model.getBody().setAngularVelocity(0);
			// change flag
			isJump = true;
			sleepJump = -1;
		}
	}

	public void changeState(byte state) {
		if (this.state != state)
			this.state = state;
	}

	public void changeSpeed(byte speed, byte round) {
		if (this.speed != speed) {
			this.speed = speed;
		}
		if (this.round != round) {
			this.round = round;
		}
	}

	public void collisTrap(int type) {
		if (countDie == -1) {
			// play sound
			SoundManager.playSound(Assets.soHurt);
			// set finish
			setFinish();
			// jump back
			changeState(STATE_JUMP);
			// jump
			model.getBody().setLinearVelocity(new Vector2(type * 8 * UP_ROUND * Gdx.graphics.getDeltaTime(),
					JUMP_SPEED * Gdx.graphics.getDeltaTime()));
			// start count
			countDie = 0;
		}
	}

	public SimpleModel getModel() {
		return model;
	}

	public boolean getStart() {
		return this.isStart;
	}

	public boolean getFinish() {
		return this.isFinish;
	}

	public void resetSleepJump() {
		if (sleepJump == -1) {
			sleepJump = 0;
		}
	}

	public void resetJump() {
		if (this.isJump)
			this.isJump = false;
	}

	public void setFinish() {
		if (!this.isFinish)
			this.isFinish = true;
	}

	public boolean getDie() {
		return this.isDie;
	}

	public void update(float deltaTime) {
		// update stateTime
		stateTime += deltaTime;

		// update sleep time
		if (sleepJump >= 0)
			sleepJump += deltaTime;

		// update count can jump
		if (countCanJump >= 0.25f)
			countCanJump = -2;
		if (countCanJump >= 0)
			countCanJump += deltaTime;

		// update follow
		if (!isDie)
			this.updateFollowModel(model, false);

		// change speed : adjust speed, round
		if (isStart && !isFinish) {
			// speed
			if (model.getBody().getLinearVelocity().x < AVG_SPEED * deltaTime)
				speed = AVG_SPEED;
			else if (model.getBody().getLinearVelocity().x > UP_SPEED * deltaTime)
				speed = RESET_ALL;

			// round
			if (Math.abs(model.getBody().getAngularVelocity()) < AVG_ROUND * deltaTime)
				round = AVG_ROUND;
			else if (Math.abs(model.getBody().getAngularVelocity()) > UP_ROUND * deltaTime)
				round = RESET_ALL;

			// change speed : start run
			model.getBody().applyAngularImpulse(-round * deltaTime);
			model.getBody().applyLinearImpulse(new Vector2(speed * deltaTime, 0), model.getBody().getWorldCenter());
		}

		// check die
		if (this.getY() <= -1 && !isDie) {
			model.getBody().setActive(false);
			isDie = true;
		}

		// check count die
		if (countDie >= 0)
			countDie += deltaTime;
		if (countDie >= 1 && !isDie) {
			model.getBody().setActive(false);
			isDie = true;
		}
	}

	public void render(SpriteBatch batch) {
		switch (state) {
		case STATE_STAND:
			keyFrame = Assets.aniStand.getKeyFrame(stateTime, true);
			break;
		case STATE_RUN:
			keyFrame = Assets.aniRun.getKeyFrame(stateTime, true);
			break;
		case STATE_JUMP:
			keyFrame = Assets.aniJump.getKeyFrame(stateTime, false);
			break;
		default:
			break;
		}
		this.renderSprite(keyFrame).draw(batch);
	}
}
