package com.outdatedversion.survival

import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.PaperCommandManager
import com.outdatedversion.survival.command.CoordinatesCommand
import com.outdatedversion.survival.command.PointsOfInterestCommand
import com.outdatedversion.survival.command.TimeZoneCommand
import com.outdatedversion.survival.format.ChatFormatter
import com.outdatedversion.survival.format.VanillaChatFormatter
import com.outdatedversion.survival.persistence.service.DataContainerPointsOfInterestService
import com.outdatedversion.survival.persistence.service.PointsOfInterestService
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.lang.IllegalArgumentException
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class Plugin : JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        this.commandManager = PaperCommandManager(this)
        this.commandManager.commandCompletions
            .registerCompletion("timezones") { TimeZone.getAvailableIDs().toMutableList() }

        val poiService: PointsOfInterestService = DataContainerPointsOfInterestService(this)
        val defaultTimeFormat =
            DateTimeFormatter.ofPattern("h:mma v").withLocale(Locale.US).withZone(ZoneId.of("America/Chicago"))
        val defaultChatFormatter: ChatFormatter = VanillaChatFormatter(this, defaultTimeFormat)
        val chatProcessor = DefaultChatProcessor(defaultChatFormatter)

        this.commandManager.registerCommand(CoordinatesCommand(poiService, chatProcessor))
        this.commandManager.registerCommand(PointsOfInterestCommand(poiService))
        this.commandManager.registerCommand(TimeZoneCommand(this))

        this.server.pluginManager.registerEvents(ChatEventListener(chatProcessor), this)
        this.server.pluginManager.registerEvents(SessionHandler(defaultTimeFormat), this)
    }

    override fun onDisable() {
        this.commandManager.unregisterCommands()
    }

    fun createKey(key: String): NamespacedKey {
        return NamespacedKey(this, key)
    }
}
