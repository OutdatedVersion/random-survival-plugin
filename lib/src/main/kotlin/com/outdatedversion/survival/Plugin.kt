package com.outdatedversion.survival

import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.PaperCommandManager
import com.outdatedversion.survival.command.*
import com.outdatedversion.survival.format.ChatFormatter
import com.outdatedversion.survival.format.PrivateMessageFormatter
import com.outdatedversion.survival.format.VanillaChatFormatter
import com.outdatedversion.survival.persistence.service.DataContainerPointsOfInterestService
import com.outdatedversion.survival.persistence.service.PointsOfInterestService
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Plugin: JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        this.commandManager = PaperCommandManager(this)
        this.commandManager.commandCompletions
            .registerCompletion("timezones") { TimeZone.getAvailableIDs().toMutableList() }
        this.commandManager.commandContexts.registerContext(UUID::class.java) { ctx ->
            val text = ctx.popFirstArg()
            try {
                return@registerContext UUID.fromString(text)
            } catch (_: IllegalArgumentException) {
                throw InvalidCommandArgument("$text is not a valid UUID")
            }
        }

        val poiService: PointsOfInterestService = DataContainerPointsOfInterestService(this)
        val defaultTimeFormat =
            DateTimeFormatter.ofPattern("h:mma v").withLocale(Locale.US).withZone(ZoneId.of("America/Chicago"))
        val defaultChatFormatter: ChatFormatter = VanillaChatFormatter(this, defaultTimeFormat)
        val chatProcessor = DefaultChatProcessor(defaultChatFormatter)
        // Creates the private message formatter used to format private messages, then create the messaging module and pass that
        val privateMessageFormatter = PrivateMessageFormatter(this, defaultTimeFormat)
        val messagingModule = MessagingModule(privateMessageFormatter)

        this.commandManager.registerCommand(CoordinatesCommand(poiService, chatProcessor))
        this.commandManager.registerCommand(PointsOfInterestCommand(poiService, chatProcessor))
        this.commandManager.registerCommand(TimeZoneCommand(this))
        this.commandManager.registerCommand(IsSlimeChunkCommand())
        // Register the /msg and /reply with the server and initiate its tracking
        messagingModule.register(this.commandManager, this)



        this.server.pluginManager.registerEvents(DataMigrationEventListener(poiService), this)
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
