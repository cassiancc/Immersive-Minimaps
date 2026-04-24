
package cc.cassian.immersiveminimaps.overlay;

import cc.cassian.immersiveminimaps.ModClient;
import cc.cassian.immersiveminimaps.helpers.ColorUtil;
import folk.sisby.surveyor.PlayerSummary;
import folk.sisby.surveyor.client.SurveyorClient;
import folk.sisby.surveyor.landmark.Landmark;
import folk.sisby.surveyor.landmark.component.LandmarkComponentTypes;
import folk.sisby.surveyor.util.RegionPos;
import garden.hestia.hoofprint.HoofprintMapStorage;

import java.util.*;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;

public class MinimapOverlay implements HudElement {
	public static final Identifier BACKGROUND = ModClient.locate("background");
	private final float PLAYER_ROTATION_STEPS = 16.0F;
	private final int PADDING = 8;
	private double centreX = 0.0F;
	private double centreZ = 0.0F;
	private Integer guiScale = null;
	private final boolean caveMode = false;
	private final boolean hideDecorations = false;
	private int cursorFrame = 0;
	private ResourceKey<Level> dim;
	private float switchFade = 0.0F;
	private final Minecraft minecraft = Minecraft.getInstance();
	private final int width = 93;
	private final int height = 93;
	public static MinimapOverlay INSTANCE = new MinimapOverlay();


	public void extractRenderState(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		if (ModClient.CONFIG.style.draw_background) {
			guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, 2, 2, width+5, height+5);
		}
		var delta = deltaTracker.getGameTimeDeltaTicks();
		this.switchFade = Math.max(0.0F, this.switchFade - delta);
		HoofprintMapStorage mapStorage = mapStorage();
		guiGraphics.pose().pushMatrix();
		float scaleFactor = this.getScaleFactor();
		guiGraphics.pose().scale(scaleFactor, scaleFactor);
		WorldBorder worldBorder = this.minecraft.level.getWorldBorder();
		double size = worldBorder.getSize();
		int borderX1 = Math.max((int)Math.floor(worldBorder.getCenterX() - size / (double)2.0F), mapStorage.minBlockX);
		int borderX2 = Math.min((int)Math.ceil(worldBorder.getCenterX() + size / (double)2.0F), mapStorage.maxBlockX);
		int borderZ1 = Math.max((int)Math.floor(worldBorder.getCenterZ() - size / (double)2.0F), mapStorage.minBlockZ);
		int borderZ2 = Math.min((int)Math.ceil(worldBorder.getCenterZ() + size / (double)2.0F), mapStorage.maxBlockZ);

		int renderX1 = Math.max((int)Math.floor(this.screenXToWorldX(0.0F)), borderX1);
		int renderX2 = Math.min((int)Math.ceil(this.screenXToWorldX(this.width)), borderX2);
		int renderZ1 = Math.max((int)Math.floor(this.screenYToWorldZ(0.0F)), borderZ1);
		int renderZ2 = Math.min((int)Math.ceil(this.screenYToWorldZ(this.height)), borderZ2);

