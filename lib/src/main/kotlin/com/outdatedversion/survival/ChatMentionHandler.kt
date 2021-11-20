package com.outdatedversion.survival

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.persistence.PersistentDataType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class ChatMentionHandler(private val defaultTimeFormatter: DateTimeFormatter, private val pluginInstance: Plugin): Listener {
    @EventHandler
    fun handleChatEvent(event: AsyncChatEvent) {
        val namespacedKey = NamespacedKey(pluginInstance, "timezone") // Sets Key

        event.isCancelled = true // Cancels default message

        Bukkit.getOnlinePlayers().forEach { player ->
            var mentioned = false
            val nameRegex = """(@?${player.name})""".toPattern(Pattern.CASE_INSENSITIVE)

            val data = player.persistentDataContainer
            val timezone = data.get(namespacedKey, PersistentDataType.STRING)

            val config = TextReplacementConfig
                .builder()
                .match(nameRegex)
                .replacement { match, _ ->
                    mentioned = true
                    Component.text(match.group(), NamedTextColor.YELLOW)
                }
                .build()

            val timestampText = Component.text("Sent at ${this.defaultTimeFormatter.withZone(ZoneId.of(timezone)).format(Instant.now())}", NamedTextColor.GRAY)
            player.sendMessage(
                Component.join(
                    JoinConfiguration.separator(Component.space()),
                    Component.text("<${event.player.name}>").hoverEvent(HoverEvent.showText(timestampText)),
                    event.message().replaceText(config)
                )
            )

            if (mentioned) {
                player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 0.8f, 1f))
            }
        }
    }
}