package dev.kaato.notzplants

import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import dev.kaato.notzplants.events.PlantsEv
import dev.kaato.notzplants.events.PlantsPreventEv
import dev.kaato.notzplants.files.NotzYAML
import dev.kaato.notzplants.utils.MessageU
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getPluginManager
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class Main : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
        lateinit var cf: FileConfiguration
        lateinit var prefix: String
        lateinit var wg: WorldGuardPlugin
        var started: Boolean = false
    }

    override fun onEnable() {
        plugin = this
        wg = WorldGuardPlugin.inst()
        cf = NotzYAML("config").config
        getPluginManager().registerEvents(PlantsPreventEv(), this)

        object : BukkitRunnable() {
            override fun run() {
                start()
            }
        }.runTaskLater(this, 20 * 20L)
    }

    fun regCommands() {
    }

    fun regEvents() {
        getPluginManager().registerEvents(PlantsEv(), this)
    }

    fun regTabs() {
    }

    private fun start() {
        regCommands()
        regEvents()
        regTabs()
        letters()
        started = true
    }

    private fun letters() {
        val site = "https://kaato.dev/plugins"
        MessageU.send(
            Bukkit.getConsoleSender(),
            """
                &2Inicializado com sucesso.
                &f┳┓    &2┏┓┓      
                &f┃┃┏┓╋┓&2┃┃┃┏┓┏┓╋┏
                &f┛┗┗┛┗┗&2┣┛┗┗┻┛┗┗┛
                
                $prefix &6Para mais plugins como este, acesse &b$site &6!!
                
            """.trimIndent()
        )
        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("notzplants.admin")) {
                it.sendMessage(MessageU.c("&2[&fNotzPlants&2] &aInicializado com sucesso."))
            }
        }
    }

    override fun onDisable() {
    }
}
