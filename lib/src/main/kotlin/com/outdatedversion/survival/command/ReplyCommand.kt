package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import com.outdatedversion.survival.MessagingModule
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 *  Handles the execution of the reply command used to reply to/with private messages.
 *  Validates that reply can be used for the current sender if module has saved recipient
 *  Checks if that player is online
 *  Tells module to send the messages
 */
@CommandAlias("r|reply|rpl")
class ReplyCommand(
    private val module: MessagingModule,
): BaseCommand() {
    @Default
    @CommandCompletion("@nothing")
    @Description("Replies a private message to the last player you messaged.")
    @Syntax("<message>")
    fun handleCommand(player: Player, vararg message: String) {
        val savedPlayerToRespondTo = module.getReply(player.uniqueId)
        if (savedPlayerToRespondTo != null) {
            val recipientPlayer = Bukkit.getServer().getPlayer(module.getReply(player.uniqueId)!!)
            if (recipientPlayer == null) {
                var playerName = "The player"

                val offlinePlayer = Bukkit.getOfflinePlayer(module.getReply(player.uniqueId)!!)
                if (!offlinePlayer.name.isNullOrEmpty()) {
                    playerName = offlinePlayer.name!!
                }

                player.sendMessage(
                    Component.text(
                        "$playerName is no longer online.",
                        NamedTextColor.RED
                    ).decorate(TextDecoration.ITALIC)
                )
                module.removeReply(player.uniqueId)
            } else {
                if (message.isEmpty()) {
                    player.sendMessage(
                        Component.text(
                            "Usage: ",
                            NamedTextColor.YELLOW
                        ).append(Component.text("/reply", NamedTextColor.GREEN))
                            .append(Component.text(" <message>", NamedTextColor.WHITE))
                    )
                    return
                }

                module.sendPrivateMessage(
                    player,
                    recipientPlayer,
                    Component.text(message.joinToString(separator = " ").trim())
                )
            }
        } else {
            player.sendMessage(
                Component.text(
                    "Usage: ",
                    NamedTextColor.YELLOW
                ).append(Component.text("/msg", NamedTextColor.GREEN)).clickEvent(ClickEvent.suggestCommand("/msg"))
                    .append(Component.text(" must first be used before a reply can be sent.", NamedTextColor.WHITE))
            )
        }
    }
}