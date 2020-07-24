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
package com.tengames.jerryrunneatcheese.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import woodyx.basicapi.accessor.ActorAccessor;
import woodyx.basicapi.physics.BoxUtility;
import woodyx.basicapi.sound.SoundManager;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.tengames.jerryrunneatcheese.interfaces.GlobalVariables;
import com.tengames.jerryrunneatcheese.main.Assets;
import com.tengames.jerryrunneatcheese.main.JerryRunNEatCheese;
import com.tengames.jerryrunneatcheese.objects.CommonModelList;
import com.tengames.jerryrunneatcheese.objects.DynamicButton;
import com.tengames.jerryrunneatcheese.objects.DynamicDialog;
import com.tengames.jerryrunneatcheese.objects.IconModel;
import com.tengames.jerryrunneatcheese.objects.Jerry;
import com.tengames.jerryrunneatcheese.objects.Nature;
import com.tengames.jerryrunneatcheese.objects.Terran;

public class ScreenGame implements Screen, InputProcessor {
	private static int countObjects = 0;

	public static final byte STATE_NULL = 0;
	public static final byte STATE_WIN = 1;
	public static final byte STATE_LOOSE = 2;

	private JerryRunNEatCheese coreGame;
	private SpriteBatch batchObject, batchBackground;
	private World world;
//	private Box2DDebugRenderer renderer;
	private OrthographicCamera cameraObject, cameraBackground;
	private Stage stage;
	private Image imgDark;
	private Label lbHelp;
	private TweenManager tweenEffect;
	private ArrayList<Terran> terrans;
	private Jerry jerry;
	private DynamicDialog dialog;
	private DynamicButton[] buttons;
	private BufferedReader reader;
	private Nature[] clouds;
	private String strJson;
	private int numLevel, numCake1, numCake2, numCake3, levelScore, realScore;
	private byte state;
	private boolean isReady;
//	private Skin skin;

	public ScreenGame(JerryRunNEatCheese coreGame, String strJson, int numLevel) {
		this.coreGame = coreGame;
		this.strJson = strJson;
		this.numLevel = numLevel;
		initialize();
	}

	public void initialize() {
		// create world
		world = new World(GlobalVariables.GRAVITY, true);

		// create debuger
//		renderer = new Box2DDebugRenderer();

		// create batch, camera
		cameraBackground = new OrthographicCamera(800 * BoxUtility.WORLD_TO_BOX, 480 * BoxUtility.WORLD_TO_BOX);
		cameraBackground.setToOrtho(false, 800 * BoxUtility.WORLD_TO_BOX, 480 * BoxUtility.WORLD_TO_BOX);
		cameraBackground.position.set(400 * BoxUtility.WORLD_TO_BOX, 240 * BoxUtility.WORLD_TO_BOX, 0);

		cameraObject = new OrthographicCamera(800 * BoxUtility.WORLD_TO_BOX, 480 * BoxUtility.WORLD_TO_BOX);
		cameraObject.position.set(400 * BoxUtility.WORLD_TO_BOX, 240 * BoxUtility.WORLD_TO_BOX, 0);

		batchBackground = new SpriteBatch();
		batchObject = new SpriteBatch();

		// create stage
		stage = new Stage(800, 480, true);
		((OrthographicCamera) stage.getCamera()).setToOrtho(false, 800, 480);
		InputMultiplexer input = new InputMultiplexer(stage, this);

		// set input
		Gdx.input.setInputProcessor(input);
		Gdx.input.setCatchBackKey(true);

		// create smt
		initializeParams();
		createUI();
		createArrays();
		createModels();
		// check contact listener
		checkCollision();
	}

	private void createArrays() {
		// create array of terrans
		terrans = new ArrayList<Terran>();
	}

