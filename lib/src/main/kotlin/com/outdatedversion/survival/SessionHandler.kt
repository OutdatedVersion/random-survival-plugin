package com.outdatedversion.survival

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.time.Instant
import java.time.format.DateTimeFormatter

class SessionHandler(private val defaultTimeFormatter: DateTimeFormatter): Listener {
    @EventHandler
    fun handleJoin(event: PlayerJoinEvent) {
        event.joinMessage(event.joinMessage()?.hoverEvent(this.buildTimestampComponent()))
    }

    @EventHandler
    fun handleLeave(event: PlayerQuitEvent) {
        event.quitMessage(event.quitMessage()?.hoverEvent(this.buildTimestampComponent()))
    }

    private fun buildTimestampComponent(): Component {
        return Component.text("At ${this.defaultTimeFormatter.format(Instant.now())}", NamedTextColor.GRAY)
    }
}