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

@CommandAlias("r|reply|rpl")
class ReplyCommand(private val module : MessagingModule

): BaseCommand() {
    @Default
    @CommandCompletion("@nothing")
    @Description("Replies a private message to the last player you messaged.")
    @Syntax("<message>")
    fun handleCommand(player: Player, vararg message: String) {
      if(module.getReply(player.uniqueId) != null){
          val recipientPlayer: Player? = Bukkit.getServer().getPlayer(module.getReply(player.uniqueId)!!)
          if (recipientPlayer == null) {
              player.sendMessage(
                  Component.text(
                      "The player is no longer online.",
                      NamedTextColor.RED
                  ).decorate(TextDecoration.ITALIC)
              )
              module.removeReply(player.uniqueId)
              return
          }
          if(message.isEmpty()){
              sendHelpMessage(player)
              return
          }

          module.sendPrivateMessage(player, recipientPlayer, Component.text(message.joinToString(separator = " ", postfix = " ").trimStart()))

      }else{
          player.sendMessage(
              Component.text(
                  "Usage: ",
                  NamedTextColor.YELLOW
              ).append(Component.text("/msg", NamedTextColor.GREEN))
                  .append(Component.text(" must first be used before a reply can be sent.", NamedTextColor.WHITE))
          )
      }
    }

    fun sendHelpMessage(player: Player) {
        player.sendMessage(
            Component.text(
                "Usage: ",
                NamedTextColor.YELLOW
            ).append(Component.text("/reply", NamedTextColor.GREEN))
                .append(Component.text(" <message>", NamedTextColor.WHITE))
        )

    }
}