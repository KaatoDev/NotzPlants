package dev.kaato.notzplants.entities

import dev.kaato.notzplants.entities.enums.PlantsDrops

data class Reward(val message: String, val chance: Double, val commands: List<String>, val plants: List<PlantsDrops>) {
}