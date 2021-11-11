package com.outdatedversion.survival

import co.aikar.commands.PaperCommandManager
import com.outdatedversion.survival.command.CoordinatesCommand
import org.bukkit.plugin.java.JavaPlugin

class Plugin: JavaPlugin() {
    override fun onEnable() {
        val commandManager = PaperCommandManager(this)
        commandManager.registerCommand(CoordinatesCommand())

        this.server.pluginManager.registerEvents(ChatMentionHandler(), this);
    }
}