	private void initializeParams() {
		// get data
		SoundManager.MUSIC_ENABLE = coreGame.androidListener.getSound();
		SoundManager.SOUND_ENABLE = coreGame.androidListener.getSound();

		// play music
		SoundManager.playMusic(Assets.muBgGame, 0.5f, true);

		/* loading data */
//		try {
//		reader = new BufferedReader(new FileReader("/home/woodyx/workspace-gdx/JerryRunNEatCheeseAndroid/assets/data/jerryrunneatcheese.txt"));
//		reader = new BufferedReader(new FileReader("jerryrunneatcheese.txt"));
//		} catch (FileNotFoundException e) {}
		reader = coreGame.androidListener.getData();

		// create clouds, butterfly
		clouds = new Nature[10];
		for (int i = 0; i < 5; i++) {
			clouds[i] = new Nature(1);
		}
		for (int i = 5; i < clouds.length; i++) {
			clouds[i] = new Nature(2);
		}

		// initialize params
		tweenEffect = new TweenManager();
		state = STATE_NULL;
		numCake1 = 0;
		numCake2 = 0;
		numCake3 = 0;
		levelScore = 0;
		realScore = 0;
		isReady = false;

		// trace
		if (numLevel < 10) {
			coreGame.androidListener.traceScene("0" + numLevel);
		} else {
			coreGame.androidListener.traceScene(numLevel + "");
		}

		// show admob
		if (numLevel % 3 == 0)
			coreGame.androidListener.showIntertitial();
	}

	private void createModels() {
		// create models
		if (strJson != null)
			createJsonModels();
	}

	private void createUI() {
		// load skin
//		skin = new Skin(Gdx.files.internal("drawable/objects/uiskin.json"), Assets.taSkin);

		// creat buttons
		buttons = new DynamicButton[6];

		// label stage
		buttons[0] = new DynamicButton(("Level: " + numLevel), new Vector2(10, 440), 0.5f);

		// label score
		buttons[1] = new DynamicButton(("Score: " + realScore), new Vector2(160, 440), 0.7f);

		// button menu
		buttons[2] = new DynamicButton(Assets.taObjects.findRegion("ic-menu"), new Vector2(680, 420), 0.5f, 1.1f);
		buttons[2].getButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				 play sound
				SoundManager.playSound(Assets.soClick);
				// back to screen stage
				coreGame.setScreen(new ScreenStage(coreGame));
			}
		});

		// button replay
		buttons[3] = new DynamicButton(Assets.taObjects.findRegion("ic-replay"), new Vector2(740, 420), 0.5f, 1.3f);
		buttons[3].getButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soClick);
				// replay
				coreGame.setScreen(new ScreenGame(coreGame, strJson, numLevel));
			}
		});

		// button sound
		buttons[4] = new DynamicButton(Assets.taObjects.findRegion("bt-soundon"),
				Assets.taObjects.findRegion("bt-soundoff"), new Vector2(740, 20), 0.6f, 0.5f);
		buttons[4].getButton().setChecked(!SoundManager.SOUND_ENABLE);

		buttons[4].getButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soClick);
				// turn off sound and music
				SoundManager.MUSIC_ENABLE = !SoundManager.MUSIC_ENABLE;
				if (!SoundManager.MUSIC_ENABLE) {
					SoundManager.pauseMusic(Assets.muBgGame);
				} else {
					SoundManager.playMusic(Assets.muBgGame, 0.5f, true);
				}
				SoundManager.SOUND_ENABLE = !SoundManager.SOUND_ENABLE;
				coreGame.androidListener.setSound(SoundManager.SOUND_ENABLE);
			}
		});

		// back to screen scenario
		/*
		 * TextButton btBack = new TextButton("Back", skin); btBack.setPosition(20,
		 * 400); btBack.addListener(new ChangeListener() {
		 * 
		 * @Override public void changed(ChangeEvent event, Actor actor) {
		 * coreGame.setScreen(new ScreenScenario(coreGame)); } });
		 */

		// add to stage
