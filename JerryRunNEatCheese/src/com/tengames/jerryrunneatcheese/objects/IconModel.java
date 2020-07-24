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

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IconModel extends SimpleSprite {

	public static final byte JERRY = 1;
	public static final byte BOX = 2;
	public static final byte BRICK = 3;
	public static final byte CAKE_1 = 4;
	public static final byte CAKE_2 = 5;
	public static final byte CAKE_3 = 6;
	public static final byte CUP = 7;
	public static final byte ICE_BAR = 8;
	public static final byte TRAP_1 = 9;
	public static final byte TRAP_2 = 10;
	public static final byte WOOD_BAR = 11;
	public static final byte WOOD_MUD = 12;

	private TextureRegion region;
	private boolean isTouch, canTouch;
	private byte type;

	public IconModel(TextureRegion texture, float x, float y, byte type, float scaleX, float scaleY) {
		super(texture, x, y);
		this.type = type;
		this.region = texture;
		this.setScale(scaleX, scaleY);
		setTouch(false);
		setCanTouch(false);
	}

	public TextureRegion getRegion() {
		return this.region;
	}

	public byte getType() {
		return type;
	}

	public boolean getTouch() {
		return isTouch;
	}

	public void setTouch(boolean isTouch) {
		this.isTouch = isTouch;
	}

	public boolean getCanTouch() {
		return canTouch;
	}

	public void setCanTouch(boolean canTouch) {
		this.canTouch = canTouch;
	}

	public boolean checkTouch(float x, float y) {
		if (x >= this.getX() && x <= (this.getX() + this.getWidth() * this.getScaleX()) && y >= this.getY()
				&& (y <= (this.getY() + this.getHeight() * this.getScaleY()))) {
			setTouch(true);
			return true;
		}
		return false;
	}
}
