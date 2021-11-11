package com.outdatedversion.survival

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatMentionHandler: Listener {
    @EventHandler
    fun handleChatEvent(event: AsyncChatEvent) {
        event.isCancelled = true

        val parts = PlainTextComponentSerializer.plainText().serialize(event.message()).split(" ")

        Bukkit.getOnlinePlayers().forEach { player ->
            val message = mutableListOf<Component>()
            var mentioned = false
            val regex = """(.*)(@?${player.name})(.*)""".toRegex(RegexOption.IGNORE_CASE)

            parts.forEach {
                if (regex.matches(it)) {
                    mentioned = true
                    message.add(Component.text(it, NamedTextColor.YELLOW))
                } else {
                    message.add(Component.text(it))
                }
            }

            player.sendMessage(
                Component.join(
                    JoinConfiguration.separator(Component.space()),
                    Component.text("<${event.player.name}>"),
                    *message.toTypedArray()
                )
            )

            if (mentioned) {
                player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 0.5f, 1f))
            }
        }
    }
}