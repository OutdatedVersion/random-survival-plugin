package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Chicken
import org.bukkit.entity.Player

@CommandAlias("isslimechunk")
class IsSlimeChunkCommand: BaseCommand() {
    @Default
    fun handleCommand(player: Player) {
        val chunk = player.location.chunk

        if (chunk.isSlimeChunk) {
            player.sendMessage(
                Component.text("You are in a slime chunk (${chunk.x}, ${chunk.z})", NamedTextColor.GREEN)
            )
        } else {
            player.sendMessage(
                Component.text("You are not in a slime chunk (${chunk.x}, ${chunk.z})", NamedTextColor.RED)
            )
        }
    }
}