package com.outdatedversion.survival

import co.aikar.commands.PaperCommandManager
import com.outdatedversion.survival.command.CoordinatesCommand
import com.outdatedversion.survival.command.PointsOfInterestCommand
import com.outdatedversion.survival.command.TimeZoneCommand
import com.outdatedversion.survival.persistence.service.DataContainerPointsOfInterestService
import com.outdatedversion.survival.persistence.service.PointsOfInterestService
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class Plugin : JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        val poiService: PointsOfInterestService = DataContainerPointsOfInterestService(this)

        this.commandManager = PaperCommandManager(this)
        this.commandManager.registerCommand(CoordinatesCommand(poiService))
        this.commandManager.registerCommand(PointsOfInterestCommand(poiService))
        this.commandManager.registerCommand(TimeZoneCommand(this))
        this.commandManager.commandCompletions
            .registerCompletion("timezones") { TimeZone.getAvailableIDs().toMutableList() }

        val timeFormat =
            DateTimeFormatter.ofPattern("h:mma v").withLocale(Locale.US).withZone(ZoneId.of("America/Chicago"))
        this.server.pluginManager.registerEvents(ChatMentionHandler(this, timeFormat), this)
        this.server.pluginManager.registerEvents(SessionHandler(timeFormat), this)
    }

    override fun onDisable() {
        this.commandManager.unregisterCommands()
    }

    fun createKey(key: String): NamespacedKey {
        return NamespacedKey(this, key)
    }
}
