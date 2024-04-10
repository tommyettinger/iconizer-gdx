package com.github.tommyettinger.iconizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;

import java.nio.ByteBuffer;
import java.util.Objects;

public final class Iconizer implements Disposable {
    public TextureAtlas openmoji;
    public Array<TextureAtlas.AtlasRegion> regions;
    public SpriteBatch batch;
    public Iconizer(){
        openmoji = new TextureAtlas(Gdx.files.classpath("openmoji.atlas"), Gdx.files.classpath(""));
        regions = openmoji.getRegions();
        ShaderProgram shader = new ShaderProgram(
                "attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projTrans;\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "   v_color = a_color;\n" +
                "   v_color.a = v_color.a * (255.0/254.0);\n" +
                "   v_texCoords = a_texCoord0;\n" +
                "   gl_Position =  u_projTrans * a_position;\n" +
                "}\n",

                "#ifdef GL_ES\n" +
                "#define LOWP lowp\n" +
                "precision mediump float;\n" +
                "#else\n" +
                "#define LOWP \n" +
                "#endif\n" +
                "varying vec2 v_texCoords;\n" +
                "varying LOWP vec4 v_color;\n" +
                "uniform sampler2D u_texture;\n" +
                "const float eps = 1.0e-10;\n" +
                "vec4 rgb2hsl(vec4 c)\n" +
                "{\n" +
                "    const vec4 J = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n" +
                "    vec4 p = mix(vec4(c.bg, J.wz), vec4(c.gb, J.xy), step(c.b, c.g));\n" +
                "    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n" +
                "    float d = q.x - min(q.w, q.y);\n" +
                "    float l = q.x * (1.0 - 0.5 * d / (q.x + eps));\n" +
                "    return vec4(abs(q.z + (q.w - q.y) / (6.0 * d + eps)), (q.x - l) / (min(l, 1.0 - l) + eps), l, c.a);\n" +
                "}\n" +
                "\n" +
                "vec4 hsl2rgb(vec4 c)\n" +
                "{\n" +
                "    const vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n" +
                "    vec3 p = abs(fract(c.x + K.xyz) * 6.0 - K.www);\n" +
                "    float v = (c.z + c.y * min(c.z, 1.0 - c.z));\n" +
                "    return vec4(v * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), 2.0 * (1.0 - c.z / (v + eps))), c.w);\n" +
                "}\n" +
                "////Call this to go to the official HSL hue distribution (where blue is opposite yellow) from a\n" +
                "////different distribution that matches primary colors in painting (where purple is opposite yellow).\n" +
                "float primaries2official(float hue) {\n" +
                "    return pow(hue * 0.8 + 0.225, 2.0) - 0.050625;\n" +
                "}\n" +
                "void main()\n" +
                "{\n" +
                "   vec4 tgt = texture2D( u_texture, v_texCoords );\n" +
                "   vec4 hsl = rgb2hsl(tgt);\n" +
                "   hsl.x = primaries2official(v_color.x);\n" +
                "   hsl.y = v_color.y;\n" +
                "   hsl.z *= v_color.z;\n" +
                "   gl_FragColor = hsl2rgb(hsl);\n" +
                "}");
        if(!shader.isCompiled()) throw new GdxRuntimeException(shader.getLog());
        batch = new SpriteBatch(1000, shader);
    }

    public Pixmap generate(int width, int height, Object... seeds){
        long seed = scrambleAll(seeds);
        float bgColor = hsl2rgb(
                (seed & 63) / 64f,
                (seed >>> 6 & 15) / 64f + 0.7f,
                (seed >>> 10 & 63) / 150f + 0.2f,
                1f);
        float fgColor1 = Color.toFloatBits(
                (seed + 24 + (seed >>> 11 & 16) & 63) / 64f,
                (seed >>> 6 & 15) / 150f + 0.9f,
                (seed >>> 10 & 63) / 256f + 0.7f,
                1f);
        float fgColor2 = Color.toFloatBits(
                (seed + 24 + (seed >>> 11 & 16) + (seed >>> 17 & 3) & 63) / 64f,
                (seed >>> 6 & 15) / 150f + 0.85f - 0.035f + (seed >>> 19 & 7) / 100f,
                (seed >>> 10 & 63) / 256f + 0.65f - 0.05f + (seed >>> 22 & 15) / 150f,
                1f);

        long seed2 = scramble(seed);
        TextureAtlas.AtlasRegion l = regions.get(confineLowerHalf(seed2, regions.size / 2));
        TextureAtlas.AtlasRegion m = regions.get(confineUpperHalf(seed2, regions.size / 2) + regions.size / 2);
        Color t = new Color();
        Color.abgr8888ToColor(t, bgColor);

        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
        fb.begin();
        ScreenUtils.clear(t);
        batch.begin();

        int full = l.originalWidth;
        float hf = width / 2f;

        batch.setPackedColor(fgColor1);
        batch.draw(l.getTexture(), 0, 0, hf, height, l.getRegionX(), l.getRegionY(), l.originalWidth/2, full, false, true);
        batch.setPackedColor(fgColor2);
        batch.draw(m.getTexture(), hf, 0, hf, height, m.getRegionX() + l.originalWidth/2, m.getRegionY(), m.originalWidth/2, full, false, true);

        batch.end();

//                    pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
        //// The above is equivalent to the following, but the above also fills the pixmap.
        Pixmap pixmap = createFromFrameBuffer(0, 0, width, height);

        fb.end();
        fb.dispose();

        return pixmap;
    }

