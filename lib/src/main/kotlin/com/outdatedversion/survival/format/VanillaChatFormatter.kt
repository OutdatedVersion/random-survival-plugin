package com.outdatedversion.survival.format

import com.outdatedversion.survival.Plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class VanillaChatFormatter(private val plugin: Plugin, private val defaultTimeFormatter: DateTimeFormatter): ChatFormatter {
    override fun apply(player: Player, message: Component): Component {
        // TODO
        val playerTimezone = player.persistentDataContainer.get(plugin.createKey("timezone"), PersistentDataType.STRING)
        var playerAwareTimeFormatter = defaultTimeFormatter
        if (!playerTimezone.isNullOrBlank()) {
            playerAwareTimeFormatter = defaultTimeFormatter.withZone(ZoneId.of(playerTimezone, ZoneId.SHORT_IDS))
        }

        val timestampText = Component.text("Sent at ${playerAwareTimeFormatter.format(Instant.now())}", NamedTextColor.GRAY)
        return Component.join(
            JoinConfiguration.separator(Component.space()),
            Component.text("<${player.name}>").hoverEvent(HoverEvent.showText(timestampText)),
            message
        )
    }
}
