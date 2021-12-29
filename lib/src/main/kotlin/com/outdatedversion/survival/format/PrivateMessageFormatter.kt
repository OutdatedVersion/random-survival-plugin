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

class PrivateMessageFormatter(private val plugin: Plugin, private val defaultTimeFormatter: DateTimeFormatter) {
    fun apply(sender: Player, recipient: Player, message: Component, isSender: Boolean): Component {
        //Implemented from the ChatFormatter
        val playerTimezone = if (isSender) sender.persistentDataContainer.get(
            plugin.createKey("timezone"),
            PersistentDataType.STRING
        ) else recipient.persistentDataContainer.get(plugin.createKey("timezone"), PersistentDataType.STRING)
        var playerAwareTimeFormatter = defaultTimeFormatter
        if (!playerTimezone.isNullOrBlank()) {
            playerAwareTimeFormatter = defaultTimeFormatter.withZone(ZoneId.of(playerTimezone, ZoneId.SHORT_IDS))
        }

        val timestampText =
            Component.text("Sent at ${playerAwareTimeFormatter.format(Instant.now())}", NamedTextColor.GRAY)

        return Component.join(
            JoinConfiguration.separator(Component.space()),
            Component.text(if (isSender) "To " else "From ", NamedTextColor.LIGHT_PURPLE)
                .append(Component.text(if (isSender) "${recipient.name}" else "${sender.name}", NamedTextColor.AQUA))
                .append(Component.text(":", NamedTextColor.WHITE)).hoverEvent(HoverEvent.showText(timestampText)),

            message
        )
    }
}
