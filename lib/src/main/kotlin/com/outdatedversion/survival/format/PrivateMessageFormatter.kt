package com.outdatedversion.survival.format

import com.outdatedversion.survival.Plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * This class is used to format the private messages send between a player sender and a player recipient
 * @param plugin An instance of the plugin used to retrieve persistent data keys for the purpose of timezone displaying
 * @param defaultTimeFormatter The default timezone to be used if the player does not already have a tracked timezone
 */
class PrivateMessageFormatter(private val plugin: Plugin, private val defaultTimeFormatter: DateTimeFormatter) {
    /**
     * Takes the message sender, its recipient, the message and whether the message should be formatted for the sender prospective or not (the recipient prospective)
     * @param sender The player sender, the player attempting to send a message
     * @param recipient The player recipient, the player the message is being sent to
     * @param message The message Component containing the body of the message being sent to the recipient
     * @param isSender Whether the message should be formatted for the sender prospective, or not, then for the recipient prospective
     * @return Formatted component message ready to be sent the [isSender] player. Includes player name, colors, hovers, message body
     */
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
                .append(Component.text(if (isSender) "${recipient.name}" else "${sender.name}", NamedTextColor.AQUA)).clickEvent(
                    ClickEvent.suggestCommand("/msg " + if (isSender) "${recipient.name}" else "${sender.name}"))
                .append(Component.text(":", NamedTextColor.WHITE)).hoverEvent(HoverEvent.showText(timestampText)),

            message
        )
    }
}
