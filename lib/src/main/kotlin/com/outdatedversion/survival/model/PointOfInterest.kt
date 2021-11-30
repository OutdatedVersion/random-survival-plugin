package com.outdatedversion.survival.model;

import org.bukkit.World
import org.bukkit.util.Vector
import java.util.*

data class PointOfInterest(
    val id: UUID,
    val coords: Vector,
    val env: World.Environment,
    val context: String,
)
