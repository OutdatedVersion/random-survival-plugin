package com.outdatedversion.survival

import co.aikar.commands.PaperCommandManager
import com.outdatedversion.survival.command.CoordinatesCommand
import com.outdatedversion.survival.command.MessageCommand
import com.outdatedversion.survival.command.ReplyCommand
import com.outdatedversion.survival.format.PrivateMessageFormatter
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.*

class MessagingModule(private val privateMessageFormatter: PrivateMessageFormatter) {
    val recipientMap = hashMapOf<UUID, UUID>()


    fun register(commandManager: PaperCommandManager) {

        commandManager.registerCommand(MessageCommand(this))
        commandManager.registerCommand(ReplyCommand(this))
    }


    fun setReply(sender: UUID, recipient: UUID) {
        recipientMap.put(sender, recipient);
    }

    fun getReply(sender: UUID): UUID? {
        if(recipientMap.containsKey(sender)) {
            return recipientMap.get(sender)
        }
        return null
    }

    fun removeReply(sender: UUID){
        recipientMap.remove(sender)
    }

    fun sendPrivateMessage(sender: Player, recipient: Player, message: Component) {
        sender.sendMessage(privateMessageFormatter.apply(sender, recipient, message, true))
        recipient.sendMessage(privateMessageFormatter.apply(sender, recipient, message, false))
        recipient.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 0.8f, 1f))
        setReply(sender.uniqueId, recipient.uniqueId)
    }

}