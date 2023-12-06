package com.theendercore.quickway


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xaero.common.XaeroMinimapSession
import xaero.common.minimap.waypoints.Waypoint
import xaero.common.minimap.waypoints.WaypointSet
import xaero.common.minimap.waypoints.WaypointsManager
import xaero.common.settings.ModSettings
import xaero.minimap.XaeroMinimap
import kotlin.math.floor


@Suppress("UNUSED")
object QuickWay {
    private const val MODID = "quickway"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MODID)
    private var cooldown = 0
    fun id(path: String): Identifier = Identifier(MODID, path)
    private var cameraWaypointKey: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding("gui.camera_waypoint", InputUtil.UNKNOWN_KEY.code, "Xaero's Minimap")
    )


    fun clientInit() {
        LOGGER.info("QuickWay, more like SlowWay! hahahahaha~")
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick register@{
            val manager: WaypointsManager? = XaeroMinimapSession.getCurrentSession()?.waypointsManager
            val waypointSet: WaypointSet? = manager?.currentWorld?.currentSet
            val camera = MinecraftClient.getInstance().cameraEntity
            while (cameraWaypointKey.wasPressed() && camera != null && waypointSet != null && cooldown == 0) {
                waypointSet.list.add(
                    Waypoint(
                        floor(camera.x).toInt(),
                        floor(camera.y).toInt(),
                        floor(camera.z).toInt(),
                        "",
                        "•",
                        (Math.random() * ModSettings.ENCHANT_COLORS.size).toInt(),
                        0,
                        false
                    )
                )
                cooldown += 5
                XaeroMinimap.instance.settings.saveAllWaypoints(manager)
            }
            if (cooldown > 0) cooldown--
        })
    }
}