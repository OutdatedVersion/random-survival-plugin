package com.outdatedversion.survival.command;

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Private
import co.aikar.commands.annotation.Single
import co.aikar.commands.annotation.Subcommand
import com.outdatedversion.survival.ChatProcessor
import com.outdatedversion.survival.format
import com.outdatedversion.survival.formatEnvironment
import com.outdatedversion.survival.model.PointOfInterest
import com.outdatedversion.survival.model.asComponent
import com.outdatedversion.survival.persistence.service.PointsOfInterestService
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("pointsofinterest|poi")
class PointsOfInterestCommand(
    private val pointsOfInterestService: PointsOfInterestService,
    private val chatProcessor: ChatProcessor,
): BaseCommand() {
    @Default
    fun handleCommand(player: Player) {
        val points = this.pointsOfInterestService.getAll(player.uniqueId)

        if (points.isEmpty()) {
            player.sendMessage(Component.text("You have no locations saved", NamedTextColor.RED, TextDecoration.BOLD))
        } else {
            points.forEach { poi ->
                val sayTooltip = HoverEvent.showText(
                    Component.text("Click to say the location of ", NamedTextColor.GRAY)
                        .append(Component.text(poi.context, NamedTextColor.YELLOW))
                )
                val removeTooltip = HoverEvent.showText(
                    Component.text("Click to remove ", NamedTextColor.GRAY)
                        .append(Component.text(poi.context, NamedTextColor.YELLOW))
                )
                player.sendMessage(
                    Component.join(
                        JoinConfiguration.separator(Component.space()),
                        Component.text("-"),
                        this.formatPointOfInterest(poi),
                        Component.text("[say]", NamedTextColor.GREEN)
                            .hoverEvent(sayTooltip)
                            .clickEvent(ClickEvent.runCommand("/pointsofinterest say ${poi.id}")),
                        Component.text("[remove]", NamedTextColor.RED)
                            .hoverEvent(removeTooltip)
                            .clickEvent(ClickEvent.runCommand("/pointsofinterest remove ${poi.id}"))
                    )
                )
            }
        }

        player.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.space()),
                Component.text("Use", NamedTextColor.GRAY),
                Component.text("/coordinates <context>", NamedTextColor.YELLOW),
                Component.text("to add a point of interest", NamedTextColor.GRAY)
            ).decorate(TextDecoration.ITALIC)
        )
    }

    @Private
    @Subcommand("remove")
    fun handleRemove(player: Player, @Single id: String) {
        val poi = this.pointsOfInterestService.remove(player.uniqueId, UUID.fromString(id))

        if (poi != null) {
            player.sendMessage(Component.text("Removed ", NamedTextColor.RED).append(this.formatPointOfInterest(poi)))
        } else {
            player.sendMessage(Component.text("No PoI with ID '$id'", NamedTextColor.RED))
        }
    }

    @Private
    @Subcommand("say")
    fun handleSay(player: Player, @Single id: String) {
        val poi = this.pointsOfInterestService.get(player.uniqueId, UUID.fromString(id))

        if (poi == null) {
            player.sendMessage(Component.text("That did not match any of your saved points", NamedTextColor.RED))
            return
        }

        this.chatProcessor.processAndSendPlayerChat(player, poi.asComponent())
    }

    @Private
    @Subcommand("save")
    fun handleSave(player: Player, @Single ownerId: String, @Single id: String) {
        val poi = this.pointsOfInterestService.get(UUID.fromString(ownerId), UUID.fromString(id))

        if (poi == null) {
            player.sendMessage(Component.text("Could not find that PoI", NamedTextColor.RED))
            return
        }

        val newPoi = this.pointsOfInterestService.save(player.uniqueId, poi.copy(id = UUID.randomUUID()))
        player.sendMessage(
            Component.text("Saved ", NamedTextColor.GRAY)
                .append(this.formatPointOfInterest(newPoi))
                .append(Component.text(" View with /poi", NamedTextColor.DARK_AQUA))
        )
    }

    private fun formatPointOfInterest(poi: PointOfInterest): Component {
        return Component.join(
            JoinConfiguration.separator(Component.space()),
            Component.text(poi.context, NamedTextColor.YELLOW),
            Component.text(poi.coords.format(), NamedTextColor.AQUA),
            Component.text("(${formatEnvironment(poi.env)})", NamedTextColor.GRAY)
        )
    }
}
