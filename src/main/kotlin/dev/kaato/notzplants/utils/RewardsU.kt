package dev.kaato.notzplants.utils

import dev.kaato.notzplants.Main.Companion.cf
import dev.kaato.notzplants.entities.Reward
import dev.kaato.notzplants.entities.enums.PlantsDrops
import dev.kaato.notzplants.utils.MessageU.send
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Player
import kotlin.random.Random

object RewardsU {
    var rewards = mutableListOf<Reward>()

    init {
        cf.getStringList("enabled-rewards").forEach {
            if (cf.contains("rewards.$it")) {
                val message = cf.getString("rewards.$it.message")
                val chance = cf.getDouble("rewards.$it.chance")
                val commands = cf.getStringList("rewards.$it.commands")
                val plants = cf.getStringList("rewards.$it.plants").map { PlantsDrops.valueOf(it) }
                rewards.add(Reward(message, chance, commands, plants))
            }
        }
    }

    fun getReward(p: Player, block: Block) {
        val rewards = rewards.filter { it.plants.contains(PlantsDrops.valueOf(block.type.name)) }
        rewards.forEach {
            if (!getBingo(it)) return@forEach

            it.commands.forEach {
                val cmd = it.replace("{player}", p.name)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
            }

            send(p, "rewardPrefix", it.message)
        }
    }

    fun getBingo(reward: Reward): Boolean {
        val bingo = Random.nextDouble(100.0)
        val res = bingo <= reward.chance
        return res
    }
}