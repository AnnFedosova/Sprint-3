package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int get() = Random.nextInt(0, 100)

    fun attack(opponent: Fightable): Int
}

class Player(
    val name: String,
    var isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int,
    override val damageRoll: Int
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints = opponent.healthPoints - damage
        println("$name attack opponent with damage=$damage")
        return damage
    }

}

abstract class Monster(
    val name: String,
    val description: String,
    override val powerType: String,
    override var healthPoints: Int,
    override val damageRoll: Int = 50
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints = opponent.healthPoints - damageRoll
        println("$name attack opponent with damage=$damageRoll")
        return damageRoll
    }

}

class Goblin(
    healthPoints: Int
) : Monster("Goblin", "Goblin Desc", "Magic", healthPoints) {

    override val damageRoll: Int
        get() = super.damageRoll / 2

}


