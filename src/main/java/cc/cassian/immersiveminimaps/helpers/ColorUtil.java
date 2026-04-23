/*
Copied from Hoofprint under LGPL - https://github.com/HestiMae/hoofprint/blob/26.1/LICENSE
                   GNU LESSER GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.


  This version of the GNU Lesser General Public License incorporates
the terms and conditions of version 3 of the GNU General Public
License, supplemented by the additional permissions listed below.

  0. Additional Definitions.

  As used herein, "this License" refers to version 3 of the GNU Lesser
General Public License, and the "GNU GPL" refers to version 3 of the GNU
General Public License.

  "The Library" refers to a covered work governed by this License,
other than an Application or a Combined Work as defined below.

  An "Application" is any work that makes use of an interface provided
by the Library, but which is not otherwise based on the Library.
Defining a subclass of a class defined by the Library is deemed a mode
of using an interface provided by the Library.

  A "Combined Work" is a work produced by combining or linking an
Application with the Library.  The particular version of the Library
with which the Combined Work was made is also called the "Linked
Version".

  The "Minimal Corresponding Source" for a Combined Work means the
Corresponding Source for the Combined Work, excluding any source code
for portions of the Combined Work that, considered in isolation, are
based on the Application, and not on the Linked Version.

  The "Corresponding Application Code" for a Combined Work means the
object code and/or source code for the Application, including any data
and utility programs needed for reproducing the Combined Work from the
Application, but excluding the System Libraries of the Combined Work.

  1. Exception to Section 3 of the GNU GPL.

  You may convey a covered work under sections 3 and 4 of this License
without being bound by section 3 of the GNU GPL.

  2. Conveying Modified Versions.

  If you modify a copy of the Library, and, in your modifications, a
facility refers to a function or data to be supplied by an Application
that uses the facility (other than as an argument passed when the
facility is invoked), then you may convey a copy of the modified
version:

   a) under this License, provided that you make a good faith effort to
   ensure that, in the event an Application does not supply the
   function or data, the facility still operates, and performs
   whatever part of its purpose remains meaningful, or

   b) under the GNU GPL, with none of the additional permissions of
   this License applicable to that copy.

  3. Object Code Incorporating Material from Library Header Files.

  The object code form of an Application may incorporate material from
a header file that is part of the Library.  You may convey such object
code under terms of your choice, provided that, if the incorporated
material is not limited to numerical parameters, data structure
layouts and accessors, or small macros, inline functions and templates
(ten or fewer lines in length), you do both of the following:

   a) Give prominent notice with each copy of the object code that the
   Library is used in it and that the Library and its use are
   covered by this License.

   b) Accompany the object code with a copy of the GNU GPL and this license
   document.

  4. Combined Works.

  You may convey a Combined Work under terms of your choice that,
taken together, effectively do not restrict modification of the
portions of the Library contained in the Combined Work and reverse
engineering for debugging such modifications, if you also do each of
the following:

   a) Give prominent notice with each copy of the Combined Work that
   the Library is used in it and that the Library and its use are
   covered by this License.

   b) Accompany the Combined Work with a copy of the GNU GPL and this license
   document.

   c) For a Combined Work that displays copyright notices during
   execution, include the copyright notice for the Library among
   these notices, as well as a reference directing the user to the
   copies of the GNU GPL and this license document.

   d) Do one of the following:

       0) Convey the Minimal Corresponding Source under the terms of this
       License, and the Corresponding Application Code in a form
       suitable for, and under terms that permit, the user to
       recombine or relink the Application with a modified version of
       the Linked Version to produce a modified Combined Work, in the
       manner specified by section 6 of the GNU GPL for conveying
       Corresponding Source.

       1) Use a suitable shared library mechanism for linking with the
       Library.  A suitable mechanism is one that (a) uses at run time
       a copy of the Library already present on the user's computer
       system, and (b) will operate properly with a modified version
       of the Library that is interface-compatible with the Linked
       Version.

   e) Provide Installation Information, but only if you would otherwise
   be required to provide such information under section 6 of the
   GNU GPL, and only to the extent that such information is
   necessary to install and execute a modified version of the
   Combined Work produced by recombining or relinking the
   Application with a modified version of the Linked Version. (If
   you use option 4d0, the Installation Information must accompany
   the Minimal Corresponding Source and Corresponding Application
   Code. If you use option 4d1, you must provide the Installation
   Information in the manner specified by section 6 of the GNU GPL
   for conveying Corresponding Source.)

  5. Combined Libraries.

  You may place library facilities that are a work based on the
Library side by side in a single library together with other library
facilities that are not Applications and are not covered by this
License, and convey such a combined library under terms of your
choice, if you do both of the following:

   a) Accompany the combined library with a copy of the same work based
   on the Library, uncombined with any other library facilities,
   conveyed under the terms of this License.

   b) Give prominent notice with the combined library that part of it
   is a work based on the Library, and explaining where to find the
   accompanying uncombined form of the same work.

  6. Revised Versions of the GNU Lesser General Public License.

  The Free Software Foundation may publish revised and/or new versions
of the GNU Lesser General Public License from time to time. Such new
versions will be similar in spirit to the present version, but may
differ in detail to address new problems or concerns.

  Each version is given a distinguishing version number. If the
Library as you received it specifies that a certain numbered version
of the GNU Lesser General Public License "or any later version"
applies to it, you have the option of following the terms and
conditions either of that published version or of any later version
published by the Free Software Foundation. If the Library as you
received it does not specify a version number of the GNU Lesser
General Public License, you may choose any version of the GNU Lesser
General Public License ever published by the Free Software Foundation.

  If the Library as you received it specifies that a proxy can decide
whether future versions of the GNU Lesser General Public License shall
apply, that proxy's public statement of acceptance of any version is
permanent authorization for you to choose that version for the
Library.
 */
