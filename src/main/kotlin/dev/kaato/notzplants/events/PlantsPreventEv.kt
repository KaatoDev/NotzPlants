package dev.kaato.notzplants.events

import dev.kaato.notzplants.Main.Companion.started
import dev.kaato.notzplants.utils.MessageU.send
import org.bukkit.Bukkit
import org.bukkit.Material.*
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class PlantsPreventEv : Listener {
    private val plants = arrayOf(CARROT, NETHER_WARTS, POTATO, CROPS)

    @EventHandler
    fun preventBreakPlant(e: BlockBreakEvent) {
        val block = e.block ?: return
        val p = e.player ?: return

        if (!started) {
            if (plants.contains(block.type)) {
                send(Bukkit.getConsoleSender(), "&2NotzPlants &cnão inicializado ainda! Prevenção de quebra de plantação ativada.")
                e.isCancelled = true
            }
        } else {
            send(Bukkit.getConsoleSender(), "&aNotzPlants &afoi inicializado e a prevenção de quebra de plantação foi removida!")
            HandlerList.unregisterAll(this)
        }
    }
}