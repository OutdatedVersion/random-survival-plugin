package com.outdatedversion.survival

import com.outdatedversion.survival.format.ChatFormatter
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.regex.Pattern

class DefaultChatProcessor(private val defaultFormatter: ChatFormatter): ChatProcessor {
    override fun processAndSendPlayerChat(player: Player, message: Component) {
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            var mentioned = false
            val nameRegex = """(@?${Pattern.quote(onlinePlayer.name)})""".toPattern(Pattern.CASE_INSENSITIVE)
            val itemRegex = """(\[item])""".toPattern(Pattern.CASE_INSENSITIVE)

            val mentionReplacementConfig = TextReplacementConfig
                .builder()
                .match(nameRegex)
                .replacement { match, _ ->
                    mentioned = true
                    Component.text(match.group(), NamedTextColor.YELLOW)
                }
                .build()
            val itemReplacementConfig = TextReplacementConfig
                .builder()
                .match(itemRegex)
                .replacement { _, _ ->
                    val mainHandItem = player.inventory.itemInMainHand
                    val item = if (mainHandItem.type == Material.AIR) player.inventory.itemInOffHand else mainHandItem
                    Component.text(
                        "${snakecaseToDisplay(item.type.name)}${if (item.amount > 1) " x${item.amount}" else ""}",
                        NamedTextColor.AQUA
                    ).hoverEvent(item.asHoverEvent())
                }
                .build()

            onlinePlayer.sendMessage(
                defaultFormatter.apply(
                    player,
                    message.replaceText(mentionReplacementConfig)
                           .replaceText(itemReplacementConfig)
                )
            )

            if (mentioned) {
                onlinePlayer.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 0.8f, 1f))
            }
        }
    }
}