package cc.cassian.immersiveminimaps.helpers;

import garden.hestia.hoofprint.Hoofprint;
import garden.hestia.hoofprint.util.BlockConstants;
import garden.hestia.hoofprint.util.ColorConstants;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ColorUtil {
	public static final int SKY_LIGHT = 15;
	public static final Map<Predicate<Block>, Function<Integer, Integer>> BLOCK_COLOR_PROVIDERS = Map.of(
			BlockConstants.FOLIAGE_BLOCKS::contains, foliage -> ColorUtil.tint(ColorConstants.FOLIAGE_TEXTURE_COLOR, foliage),
			BlockConstants.GRASS_BLOCKS::contains, foliage -> ColorUtil.tint(ColorConstants.GRASS_TEXTURE_COLOR, foliage),
			BlockConstants.GRASS_BLOCK_BLOCKS::contains, foliage -> ColorUtil.tint(ColorConstants.GRASS_BLOCK_TEXTURE_COLOR, foliage)
	);
	public static final Map<Predicate<Block>, Integer> CONSTANT_BLOCK_COLOR_PROVIDERS = Map.of(
			BlockConstants.STONE_BLOCKS::contains, ColorConstants.STONE_MAP_COLOR,
			BlockConstants.ICE_BLOCKS::contains, ColorConstants.ICE_MAP_COLOR,
			BlockConstants.SPRUCE_BLOCKS::contains, ColorUtil.tint(ColorConstants.FOLIAGE_TEXTURE_COLOR, FoliageColor.FOLIAGE_EVERGREEN),
			BlockConstants.BIRCH_BLOCKS::contains, ColorUtil.tint(ColorConstants.FOLIAGE_TEXTURE_COLOR, FoliageColor.FOLIAGE_BIRCH),
			BlockConstants.MANGROVE_BLOCKS::contains, ColorUtil.tint(ColorConstants.FOLIAGE_TEXTURE_COLOR, FoliageColor.FOLIAGE_MANGROVE)
	);

	public static int tint(int base, int tint) {
		int a1 = (base >>> 24);
		int r1 = ((base & 0xff0000) >> 16);
		int g1 = ((base & 0xff00) >> 8);
		int b1 = (base & 0xff);

		int a2 = (tint >>> 24);
		int r2 = ((tint & 0xff0000) >> 16);
		int g2 = ((tint & 0xff00) >> 8);
		int b2 = (tint & 0xff);

		int a = (a1 * a2) / 256;
		int r = (r1 * r2) / 256;
		int g = (g1 * g2) / 256;
		int b = (b1 * b2) / 256;

		return a << 24 | r << 16 | g << 8 | b;
	}

	public static int blend(int c1, int c2, float ratio) {
		float iRatio = 1.0f - ratio;

		int a1 = (c1 >>> 24);
		int r1 = ((c1 & 0xff0000) >> 16);
		int g1 = ((c1 & 0xff00) >> 8);
		int b1 = (c1 & 0xff);

		int a2 = (c2 >>> 24);
		int r2 = ((c2 & 0xff0000) >> 16);
		int g2 = ((c2 & 0xff00) >> 8);
		int b2 = (c2 & 0xff);

		int a = (int) ((a1 * iRatio) + (a2 * ratio));
		int r = (int) ((r1 * iRatio) + (r2 * ratio));
		int g = (int) ((g1 * iRatio) + (g2 * ratio));
		int b = (int) ((b1 * iRatio) + (b2 * ratio));

		return a << 24 | r << 16 | g << 8 | b;
	}

	public static int applyBrightnessRGB(Brightness brightness, int color) {
		int i = brightness.brightness;
		int r = (color >> 16 & 0xFF) * i / 255;
		int g = (color >> 8 & 0xFF) * i / 255;
		int b = (color & 0xFF) * i / 255;
		return r << 16 | g << 8 | b;
	}

	/**
	 * @author ampflower
	 */
	public static Brightness getBrightnessFromDepth(int depth, int x, int z) {
		if (depth == 7) { // Emulate floating point error in vanilla code
			depth = 8;
		}
		int ditheredDepth = depth + (((x ^ z) & 1) << 1);
		if (ditheredDepth > 9) {
			return Brightness.LOW;
		} else if (ditheredDepth >= 5) {
			return Brightness.NORMAL;
		} else {
			return Brightness.HIGH;
		}
	}

	public static int blendColors(int[][] colors, int x, int z, int radius) {
		if (radius == 0) return colors[x][z];
		long r = 0;
		long g = 0;
		long b = 0;
		int num = 0;
		for (int i = x - radius; i < x + radius; i++) {
			for (int j = z - radius; j < z + radius; j++) {
				// if ((x - i) * (x - i) + (z - j) * (z - j) > radius * radius) continue;
				r += (colors[i][j] & 0xFF0000) >> 16;
				g += (colors[i][j] & 0xFF00) >> 8;
				b += colors[i][j] & 0xFF;
				num += Math.min(Math.abs(colors[i][j]), 1);
			}
		}
		return Math.toIntExact((r / num & 0xFF) << 16 | (g / num & 0xFF) << 8 | b / num & 0xFF);
	}

	public static Function<Integer, Integer> getBiomeColorProvider(Block block) {
		if (!Hoofprint.CONFIG.style.biomeFoliage) return null;
		for (Predicate<Block> predicate : BLOCK_COLOR_PROVIDERS.keySet()) {
			if (predicate.test(block)) {
				return BLOCK_COLOR_PROVIDERS.get(predicate);
			}
		}
		return null;
	}

	public static int getStaticBlockColor(Block block) {
		if (Hoofprint.CONFIG.style.accurateColors) {
			for (Predicate<Block> predicate : CONSTANT_BLOCK_COLOR_PROVIDERS.keySet()) {
				if (predicate.test(block)) {
					return CONSTANT_BLOCK_COLOR_PROVIDERS.get(predicate);
				}
			}
		}
		return block.defaultMapColor().col;
	}

	public static int argbToABGR(int argbColor) {
		int r = (argbColor >> 16) & 0xFF;
		int b = argbColor & 0xFF;
		return (argbColor & 0xFF00FF00) | (b << 16) | r;
	}

	public static float[] getColorFromArgb(int color) {
		return new float[]{ARGB.red(color) / 255f, ARGB.green(color) / 255f, ARGB.blue(color) / 255f};
	}

	public enum Brightness {
		LOW(180),
		NORMAL(220),
		HIGH(255),
		LOWEST(135);

		public final int brightness;

		Brightness(int brightness) {
			this.brightness = brightness;

		}
	}
}