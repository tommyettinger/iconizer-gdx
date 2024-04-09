/*
 * Copyright (c) 2023 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tommyettinger.iconizer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.Gdx.input;

public class DisplayDemo extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;
    public static final int SCREEN_HEIGHT = 512;
    private SpriteBatch batch;
    private Viewport screenView;
    private Texture blank;
    private long lastProcessedTime = 0L;
    private Iconizer iconizer;
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Random Icon Display Demo");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(true);
//        config.setResizable(false);

        final DisplayDemo app = new DisplayDemo();
        new Lwjgl3Application(app, config);
    }

    @Override
    public void create() {
        iconizer = new Iconizer();
        blank = new Texture(512, 512, Pixmap.Format.RGBA8888);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Pixmap b = iconizer.generate(128, 128, System.nanoTime() + x * 12345L + y);
                blank.draw(b, x * 128, y * 128);
                b.dispose();
            }
        }
//        blank.draw(iconizer.generate(72, 72, System.nanoTime()), 0, 0);
        batch = new SpriteBatch(1000);
        screenView = new ScreenViewport();
        screenView.getCamera().position.set(SCREEN_WIDTH * 0.5f, SCREEN_HEIGHT * 0.5f, 0);
        screenView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.enableBlending();
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        batch.setProjectionMatrix(screenView.getCamera().combined);
        batch.begin();
        batch.draw(blank, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
//        screenView.update(width, height);
//        screenView.getCamera().position.set(width * 0.5f, height * 0.5f, 0f);
//        screenView.getCamera().update();
    }

    public void handleInput() {
        if (input.isKeyPressed(Input.Keys.Q) || input.isKeyPressed(Input.Keys.ESCAPE)) //quit
            Gdx.app.exit();
        else if (TimeUtils.timeSinceMillis(lastProcessedTime) > 150) {
            lastProcessedTime = TimeUtils.millis();
            if (input.isKeyPressed(Input.Keys.R)) // random
            {
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) {
                        Pixmap b = iconizer.generate(128, 128, System.nanoTime() + x * 12345L + y);
                        blank.draw(b, x * 128, y * 128);
                        b.dispose();
                    }
                }
            }
        }
    }
}
