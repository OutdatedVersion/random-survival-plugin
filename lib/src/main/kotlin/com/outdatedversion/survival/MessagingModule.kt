package com.outdatedversion.survival

import co.aikar.commands.PaperCommandManager
import com.outdatedversion.survival.command.MessageCommand
import com.outdatedversion.survival.command.ReplyCommand
import com.outdatedversion.survival.format.PrivateMessageFormatter
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

/**
 * Manages and registers the private messages commands, and recipient reply tracking
 * @param privateMessageFormatter An instance of a private message formatter used to format private messages for players
 */
class MessagingModule(private val privateMessageFormatter: PrivateMessageFormatter) : Listener{
    private val recipientMap = hashMapOf<UUID, UUID>()

    /**
     * Register the /msg and the /reply commands with the server
     * Register the join and leave listeners with the [plugin] instance
     * @param commandManager An instance of the server's command manager
     * @param plugin instance of the plugin
     */
    fun register(commandManager: PaperCommandManager, plugin: Plugin) {
        commandManager.registerCommand(MessageCommand(this))
        commandManager.registerCommand(ReplyCommand(this))
        plugin.server.pluginManager.registerEvents(this, plugin)
    }


    /**
     * Sets the reply [recipient] for the [sender] in a map
     */
    fun setReply(sender: UUID, recipient: UUID) {
        recipientMap[sender] = recipient
    }

    /**
     * Get the reply recipient UUID for the [sender]
     * @param sender The sender of the message to get the reply recipient for as UUID
     * @return UUID of the reply recipient
     */
    fun getReply(sender: UUID): UUID? {
        return recipientMap[sender]
    }

    /**
     * Remove the reply recipient for the [sender]
     */
    fun removeReply(sender: UUID) {
        recipientMap.remove(sender)
    }

    /**
     * Sends the appropriate message to the [sender] and [recipient]
     * Play the notification sound, and save the reply recipients to the map
     *
     */
    fun sendPrivateMessage(sender: Player, recipient: Player, message: Component) {
        sender.sendMessage(privateMessageFormatter.apply(sender, recipient, message, true))
        recipient.sendMessage(privateMessageFormatter.apply(sender, recipient, message, false))

        recipient.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 0.8f, 1f))

        setReply(sender.uniqueId, recipient.uniqueId)
        // If the message recipient does not have a reply recipient set, set the reply recipient when they receive a message too
        if (recipientMap[recipient.uniqueId] == null) {
            setReply(recipient.uniqueId, sender.uniqueId)
        }
    }

    /**
     * Remove player's reply recipient when they leave the server
     */
    @EventHandler
    fun handleQuit(event: PlayerQuitEvent){
            removeReply(event.player.uniqueId)
    }
}