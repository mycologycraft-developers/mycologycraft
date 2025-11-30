package com.mycologycraft_devs.mycologycraft.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.earlydisplay.RenderElement;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import javax.swing.*;

@EventBusSubscriber(modid = MycologyCraft.MODID, value = Dist.CLIENT)
public class ClientEvents {

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		// Only render during the AFTER_TRANSLUCENT stage
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;


		Camera camera = event.getCamera();



		// Example points (replace these with your own dynamic ones)
		Vec3 start = new Vec3(0, 80, 0);
		Vec3 end   = new Vec3(30, 90, 20);

		PoseStack poseStack = event.getPoseStack();

		poseStack.translate(-camera.getPosition().x,-camera.getPosition().y,-camera.getPosition().z);

		VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());

		drawLine(poseStack, vertexConsumer, 0 ,0,0, 100,100,100,1f,1f,1f,1f);
		drawLine(poseStack, vertexConsumer, 50 ,0,50, 100,100,100,1f,0.5f,0.5f,1f);
	}

	public static void drawLine(PoseStack poseStack, VertexConsumer consumer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha){
		PoseStack.Pose posed = poseStack.last();
		consumer.addVertex(posed, (float)minX, (float)minY, (float)minZ).setColor(red, green, blue, alpha).setNormal(posed, 1.0F, 0.0F, 0.0F);
		consumer.addVertex(posed, (float)maxX, (float)maxY, (float)maxZ).setColor(red, green, blue, alpha).setNormal(posed, 1.0F, 0.0F, 0.0F);

	}
}