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
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.IOException;

import static com.badlogic.gdx.Gdx.input;

public class BasicOutputDemo extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 72;
    public static final int SCREEN_HEIGHT = 72;
    private final String[] args;

    public BasicOutputDemo(String[] arg) {
        this.args = arg;
    }

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Basic Output Demo");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.disableAudio(true);
        final BasicOutputDemo app = new BasicOutputDemo(arg);
        new Lwjgl3Application(app, config);
    }

    @Override
    public void create() {
        PixmapIO.PNG png = new PixmapIO.PNG();
        png.setCompression(9);
        png.setFlipY(false);
        Iconizer iconizer = new Iconizer();
        long seed = args == null || args.length == 0
                ? Iconizer.scramble(System.nanoTime())
                : Iconizer.scrambleAll((Object[]) args);
        Pixmap icon = iconizer.generate(72, 72, seed);
        try {
            png.write(Gdx.files.local(seed + ".png"), icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gdx.app.exit();
    }


}
