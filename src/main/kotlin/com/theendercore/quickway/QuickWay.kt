package com.theendercore.quickway


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xaero.common.XaeroMinimapSession
import xaero.common.minimap.waypoints.Waypoint
import xaero.common.minimap.waypoints.WaypointSet
import xaero.common.minimap.waypoints.WaypointsManager
import xaero.minimap.XaeroMinimap


const val MODID = "quickway"

@JvmField
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

@Suppress("unused")
fun id(path: String): Identifier = Identifier(MODID, path)


var cameraWaypoint: KeyBinding =
    KeyBindingHelper.registerKeyBinding(KeyBinding("gui.camera_waypoint", 57, "Xaero's Minimap"))

@Suppress("unused")
fun onInitialize() {

    LOGGER.info(MODID)

    ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick register@{
        val manager: WaypointsManager? = mgrIfReady()
        val waypointSet: WaypointSet? = manager?.currentWorld?.currentSet
        val camera = MinecraftClient.getInstance().cameraEntity
        while (cameraWaypoint.wasPressed() && camera != null && waypointSet != null) {
            val w = Waypoint(camera.x.toInt(), camera.y.toInt(), camera.z.toInt(), "x", "x", 0xffffff - 1, 0, false)
            waypointSet.list.add(w)
            XaeroMinimap.instance.settings.saveAllWaypoints(manager)
        }

    })
}

fun mgrIfReady(): WaypointsManager? {
    val mgr = XaeroMinimapSession.getCurrentSession()?.waypointsManager
    if (mgr?.currentWorld == null)
        return null
    return mgr
}