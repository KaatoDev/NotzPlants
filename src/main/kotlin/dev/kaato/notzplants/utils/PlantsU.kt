package dev.kaato.notzplants.utils

import dev.kaato.notzplants.Main.Companion.cf
import dev.kaato.notzplants.entities.enums.PlantsDrops
import dev.kaato.notzplants.utils.MessageU.send
import dev.kaato.notzplants.utils.RewardsU.getReward
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

object PlantsU {
    var dropItem = false
    var blockedWorlds = listOf<String>()
    var multipliers = hashMapOf<String, Double>()
    var plants = hashMapOf<String, String>()

    init {
        dropItem = cf.getBoolean("dropItem")
        blockedWorlds = cf.getStringList("block-worlds")

        cf.getMapList("multipliers").forEach {
            multipliers[it.keys.first().toString()] = it.values.first().toString().toDouble()
        }

        arrayOf("NETHER_WARTS", "CROPS", "CARROT", "POTATO").forEach {
            plants[it] = cf.getString("items.$it")
        }
    }

    fun getDrop(p: Player, item: Material, multiplier: Double): ItemStack {
        val plantDrop = PlantsDrops.valueOf(item.name)

        val quantity = getQuantity(p, plantDrop, multiplier)
        val drop = ItemStack(plantDrop.material, quantity)

        return drop
    }

    fun getQuantity(p: Player, plantDrop: PlantsDrops, multiplier: Double): Int {
        var drops = plantDrop.getDropAmount().toDouble()

        drops *= multiplier

        if (drops - drops.toInt() > 0) {
            drops++
        }

        return drops.toInt()
    }

    fun sendDrops(p: Player, block: Block) {
        val m = multipliers.filter {
            p.hasPermission("notzplants.multiplier.${it.key}")
        }

        var multi = m.keys.lastOrNull()?:""
        val multiplier = m.values.lastOrNull() ?: 1.0

        val drop = getDrop(p, block.type, multiplier)

        if (dropItem)
            block.world.dropItemNaturally(block.location, drop)
        else p.inventory.addItem(drop)

        block.data = 1

        getReward(p, block)

        send(p, "actionBar", block.type, drop.amount, multi)
    }
}