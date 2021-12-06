package com.outdatedversion.survival

import com.outdatedversion.survival.format.ChatFormatter
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.regex.Pattern

class DefaultChatProcessor(private val defaultFormatter: ChatFormatter): ChatProcessor {
    override fun processAndSendPlayerChat(player: Player, message: Component) {
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            var mentioned = false
            val nameRegex = """(@?${onlinePlayer.name})""".toPattern(Pattern.CASE_INSENSITIVE)

            val mentionReplacementConfig = TextReplacementConfig
                .builder()
                .match(nameRegex)
                .replacement { match, _ ->
                    mentioned = true
                    Component.text(match.group(), NamedTextColor.YELLOW)
                }
                .build()

            onlinePlayer.sendMessage(
                defaultFormatter.apply(player, message.replaceText(mentionReplacementConfig))
            )

            if (mentioned) {
                onlinePlayer.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 0.8f, 1f))
            }
        }
    }
}