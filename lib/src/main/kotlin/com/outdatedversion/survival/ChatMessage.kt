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
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.persistence.PersistentDataType
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


class ChatMessage(main: Plugin) : Listener {
    private val main: Plugin
    @EventHandler
    fun onPlayerSendChatMessage(event: AsyncChatEvent) {
        val userMessage = event.originalMessage()
        event.isCancelled = true

        val namespacedKey = NamespacedKey(main, "timezone")

        val parts = PlainTextComponentSerializer.plainText().serialize(event.message()).split(" ")

        Bukkit.getOnlinePlayers().forEach { player ->
            val message = mutableListOf<Component>()
            var mentioned = false
            val nameRegex = """(.*)(@?${player.name})(.*)""".toRegex(RegexOption.IGNORE_CASE)
            val data = player.persistentDataContainer
            val timezoneOffSet = data.get(namespacedKey, PersistentDataType.STRING)
            // Gets time in specified time-zone
            val timeZone = TimeZone.getTimeZone("GMT$timezoneOffSet")
            val timeFormat = SimpleDateFormat("hh:mm a") // Specify time format eg. 01:30 pm
            timeFormat.timeZone = timeZone // Sets time zonm
            val date = Date()
            val currentTime = timeFormat.format(date)
            println(currentTime)
//            player.sendMessage(
//                Component.text("<" + event.player.name + "> ").append(
//                    event.message().hoverEvent(
//                        HoverEvent.showText(Component.text(currentTime))
//                    )
//                )
//            )

            parts.forEach {
                if (nameRegex.matches(it)) {
                    mentioned = true
                    message.add(Component.text(it, NamedTextColor.YELLOW))
                } else {
                    message.add(Component.text(it))
                }
            }

            val timestampText = Component.text("Sent at ${currentTime}", NamedTextColor.GRAY)
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

    init {
        this.main = main
    }
}