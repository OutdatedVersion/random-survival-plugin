package com.outdatedversion.survival

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatEventListener(private val chatProcessor: ChatProcessor): Listener {
    @EventHandler
    fun handleChatEvent(event: AsyncChatEvent) {
        event.isCancelled = true
        this.chatProcessor.processAndSendPlayerChat(event.player, event.message())
    }
}