//		 stage.addActor(btBack);
		for (DynamicButton button : buttons) {
			if (button != null)
				stage.addActor(button);
		}

		if (numLevel == 1) {
			// add dark
			imgDark = new Image(Assets.taObjects.findRegion("dark"));
			imgDark.setPosition(0, 0);
			imgDark.setSize(800, 480);

			// add text
			String text = "Tap screen to start and to jump!\n\n\nTry to collect cheeses as much as possible\n\nand be careful with traps!";
			lbHelp = new Label(text, Assets.lbSNumber);
			lbHelp.setAlignment(Align.center);
			lbHelp.setPosition((800 - lbHelp.getWidth()) / 2, (480 - lbHelp.getHeight()) / 2);

			// add to stage
			stage.addActor(imgDark);
			stage.addActor(lbHelp);
		}
	}

	/* generate map */
	private void createJsonModels() {
		Json json = new Json();
		CommonModelList jsList = new CommonModelList();
		jsList = json.fromJson(CommonModelList.class, strJson);
		// generate map
		for (int i = 0; i < jsList.getSize(); i++) {
			switch (jsList.getModel(i).getType()) {
			/* create jerry */
			case IconModel.JERRY:
				if (jerry == null)
					jerry = new Jerry(world, jsList.getModel(i).getPosition(), jsList.getModel(i).getSize());
				break;
			/* create objects */
			// sensor
			case IconModel.CAKE_1:
				numCake1++;
				Terran cake1 = new Terran(world, jsList.getModel(i).getType(), jsList.getModel(i).getPosition(),
						jsList.getModel(i).getSize(), jsList.getModel(i).getRotation(), countObjects);
				terrans.add(cake1);
				countObjects++;
				break;
			case IconModel.CAKE_2:
				numCake2++;
				Terran cake2 = new Terran(world, jsList.getModel(i).getType(), jsList.getModel(i).getPosition(),
						jsList.getModel(i).getSize(), jsList.getModel(i).getRotation(), countObjects);
				terrans.add(cake2);
				countObjects++;
				break;
			case IconModel.CAKE_3:
				numCake3++;
				Terran cake3 = new Terran(world, jsList.getModel(i).getType(), jsList.getModel(i).getPosition(),
						jsList.getModel(i).getSize(), jsList.getModel(i).getRotation(), countObjects);
				terrans.add(cake3);
				countObjects++;
				break;
			case IconModel.CUP:
			case IconModel.TRAP_1:
			case IconModel.TRAP_2:
				Terran sensor = new Terran(world, jsList.getModel(i).getType(), jsList.getModel(i).getPosition(),
						jsList.getModel(i).getSize(), jsList.getModel(i).getRotation(), countObjects);
				terrans.add(sensor);
				countObjects++;
				break;
			// normal
			case IconModel.BOX:
			case IconModel.BRICK:
			case IconModel.ICE_BAR:
			case IconModel.WOOD_BAR:
			case IconModel.WOOD_MUD:
				Terran normal = new Terran(world, jsList.getModel(i).getType(), jsList.getModel(i).getPosition(),
						jsList.getModel(i).getSize(), jsList.getModel(i).getRotation(), -1);
				terrans.add(normal);
				break;
			default:
				break;
			}
		}

		// free models
		jsList.dispose();
		jsList = null;

		// result
		levelScore = numCake1 * 10 + numCake2 * 20 + numCake3 * 30;
	}

	private void export(int number) {
		String line = null, strExport = "MAP: " + (number + 1);
		try {
			while ((line = reader.readLine()) != null) {
				if (line.equals(strExport)) {
					// export strJson
					strJson = reader.readLine();
					break;
				}
			}
			// close reader
			reader.close();
			// set new Screen
			coreGame.setScreen(new ScreenGame(coreGame, strJson, (number + 1)));
		} catch (IOException e) {
		}
	}

	private void createEffectScore() {
		Timeline.createSequence().beginParallel()
				.push(Tween.to(buttons[1], ActorAccessor.SCALE_XY, 0.5f).ease(Elastic.OUT).target(1.2f, 1.2f))
				.push(Tween.to(buttons[1].getLabel(), ActorAccessor.TINT, 0.5f).ease(Elastic.OUT).target(1, 0, 0)).end()
				.beginParallel()
				.push(Tween.to(buttons[1].getLabel(), ActorAccessor.TINT, 0.5f).ease(Linear.INOUT).target(1, 1, 1))
				.push(Tween.to(buttons[1], ActorAccessor.SCALE_XY, 1f).ease(Linear.INOUT).target(1, 1)).end()
				.start(tweenEffect);
	}

	private void createDialog(byte type) {
		// pause music
		SoundManager.pauseMusic(Assets.muBgGame);

		// play sound
		switch (type) {
		case STATE_WIN:
			SoundManager.playSound(Assets.soWin);
			break;
		case STATE_LOOSE:
			SoundManager.playSound(Assets.soFail);
			break;
		default:
			break;
		}

		// change input
		Gdx.input.setInputProcessor(stage);

		// show dialog
		Image imgDark = new Image(Assets.taObjects.findRegion("dark"));
		imgDark.setSize(800, 480);
		imgDark.setPosition(0, 0);

		WindowStyle windowStyle = new WindowStyle();
		windowStyle.titleFont = Assets.fNumber;
		dialog = new DynamicDialog(windowStyle, Assets.taObjects.findRegion("dialog"), new Vector2(374, 292),
				new Vector2(400, 900), new Vector2(400, 240), realScore, levelScore, type);

		if (type == STATE_WIN) {
			// save data
			coreGame.androidListener.setValue(numLevel - 1, dialog.getValue());

			// save hscore
			coreGame.androidListener.saveHscore(realScore);

			// unlock new stage
			if (coreGame.androidListener.getValue(numLevel) == 0)
				coreGame.androidListener.setValue(numLevel, 1);
		}

		dialog.getBtMenu().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				 play sound
				SoundManager.playSound(Assets.soClick);
				// back to screen stage
				coreGame.setScreen(new ScreenStage(coreGame));
			}
		});

		dialog.getBtReplay().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soClick);
				// replay
				coreGame.setScreen(new ScreenGame(coreGame, strJson, numLevel));
			}
		});

		if (dialog.getBtNext() != null) {
			dialog.getBtNext().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// play sound
					SoundManager.playSound(Assets.soClick);
					// return stage menu
					if (numLevel == 15) {
						coreGame.setScreen(new ScreenStage(coreGame));
					} else {
						// next level
						coreGame.androidListener.setValue(numLevel, 1);
						export(numLevel);
					}
				}
			});
		}

		// add to stage
		stage.addActor(imgDark);
		stage.addActor(dialog);
	}

	public void update(float deltaTime) {
		updateWorld(deltaTime);
		updateJerry(deltaTime);
		updateCamera();
		updateObjects(deltaTime);
		updateStage(deltaTime);
	}

	private void updateWorld(float deltaTime) {
		world.step(deltaTime, 8, 3);
	}

	Vector3 camPoint = new Vector3();

	private void updateCamera() {
		// process batchs
		batchBackground.setProjectionMatrix(cameraBackground.combined);
		batchObject.setProjectionMatrix(cameraObject.combined);

		// update follow jerry
		cameraObject.project(camPoint.set(jerry.getModel().getPosition().x, jerry.getModel().getPosition().y, 0));
		cameraObject.position.x = jerry.getModel().getPosition().x;

		// update camera
		cameraObject.update();
	}

	private void updateJerry(float deltaTime) {
		// update jerry
		if (jerry != null && !jerry.getDie())
			jerry.update(deltaTime);
		// check finish
		if (jerry.getDie() && state == STATE_NULL) {
			createDialog(STATE_LOOSE);
			// state fail
			state = STATE_LOOSE;
		}
	}

	private void updateObjects(float deltaTime) {
		// update terran
		if (!terrans.isEmpty()) {
			for (int i = 0; i < terrans.size(); i++) {
				if (terrans.get(i) != null && terrans.get(i).getModel() != null) {
					// check if terran out off camera --> set die
					if ((terrans.get(i).getModel().getPosition().x
							+ terrans.get(i).getWidth()) <= (cameraObject.position.x - cameraObject.viewportWidth)) {
						terrans.get(i).setDie();
					}
					// check if terran in camera --> update
					if (terrans.get(i).getModel().getPosition().x <= (cameraObject.position.x
							+ cameraObject.viewportWidth)) {
						terrans.get(i).update(deltaTime);
					}
					// remove dead terran
					if (terrans.get(i).getCanRemove()) {
						terrans.remove(i);
					}
				}
			}
		}

		// update cloud
		for (Nature cloud : clouds) {
			if (cloud != null)
				cloud.update(deltaTime);
		}
	}

	private void updateStage(float deltaTime) {
		// update tween
		tweenEffect.update(deltaTime);
		// update dialog
		if (dialog != null)
			dialog.update(deltaTime);
		// update buttons
		for (DynamicButton button : buttons) {
			if (button != null)
				button.update(deltaTime);
		}
	}

	public void draw() {
		renderBackGround();
		renderObjects();
		renderStage();
	}

	public void bgDrawableBackground(boolean flag) {
		if (flag) {
			batchBackground.disableBlending();
			batchBackground.begin();
		} else {
			batchBackground.end();
		}
	}

	public void objDrawableBackground(boolean flag) {
		if (flag) {
			batchBackground.enableBlending();
			batchBackground.begin();
		} else {
			batchBackground.end();
		}
	}

	public void objDrawableObject(boolean flag) {
		if (flag) {
			batchObject.enableBlending();
			batchObject.begin();
		} else {
			batchObject.end();
		}
	}

	private void renderBackGround() {
		// render background
		bgDrawableBackground(true);
		batchBackground.draw(Assets.txBgGame, 0, 0, 800 * BoxUtility.WORLD_TO_BOX, 480 * BoxUtility.WORLD_TO_BOX);
		bgDrawableBackground(false);
		// render clouds
		objDrawableBackground(true);
		renderClouds();
		objDrawableBackground(false);
	}

	private void renderObjects() {
		objDrawableObject(true);
		renderTerrans();
		renderJerry();
		objDrawableObject(false);
	}

	private void renderTerrans() {
		if (!terrans.isEmpty()) {
			for (Terran terran : terrans) {
				// check if terran in camera --> update
				if ((terran.getModel() != null) && (!terran.getCanRemove())) {
					if (terran.getModel().getPosition().x <= (cameraObject.position.x + cameraObject.viewportWidth)) {
						terran.draw(batchObject);
					}
				}
			}
		}
	}

	private void renderJerry() {
		if (jerry != null && !jerry.getDie())
			jerry.render(batchObject);
	}

	private void renderClouds() {
		for (Nature cloud : clouds) {
			if (cloud != null)
				cloud.render(batchBackground);
		}
	}

	private void renderStage() {
		stage.draw();
	}

