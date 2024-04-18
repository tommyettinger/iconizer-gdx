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
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.IOException;

public class BasicOutputDemo extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 96;
    public static final int SCREEN_HEIGHT = 96;
    private final String[] args;

    public BasicOutputDemo(String[] arg) {
        this.args = arg;
    }

    public static void main(String[] arg) {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        final BasicOutputDemo app = new BasicOutputDemo(arg);
        new HeadlessApplication(app, config);
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
        Pixmap icon = iconizer.generate(SCREEN_WIDTH, SCREEN_HEIGHT, seed);
        try {
            png.write(Gdx.files.local(seed + ".png"), icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gdx.app.exit();
    }


}