		for(Map.Entry<RegionPos, Identifier> entry : (this.caveMode ? mapStorage.caveRegionTextures : mapStorage.regionTextures).entrySet()) {
			RegionPos regionPos = entry.getKey();
			Identifier texture = entry.getValue();
			int regionX1 = regionPos.x() * 32 * 16;
			int regionZ1 = regionPos.z() * 32 * 16;
			int u = Math.max(0, renderX1 - regionX1);
			int v = Math.max(0, renderZ1 - regionZ1);
			int drawWidth = Math.min(512, renderX2 - regionX1) - u;
			int drawHeight = Math.min(512, renderZ2 - regionZ1) - v;
			if (drawHeight > 0 && drawWidth > 0) {
				guiGraphics.pose().pushMatrix();
				guiGraphics.pose().translate((float)this.worldXToRenderX(regionX1 + u), (float)this.worldZToRenderY(regionZ1 + v));
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, (float)u, (float)v, drawWidth, drawHeight, drawWidth, drawHeight, 512, 512);
				guiGraphics.pose().popMatrix();
			}
		}

		guiGraphics.pose().popMatrix();
		double bestDistance = Double.MAX_VALUE;

		for(Landmark landmark : mapStorage.landmarks.values()) {
			if (landmark != null) {
				BlockPos pos = landmark.get(LandmarkComponentTypes.POS);
				if (!this.hideDecorations) {
					if (pos == null) {
						for(ChunkPos chunk : RegionPos.regionsToChunks(landmark.getOrDefault(LandmarkComponentTypes.CHUNKS, new HashMap<>()))) {
							double screenX = this.renderToScreen(this.worldXToRenderX(chunk.getMinBlockX()));
							double screenY = this.renderToScreen(this.worldZToRenderY(chunk.getMinBlockZ()));
							boolean isInside = 0 >= screenX && 0 < screenX + (double)(PLAYER_ROTATION_STEPS * scaleFactor) && 0 >= screenY && 0 < screenY + (double)(PLAYER_ROTATION_STEPS * scaleFactor);
							if (isInside && (double) 10.0F < bestDistance) {
								bestDistance = 10.0F;
							}
						}
					} else {
						double landmarkCenterX = this.renderToScreen(this.worldXToRenderX(pos.getX()));
						double landmarkCenterY = this.renderToScreen(this.worldZToRenderY(pos.getZ()));
						double mouseDistance = (0 - landmarkCenterX) * (0 - landmarkCenterX) + (0 - landmarkCenterY) * (0 - landmarkCenterY);
						if (mouseDistance < (double) (36 * this.minecraft.getWindow().getGuiScale()) && mouseDistance < bestDistance) {
							bestDistance = mouseDistance;
						}
					}
				}
			}
		}

		SurveyorClient.getFriends().forEach((uuidx, playerx) -> this.renderPlayer(guiGraphics, playerx, uuidx));
		try { // FIXME
			mapStorage.landmarks.values().forEach((landmarkx) -> this.renderLandmark(guiGraphics, landmarkx, scaleFactor));
		}
		catch (Exception ignored) {}
	}

	private void renderPlayer(GuiGraphics context, PlayerSummary player, UUID uuid) {
		boolean friend = !SurveyorClient.getClientUuid().equals(uuid);
		boolean inDim = player.dimension().equals(this.dim);
		if ((!friend || inDim) && (player.online() || ModClient.CONFIG.style.offlinePlayers) && !this.hideDecorations) {
			double dimX = player.pos().x();
			double dimZ = player.pos().z();

			double playerScreenX = this.renderToScreen(this.worldXToRenderX(dimX));
			double playerScreenY = this.renderToScreen(this.worldZToRenderY(dimZ));
			double clampedX = this.clampScreenX(playerScreenX);
			double clampedY = this.clampScreenY(playerScreenY);
			context.pose().pushMatrix();
			boolean clipped = clampedX != playerScreenX || clampedY != playerScreenY;
			context.pose().translate((float)clampedX, (float)clampedY);
			int tint = !player.online() ? 77 : (255);
			int argb = ARGB.color(255, (friend ? 0 : 255) * tint / 255, (inDim ? 255 : 204) * tint / 255, (friend ? 76 : 255) * tint / 255);
			if (!(Math.abs(playerScreenX - clampedX) > (double)this.width) && !(Math.abs(playerScreenY - clampedY) > (double)this.height)) {
				if (clipped) {
					context.pose().translate(-3.0F, -3.0F);
					context.blit(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("textures/map/decorations/player_off_map.png"), 0, 0, 1.0F, 1.0F, 6, 6, 6, 6, 8, 8, argb);
				} else {
					float playerRotation = (float)Math.round(player.yaw() / 360.0F * PLAYER_ROTATION_STEPS) / PLAYER_ROTATION_STEPS * 360.0F;
					context.pose().rotate((float)Math.toRadians(180.0F + playerRotation));
					context.pose().translate(-2.5F, -3.5F);
					context.blit(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("textures/map/decorations/player.png"), 0, 0, 2.0F, 0.0F, 5, 7, 5, 7, 8, 8, argb);
				}
			} else {
				context.pose().translate(-2.0F, -2.0F);
				context.blit(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("textures/map/decorations/player_off_limits.png"), 0, 0, 2.0F, 2.0F, 4, 4, 4, 4, 8, 8, argb);
			}

			context.pose().popMatrix();
		}
	}

	private void renderLandmark(GuiGraphics guiGraphics, Landmark landmark, float scaleFactor) {
		BlockPos pos = landmark.get(LandmarkComponentTypes.POS);
		if (!this.hideDecorations) {
			if (pos == null) {
				Set<ChunkPos> chunks = RegionPos.regionsToChunks(landmark.getOrDefault(LandmarkComponentTypes.CHUNKS, new HashMap<>()));
				guiGraphics.pose().pushMatrix();
				guiGraphics.pose().scale(scaleFactor, scaleFactor);

				for(ChunkPos chunk : chunks) {
					guiGraphics.pose().pushMatrix();
					guiGraphics.pose().translate((float)this.worldXToRenderX(chunk.getMinBlockX()), (float)this.worldZToRenderY(chunk.getMinBlockZ()));
					int color = -16777216 | ColorUtil.applyBrightnessRGB(ColorUtil.Brightness.NORMAL, landmark.getOrDefault(LandmarkComponentTypes.COLOR, 16777215));
					guiGraphics.fill(0, 0, 16, 16, 1157627903 & color);
					//? if >26 {
					/*int x = chunk.x();
					int z = chunk.z();
					*///?} else {
					int x = chunk.x;
					int z = chunk.z;
					//?}
					if (!chunks.contains(new ChunkPos(x - 1, z))) {
						guiGraphics.fill(0, 0, 1, 16, color);
					}

					if (!chunks.contains(new ChunkPos(x, z - 1))) {
						guiGraphics.fill(0, 0, 16, 1, color);
					}

					if (!chunks.contains(new ChunkPos(x + 1, z))) {
						guiGraphics.fill(15, 0, 16, 16, color);
					}

					if (!chunks.contains(new ChunkPos(x, z + 1))) {
						guiGraphics.fill(0, 15, 16, 16, color);
					}

					guiGraphics.pose().popMatrix();
				}

				guiGraphics.pose().popMatrix();
			} else {
				double landmarkScreenX = this.renderToScreen(this.worldXToRenderX(pos.getX()));
				double landmarkScreenY = this.renderToScreen(this.worldZToRenderY(pos.getZ()));

				int landmarkColor = landmark.getOrDefault(LandmarkComponentTypes.COLOR, 16777215);
				int tint = 16777215;
				guiGraphics.pose().pushMatrix();
				guiGraphics.pose().translate((float)landmarkScreenX, (float)landmarkScreenY);

				if (landmarkScreenX < width && landmarkScreenY < height && landmarkScreenX > 0 && landmarkScreenY > 0) {
					if (landmark.contains(LandmarkComponentTypes.STACK) && !landmark.get(LandmarkComponentTypes.STACK).isEmpty()) {
						ItemStack stack = landmark.get(LandmarkComponentTypes.STACK);
						guiGraphics.renderFakeItem(stack, -8, -8);
					} else {
						guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("textures/map/decorations/white_banner.png"), -4, -8, 0.0F, 0.0F, 8, 8, 8, 8, 8, 8, -16777216 | ColorUtil.tint(landmarkColor, tint));
					}
				}

				guiGraphics.pose().popMatrix();
			}
		}
	}

	public HoofprintMapStorage mapStorage() {
		return HoofprintMapStorage.get(this.dim);
	}

	public void init() {
		if (guiScale == null)
			this.guiScale = ModClient.CONFIG.defaultScale < 1 ? (int)Math.ceil((double)this.minecraft.getWindow().getGuiScale() / (ModClient.CONFIG.defaultScale == -1 ? (double)2.0F : (double)1.0F)) : ModClient.CONFIG.defaultScale;
	}

	public void zoomOut() {
		if (guiScale > 1) {
			this.guiScale -= 1;
		}
	}

	public void zoomIn() {
		if (guiScale < 10) {
			this.guiScale += 1;
		}
	}

	public void tick() {
		this.cursorFrame = (this.cursorFrame + 1) % 40;
		this.centreX = this.minecraft.player.getBlockX();
		this.centreZ = this.minecraft.player.getBlockZ();
	}

	public void changeDim(ResourceKey<Level> newDim) {
		if (dim == newDim) return;
		this.dim = newDim;
		this.switchFade = 20.0F;
	}

	double worldXToRenderX(double worldX) {
		return (double)this.getWidth() / (double)2.0F + worldX - this.centreX+ PADDING;
	}

	double worldZToRenderY(double worldZ) {
		return (double)this.getHeight() / (double)2.0F + worldZ - this.centreZ+PADDING;
	}

	double screenXToWorldX(double screenX) {
		return screenX / (double)this.getScaleFactor() + this.centreX - (double)this.getWidth() / (double)2.0F;
	}

	double screenYToWorldZ(double screenY) {
		return screenY / (double)this.getScaleFactor() + this.centreZ - (double)this.getHeight() / (double)2.0F;
	}

	double screenToRender(double screenPixels) {
		return screenPixels / (double)this.getScaleFactor();
	}

	double renderToScreen(double screenPixels) {
		return screenPixels * (double)this.getScaleFactor();
	}

	float getScaleFactor() {
		float guiScale = Objects.requireNonNullElse(this.guiScale, 1);
		return guiScale / (float)this.minecraft.getWindow().getGuiScale();
	}

	float getWidth() {
		return (float)this.width / this.getScaleFactor();
	}

	float getHeight() {
		return (float)this.height / this.getScaleFactor();
	}

	double clampScreenX(double x) {
		WorldBorder worldBorder = this.minecraft.level.getWorldBorder();
		double size = worldBorder.getSize();
		HoofprintMapStorage mapStorage = mapStorage();
		double borderX1 = this.renderToScreen(this.worldXToRenderX(Math.max((int)Math.floor(worldBorder.getCenterX() - size / (double)2.0F), mapStorage.minBlockX)));
		double borderX2 = this.renderToScreen(this.worldXToRenderX(Math.min((int)Math.ceil(worldBorder.getCenterX() + size / (double)2.0F), mapStorage.maxBlockX)));
		double minX = Math.min(borderX2, 0.0F);
		double maxX = Math.max(borderX1, this.width);
		return Mth.clamp(x, minX, maxX);
	}

	double clampScreenY(double y) {
		WorldBorder worldBorder = this.minecraft.level.getWorldBorder();
		double size = worldBorder.getSize();
		HoofprintMapStorage mapStorage = mapStorage();
		double borderZ1 = this.renderToScreen(this.worldZToRenderY(Math.max((int)Math.floor(worldBorder.getCenterZ() - size / (double)2.0F), mapStorage.minBlockZ)));
		double borderZ2 = this.renderToScreen(this.worldZToRenderY(Math.min((int)Math.ceil(worldBorder.getCenterZ() + size / (double)2.0F), mapStorage.maxBlockZ)));
		double minY = Math.min(borderZ2, 0.0F);
		double maxY = Math.max(borderZ1, this.height);
		return Mth.clamp(y, minY, maxY);
	}

	//? if <26 {
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		extractRenderState(guiGraphics, deltaTracker);
	}
	//?}
}
