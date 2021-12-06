package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import com.outdatedversion.survival.ChatProcessor
import com.outdatedversion.survival.asHumanReadableText
import com.outdatedversion.survival.model.PointOfInterest
import com.outdatedversion.survival.persistence.service.PointsOfInterestService
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("coordinates|coords")
class CoordinatesCommand(private val pointsOfInterestService: PointsOfInterestService, private val chatProcessor: ChatProcessor): BaseCommand() {
    @Default
    fun handleCommand(player: Player, vararg message: String) {
        val msg = message.joinToString(separator = " ", postfix = " ").trimStart()
        this.chatProcessor.processAndSendPlayerChat(
            player,
            Component.text("${msg}${player.location.asHumanReadableText()}")
        )

        if (msg.isNotEmpty()) {
            this.pointsOfInterestService.save(
                player.uniqueId,
                PointOfInterest(
                    id=UUID.randomUUID(),
                    coords=player.location.toVector(),
                    env=player.location.world.environment,
                    context=msg.trim()
                )
            )
        }
    }
}