    private static float primaries2official(float hue) {
//        return    hue * (  0.677f
//                + hue * ( -0.123f
//                + hue * (-11.302f
//                + hue * ( 46.767f
//                + hue * (-58.493f
//                + hue *   23.474f)))));
        return (float) Math.pow(hue * 0.8f + 0.225f, 2f) - 0.050625f;
    }
    /**
     * Converts the four HSLA components, each in the 0.0 to 1.0 range, to a packed float in RGBA format.
     * @param h hue, from 0.0 to 1.0
     * @param s saturation, from 0.0 to 1.0
     * @param l lightness, from 0.0 to 1.0
     * @param a alpha, from 0.0 to 1.0
     * @return an RGBA-format packed float
     */
    public static float hsl2rgb(final float h, final float s, final float l, final float a){
        final float hue = primaries2official(h);
        float x = Math.min(Math.max(Math.abs(hue * 6f - 3f) - 1f, 0f), 1f);
        float y = hue + (2f / 3f);
        float z = hue + (1f / 3f);
        y -= (int)y;
        z -= (int)z;
        y = Math.min(Math.max(Math.abs(y * 6f - 3f) - 1f, 0f), 1f);
        z = Math.min(Math.max(Math.abs(z * 6f - 3f) - 1f, 0f), 1f);
        float v = (l + s * Math.min(l, 1f - l));
        float d = 2f * (1f - l / (v + 1e-10f));
        v *= 255f;
        return NumberUtils.intBitsToFloat(
                (int)(a * 127f) << 25
                        | (int)(v * MathUtils.lerp(1f, z, d)) << 16
                        | (int)(v * MathUtils.lerp(1f, y, d)) << 8
                        | (int)(v * MathUtils.lerp(1f, x, d))
        );
    }


    public static Pixmap createFromFrameBuffer(int x, int y, int w, int h) {
        Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
        Pixmap pixmap = new Pixmap(new Gdx2DPixmap(w, h, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888));
        ByteBuffer pixels = pixmap.getPixels();
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);
        return pixmap;
    }

    public static long scramble(Object o) {
        return scramble(Objects.hashCode(o));
    }
    public static long scramble(Object o1, Object o2) {
        return scramble(scramble(Objects.hashCode(o1)) + Objects.hashCode(o2));
    }
    public static long scrambleAll(Object... os) {
        long r = 0;
        for (int i = 0; i < os.length; i++) {
            r = scramble(Objects.hashCode(os[i]) + r);
        }
        return r;
    }
    /**
     * Given a long {@code x}, this randomly scrambles x, so it is (almost always) a very different long.
     * This can take any long and can return any long.
     * <br>
     * It is currently unknown if this has any fixed-points (inputs that produce an identical output), but
     * a step is taken at the start of the function to eliminate one major known fixed-point at 0.
     * <br>
     * This uses the MX3 unary hash by Jon Maiga, but XORs the input with 0xBBE0563303A4615FL before using MX3.
     * @param x any long, to be scrambled
     * @return a scrambled long derived from {@code x}
     */
    public static long scramble(long x) {
        x ^= 0xBBE0563303A4615FL;
        x ^= x >>> 32;
        x *= 0xBEA225F9EB34556DL;
        x ^= x >>> 29;
        x *= 0xBEA225F9EB34556DL;
        x ^= x >>> 32;
        x *= 0xBEA225F9EB34556DL;
        return x ^ x >>> 29;
    }


    /**
     * Given a long {@code x} and an int {@code bound}, this randomly scrambles the low bits of x, so it produces an int
     * between 0 (inclusive) and bound (exclusive). The bound is permitted to be negative; it is still exclusive then.
     * This uses the low bits only so that you can get a second bounded int with {@link #confineUpperHalf(long, int)}.
     * <br>
     * This does not randomize {@code x}; use {@link #scramble} methods to do that first.
     * @param x any long; should be scrambled with {@link #scramble}
     * @param bound the exclusive outer bound
     * @return a scrambled int between 0 (inclusive) and {@code bound} (exclusive) derived from {@code x}
     */
    public static int confineLowerHalf(long x, int bound) {
        return (bound = (int) ((bound * (x & 0xFFFFFFFFL)) >> 32)) + (bound >>> 31);
    }

    /**
     * Given a long {@code x} and an int {@code bound}, this randomly scrambles the low bits of x, so it produces an int
     * between 0 (inclusive) and bound (exclusive). The bound is permitted to be negative; it is still exclusive then.
     * This uses the high bits only so that you can get a second bounded int with {@link #confineLowerHalf(long, int)}.
     * <br>
     * This does not randomize {@code x}; use {@link #scramble} methods to do that first.
     * @param x any long; should be scrambled with {@link #scramble}
     * @param bound the exclusive outer bound
     * @return a scrambled int between 0 (inclusive) and {@code bound} (exclusive) derived from {@code x}
     */
    public static int confineUpperHalf(long x, int bound) {
        return (bound = (int) ((bound * (x >>> 32)) >> 32)) + (bound >>> 31);
    }


    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        if(openmoji != null) {
            openmoji.dispose();
            openmoji = null;
        }
        if(batch != null) {
            batch.dispose();
            batch = null;
        }
    }
}
