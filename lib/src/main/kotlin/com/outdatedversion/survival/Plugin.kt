package com.outdatedversion.survival

import co.aikar.commands.CommandManager
import co.aikar.commands.PaperCommandManager
import com.outdatedversion.survival.command.CoordinatesCommand
import com.outdatedversion.survival.command.TimeStampCommandCompletion
import com.outdatedversion.survival.command.TimeStampToggle
import org.bukkit.plugin.java.JavaPlugin
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class Plugin: JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        this.commandManager = PaperCommandManager(this)
        this.commandManager.registerCommand(CoordinatesCommand())

        val timeFormat = DateTimeFormatter.ofPattern("hh:mma v").withLocale(Locale.US).withZone(ZoneId.of("America/Chicago"))
        this.server.pluginManager.registerEvents(ChatMentionHandler(timeFormat, this), this)
        this.server.pluginManager.registerEvents(SessionHandler(timeFormat), this)


        /*
        Old Bukkit command systems, TODO: Update to new  command system
         */
        server.getPluginCommand("timezone")!!.setExecutor(TimeStampToggle(this)) // Converted from Java
        getCommand("timezone")!!.tabCompleter = TimeStampCommandCompletion()
    }

    override fun onDisable() {
        this.commandManager.unregisterCommands()
    }
}
