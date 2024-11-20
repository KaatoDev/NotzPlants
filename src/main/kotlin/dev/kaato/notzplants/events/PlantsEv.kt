package dev.kaato.notzplants.events

import com.sk89q.worldguard.protection.flags.DefaultFlag
import dev.kaato.notzplants.Main.Companion.started
import dev.kaato.notzplants.Main.Companion.wg
import dev.kaato.notzplants.utils.MessageU.sendText
import dev.kaato.notzplants.utils.PlantsU.sendDrops
import org.bukkit.Material.*
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import java.util.UUID

@Suppress("DEPRECATION")
class PlantsEv : Listener {
    val spam = hashMapOf<UUID, Boolean>()
    private val plants = arrayOf(CARROT, NETHER_WARTS, POTATO, CROPS)

    @EventHandler
    fun preventBreakPlant(e: BlockBreakEvent) {
        val block = e.block ?: return
        val p = e.player ?: return

        if (pass(block) && !started)
            e.isCancelled = true
    }

    @EventHandler
    fun breakPlant(e: BlockBreakEvent) {
        val block = e.block ?: return
        val p = e.player ?: return

        if (!wg.regionContainer.createQuery().testBuild(block.location, wg.wrapPlayer(p), DefaultFlag.BLOCK_BREAK) && !p.hasPermission("notzplants.admin")) {
            e.isCancelled = true
            return
        }

        if (spam[p.uniqueId] == true) {
            sendText(p, "spamWarn1")
            e.isCancelled = true
            spam[p.uniqueId] = false
            return
        }

        spam[p.uniqueId] = true

        if (!pass(block) || p.isSneaking) {
            spam.remove(p.uniqueId)
            return
        }

        e.isCancelled = true

        if (isNormalSeed(block)) {
            sendText(p, "breakTip")
            spam.remove(p.uniqueId)
            return
        }

        sendDrops(p, block)

        spam.remove(p.uniqueId)
    }

    @EventHandler
    fun blockFarmland(e: EntityChangeBlockEvent) {
        val block = e.block

        if (block.type == SOIL && e.to == DIRT)
            e.isCancelled = true
    }


    fun pass(block: Block): Boolean {
        return plants.contains(block.type)
    }

    fun isNormalSeed(block: Block): Boolean {
        return when (block.type) {
            CARROT, POTATO, CROPS ->
                block.data < 7

            MELON_STEM, PUMPKIN_STEM ->
                true

            NETHER_WARTS ->
                block.data < 3

            else -> false
        }
    }
}