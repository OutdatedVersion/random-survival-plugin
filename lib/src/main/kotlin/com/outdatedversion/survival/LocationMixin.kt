package com.outdatedversion.survival

import org.bukkit.Location
import org.bukkit.World

fun Location.asHumanReadableText(): String {
    return "${this.blockX}, ${this.blockY}, ${this.blockZ} (${formatEnvironment(this.world.environment)})"
}

fun formatEnvironment(environment: World.Environment): String {
    return when (environment) {
        World.Environment.NORMAL -> "Overworld"
        World.Environment.NETHER -> "The Nether"
        World.Environment.THE_END -> "The End"
        else -> "Unknown"
    }
}