package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import org.bukkit.World
import org.bukkit.entity.Player

@CommandAlias("coordinates|coords")
class CoordinatesCommand: BaseCommand() {
    @Default
    fun handleCommand(player: Player, vararg message: String) {
        val loc = player.location
        val env = this.formatEnvironment(loc.world.environment)
        val msg = message.joinToString(separator = " ", postfix = " ")
        player.chat("${msg}${loc.blockX}, ${loc.blockY}, ${loc.blockZ} (${env})")
    }

    private fun formatEnvironment(environment: World.Environment): String {
        return when (environment) {
            World.Environment.NORMAL -> "Overworld"
            World.Environment.NETHER -> "The Nether"
            World.Environment.THE_END -> "The End"
            else -> "Unknown"
        }
    }
}