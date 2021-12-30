package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.outdatedversion.survival.MessagingModule
import com.outdatedversion.survival.Plugin
import com.outdatedversion.survival.asText
import com.outdatedversion.survival.model.PointOfInterest
import com.outdatedversion.survival.model.asComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.*

@CommandAlias("msg|tell|whisper")
class MessageCommand(
    private val module: MessagingModule

) : BaseCommand() {
    @Default
    @CommandCompletion("@players @nothing")
    @Description("Sends a private message to another player.")
    @Syntax("<recipient> <message>")
    fun handleCommand(player: Player, @Single recipient: String, vararg message: String) {
        // Do not send message to the same player as the sender
        if (player.name.lowercase() == recipient.lowercase()) {
            sendCannotMessagePlayerMessage(player)
            return
        }

        if (message.isEmpty()) {
            sendHelpMessage(player)
            return
        }
        // Search for the recipient in the server, check if they were found
        val recipientPlayer: Player? = Bukkit.getServer().getPlayer(recipient)
        if (recipientPlayer == null) {
            player.sendMessage(
                Component.text(
                    "Player $recipient was not found.",
                    NamedTextColor.RED
                ).decorate(TextDecoration.ITALIC)
            )
            return
        }
        module.sendPrivateMessage(player, recipientPlayer, Component.text(message.joinToString(separator = " ", postfix = " ").trimStart()))
    }

    fun sendHelpMessage(player: Player) {
        player.sendMessage(
            Component.text(
                "Usage: ",
                NamedTextColor.YELLOW
            ).append(Component.text("/msg", NamedTextColor.GREEN))
                .append(Component.text(" <recipient> <message>", NamedTextColor.WHITE))
        )

    }

    fun sendCannotMessagePlayerMessage(player: Player) {
        player.sendMessage(
            Component.text(
                "You cannot message that player",
                NamedTextColor.RED
            )
        )
    }
}