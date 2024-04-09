package com.github.tommyettinger.iconizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public final class Iconizer {
    public TextureAtlas openmoji;
    public Iconizer(){
        openmoji = new TextureAtlas(Gdx.files.classpath("openmoji.atlas"), Gdx.files.classpath(""));
        Array<TextureAtlas.AtlasRegion> regions = openmoji.getRegions();
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


}
