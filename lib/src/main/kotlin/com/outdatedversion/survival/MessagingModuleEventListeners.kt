package com.outdatedversion.survival

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Implements bukkit listeners used with the Messaging Module
 */
class MessagingModuleEventListeners(private val messagingModule: MessagingModule) : Listener {

    /**
     * Remove player's reply recipient when they leave the server
     */
    @EventHandler
    fun handleQuit(event: PlayerQuitEvent){
        if (messagingModule.getReply(event.player.uniqueId) != null) {
            messagingModule.removeReply(event.player.uniqueId);

        }
    }

}