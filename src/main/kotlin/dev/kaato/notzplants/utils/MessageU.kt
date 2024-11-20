package dev.kaato.notzplants.utils

import dev.kaato.notzplants.Main.Companion.cf
import dev.kaato.notzplants.Main.Companion.prefix
import dev.kaato.notzplants.utils.PlantsU.plants
import net.md_5.bungee.api.ChatColor
import net.minecraft.server.v1_8_R3.ChatComponentText
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import org.bukkit.Material
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

object MessageU {
    val messages = hashMapOf<String, String>()

    init {
        prefix = c(cf.getString("prefix"))

        cf.getStringList("enabled-messages").forEach {
            messages[it] = cf.getString("messages.$it")
        }
    }

    fun c(message: String): String {
        return ChatColor.translateAlternateColorCodes('&', message)
    }

    fun send(p: Player, msg: String) {
        p.sendMessage(c(prefix + msg))
    }

    fun sendText(p: Player, txt: String) {
        val msg = messages[txt] ?: return
        send(p, msg)
    }

    fun send(p: Player, txt: String, item: Material, quantity: Int, multiplier: String) {
        var msg = messages[txt] ?: return
        var plant = plants[item.name] ?: ""

        if (multiplier.isNotEmpty())
            msg = "&b[${multiplier.uppercase()}] $msg"

        msg = msg.replace("{item}", plant)
        msg = msg.replace("{quantity}", quantity.toString())

        sendActionBar(p, msg)
    }


    fun send(p: Player, txt: String, suffix: String) {
        var msg = arrayOf((messages[txt] ?: ""), suffix).joinToString(separator = " ")
        send(p, msg)
    }

    fun send(sender: ConsoleCommandSender, msg: String) {
        sender.sendMessage(c(prefix + msg))
    }

    fun sendActionBar(player: Player, message: String) {
        val chatComponent = ChatComponentText(c(message)) // Create the message component
        val packet = PacketPlayOutChat(chatComponent, 2) // Use GAME_INFO for action bar
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet) // Send the packet to the player
    }
}