//	private void renderDebug() {
//		renderer.render(world, cameraObject.combined);
//	}

	@Override
	public void render(float deltaTime) {
		update(deltaTime);
		clearScreen(deltaTime);
		draw();
//		renderDebug();
	}

	public void clearScreen(float deltaTime) {
		// clear screen
		Gdx.gl20.glClearColor(1, 1, 1, 1);
		Gdx.gl20.glClearDepthf(1.0f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_BLEND);

		// clear world
		world.clearForces();
	}

	private void checkCollision() {
		world.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void beginContact(Contact contact) {
				// change state jerry : state run
				if (jerry.getStart() && BoxUtility.detectCollision(contact, jerry.getModel().getBody(), "terran")) {
					jerry.changeState(Jerry.STATE_RUN);
					// reset jump
					jerry.resetJump();
					jerry.resetSleepJump();
				}

				// jerry contacts terrans
				for (Terran terran : terrans) {
					if (terran != null && terran.getModel() != null && !terran.getCanRemove()) {
						if (BoxUtility.detectCollision(contact, jerry.getModel().getBody(),
								terran.getModel().getBody())) {
							switch (terran.getType()) {
							// jerry eats cheese
							case IconModel.CAKE_1:
							case IconModel.CAKE_2:
							case IconModel.CAKE_3:
								if (!terran.getDie()) {
									// increase score
									realScore += terran.getValue();
									// set text
									buttons[1].setText("Score: " + realScore);
									// effect for text
									createEffectScore();
									// turn off flag
									terran.setDie();
								}
								break;
							// jerry collision with traps
							case IconModel.TRAP_1:
							case IconModel.TRAP_2:
								jerry.collisTrap(-1);
								break;
							// win
							case IconModel.CUP:
								// finish
								jerry.collisTrap(1);
								// state : win
								if (state == STATE_NULL) {
									createDialog(STATE_WIN);
									// win
									state = STATE_WIN;
								}
								break;
							default:
								break;
							}
						}
					}
				}
			}
		});
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// play music
		SoundManager.playMusic(Assets.muBgGame, 0.5f, true);
	}

	@Override
	public void hide() {
		// pause music, sound
		SoundManager.pauseMusic(Assets.muBgGame);
		switch (state) {
		case STATE_LOOSE:
			SoundManager.stopSound(Assets.soFail);
			break;
		case STATE_WIN:
			SoundManager.stopSound(Assets.soWin);
			break;
		default:
			break;
		}
	}

	@Override
	public void pause() {
		// pause music, sound
		SoundManager.pauseMusic(Assets.muBgGame);
		switch (state) {
		case STATE_LOOSE:
			SoundManager.stopSound(Assets.soFail);
			break;
		case STATE_WIN:
			SoundManager.stopSound(Assets.soWin);
			break;
		default:
			break;
		}
	}

	@Override
	public void resume() {
		// play music
		SoundManager.playMusic(Assets.muBgGame, 0.5f, true);
	}

	@Override
	public void dispose() {
		world.dispose();
		stage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK) {
			// play sound
			SoundManager.playSound(Assets.soClick);
			// back to menu
			coreGame.setScreen(new ScreenStage(coreGame));
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	Vector3 touchPoint = new Vector3();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		cameraObject.unproject(touchPoint.set(screenX, screenY, 0));
		// show help
		if (!isReady && (numLevel == 1)) {
			imgDark.setVisible(false);
			lbHelp.setVisible(false);
			imgDark.remove();
			lbHelp.remove();
			isReady = true;
		}

		// start run
		jerry.startRun();
		jerry.jump();
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
