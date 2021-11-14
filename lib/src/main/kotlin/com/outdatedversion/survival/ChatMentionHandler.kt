package com.outdatedversion.survival

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.time.Instant
import java.time.format.DateTimeFormatter

class ChatMentionHandler(private val defaultTimeFormatter: DateTimeFormatter): Listener {
    @EventHandler
    fun handleChatEvent(event: AsyncChatEvent) {
        event.isCancelled = true

        val parts = PlainTextComponentSerializer.plainText().serialize(event.message()).split(" ")

        Bukkit.getOnlinePlayers().forEach { player ->
            val message = mutableListOf<Component>()
            var mentioned = false
            val nameRegex = """(.*)(@?${player.name})(.*)""".toRegex(RegexOption.IGNORE_CASE)

            parts.forEach {
                if (nameRegex.matches(it)) {
                    mentioned = true
                    message.add(Component.text(it, NamedTextColor.YELLOW))
                } else {
                    message.add(Component.text(it))
                }
            }

            val timestampText = Component.text("Sent at ${this.defaultTimeFormatter.format(Instant.now())}", NamedTextColor.GRAY)
            player.sendMessage(
                Component.join(
                    JoinConfiguration.separator(Component.space()),
                    Component.text("<${event.player.name}>").hoverEvent(HoverEvent.showText(timestampText)),
                    *message.toTypedArray()
                )
            )

            if (mentioned) {
                player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 0.8f, 1f))
            }
        }
    }
}