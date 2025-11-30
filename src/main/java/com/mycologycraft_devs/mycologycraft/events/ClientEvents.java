package com.mycologycraft_devs.mycologycraft.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mycologycraft_devs.mycologycraft.MycologyCraft;
import com.mycologycraft_devs.mycologycraft.utility.bezierCurves;
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
import java.util.ArrayList;
import java.util.List;



//this should all be moved to its own class before pull request
@EventBusSubscriber(modid = MycologyCraft.MODID, value = Dist.CLIENT)
public class ClientEvents {

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		// Only render during the AFTER_TRANSLUCENT stage
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

		//create a new bezierCurves object
		bezierCurves berzier = new bezierCurves();

		//quick and dirty size mult
		float sizemult = 5;

		//list of points for our curve, might use a better curve type in the end but this works for testing
		berzier.controlPoints = List.of(new Vec3(0*sizemult,0*sizemult,0*sizemult), new Vec3(0.25*sizemult,0.35*sizemult,0.15*sizemult), new Vec3(0.65*sizemult,0.45*sizemult,0.9*sizemult), new Vec3(1*sizemult,1*sizemult,1*sizemult));



		//get camera for transforms and set up the pose stack
		Camera camera = event.getCamera();
		PoseStack poseStack = event.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(-camera.getPosition().x,-camera.getPosition().y,-camera.getPosition().z);
		VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.debugLineStrip(1));

		//increment for our curve drawing
		float increment = 0.05f;

		//loop to draw our curve
		for (float point = increment; point <= 1; point+=increment) {
			drawLine(poseStack, vertexConsumer, berzier.getPoint(point-increment).x ,berzier.getPoint(point-increment).y+64,berzier.getPoint(point-increment).z, berzier.getPoint(point).x,berzier.getPoint(point).y+64,berzier.getPoint(point).z,1f,1f,1f,1f);

		}


		//old testing code, still good for reference on how the function works
		//drawLine(poseStack, vertexConsumer, berzier.getPoint(0).x ,berzier.getPoint(0).y+64,berzier.getPoint(0).z, berzier.getPoint(0.5).x,berzier.getPoint(0.5).y+64,berzier.getPoint(0.5).z,1f,1f,1f,1f);
		//drawLine(poseStack, vertexConsumer, berzier.getPoint(0.5).x ,berzier.getPoint(0.5).y+64,berzier.getPoint(0.5).z, berzier.getPoint(1).x,berzier.getPoint(1).y+64,berzier.getPoint(1).z,1f,0.5f,0.5f,1f);

		poseStack.popPose();
	}


	//quick and dirty line drawing function
	public static void drawLine(PoseStack poseStack, VertexConsumer consumer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha){
		PoseStack.Pose posed = poseStack.last();
		consumer.addVertex(posed, (float)minX, (float)minY, (float)minZ).setColor(red, green, blue, alpha).setNormal(posed, 1.0F, 0.0F, 0.0F);
		consumer.addVertex(posed, (float)maxX, (float)maxY, (float)maxZ).setColor(red, green, blue, alpha).setNormal(posed, 1.0F, 0.0F, 0.0F);

	}
}