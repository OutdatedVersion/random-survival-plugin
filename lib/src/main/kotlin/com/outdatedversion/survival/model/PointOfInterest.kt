package com.outdatedversion.survival.model;

import com.outdatedversion.survival.format
import com.outdatedversion.survival.formatEnvironment
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.World
import org.bukkit.util.Vector
import java.util.*

data class PointOfInterest(
    val id: UUID,
    val ownerId: UUID?,
    val coords: Vector,
    val env: World.Environment,
    val context: String,
    // unix epoch
    val createdAt: Long,
)

fun PointOfInterest.asComponent(): Component {
    return Component.join(
        JoinConfiguration.separator(Component.space()),
        Component.text("$context ${coords.format()} (${formatEnvironment(env)})"),
        Component.text("[save]", NamedTextColor.YELLOW)
            .hoverEvent(
                HoverEvent.showText(
                    Component.join(
                        JoinConfiguration.separator(Component.space()),
                        Component.text("Click to save", NamedTextColor.GRAY),
                        Component.text(context, NamedTextColor.YELLOW),
                        Component.text("to your points", NamedTextColor.GRAY)
                    )
                )
            )
            .clickEvent(ClickEvent.runCommand("/pointsofinterest save $ownerId $id"))
    )
}
