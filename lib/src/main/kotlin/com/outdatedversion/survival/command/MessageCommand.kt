package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Single
import co.aikar.commands.annotation.Syntax
import com.outdatedversion.survival.MessagingModule
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Handles the execution of the message commands
 * Validates player send command arguments
 * Find the player recipient
 * Tell module to send message
 */
@CommandAlias("msg|m|tell|whisper|pm|dm")
class MessageCommand(
    private val module: MessagingModule,
): BaseCommand() {


    @Default
    @CommandCompletion("@players @nothing")
    @Description("Sends a private message to another player.")
    @Syntax("<recipient> <message>")
    fun handleCommand(player: Player, @Single recipient: String, vararg message: String) {
        // Do not send message to the same player as the sender
        if (player.name.lowercase() == recipient.lowercase()) {
            player.sendMessage(
                Component.text(
                    "You cannot message that player",
                    NamedTextColor.RED
                )
            )
            return
        }

        if (message.isEmpty()) {
            player.sendMessage(
                Component.text(
                    "Usage: ",
                    NamedTextColor.YELLOW
                ).append(Component.text("/msg", NamedTextColor.GREEN))
                    .append(Component.text(" <recipient> <message>", NamedTextColor.WHITE))
            )
            return
        }

        // Search for the recipient in the server, check if they were found
        val recipientPlayer = Bukkit.getServer().getPlayer(recipient)
        if (recipientPlayer == null) {
            player.sendMessage(
                Component.text(
                    "Player $recipient was not found.",
                    NamedTextColor.RED
                ).decorate(TextDecoration.ITALIC)
            )
            return
        }
        module.sendPrivateMessage(player, recipientPlayer, Component.text(message.joinToString(separator = " ")))
    }
}