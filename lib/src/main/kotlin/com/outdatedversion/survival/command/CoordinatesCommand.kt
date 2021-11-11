package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import org.bukkit.entity.Player

@CommandAlias("coordinates|coords")
class CoordinatesCommand: BaseCommand() {
    @Default
    fun handleCommand(player: Player, vararg message: String) {
        val loc = player.location
        player.chat("${message.joinToString(separator = " ", postfix = " ")}${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
    }
}