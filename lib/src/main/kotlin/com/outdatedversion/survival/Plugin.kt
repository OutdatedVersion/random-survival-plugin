package com.outdatedversion.survival

import co.aikar.commands.PaperCommandManager
import com.outdatedversion.survival.command.CoordinatesCommand
import com.outdatedversion.survival.command.TimeStampCommandCompletion
import com.outdatedversion.survival.command.TimeStampToggle
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Plugin: JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        this.commandManager = PaperCommandManager(this)
        this.commandManager.registerCommand(CoordinatesCommand())
        server.pluginManager.registerEvents(ChatMessage(this), this) // Converted from Java
        server.getPluginCommand("timezone")!!.setExecutor(TimeStampToggle(this)) // Converted from Java
        getCommand("timezone")!!.tabCompleter = TimeStampCommandCompletion()
       // val timeFormat = DateTimeFormatter.ofPattern("hh:mma v").withLocale(Locale.US).withZone(ZoneId.of("America/Chicago"))
        //this.server.pluginManager.registerEvents(ChatMentionHandler(timeFormat), this);
    }

    override fun onDisable() {
        this.commandManager.unregisterCommands()
    }
}
