package dev.kaato.notzplants.entities.enums

import org.bukkit.Material
import kotlin.random.Random

enum class PlantsDrops(val material: Material, private val dropAmount: () -> Int) {
    CARROT(Material.CARROT_ITEM, { Random.nextInt(7) + 2 }),
    NETHER_WARTS(Material.NETHER_STALK, { Random.nextInt(8) + 2 }),
    POTATO(Material.POTATO_ITEM, { Random.nextInt(7) + 2 }),
    CROPS(Material.WHEAT, { Random.nextInt(4) + 1 });

    fun getDropAmount(): Int = dropAmount()
}