package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor() : this("Default Room Name", 100)

    protected open val dangerLevel = 5
    val monster: Monster = Goblin(50)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

fun Monster.getSalutation(): String {
    return "Hello from Monster"
}

open class TownSquare() : Room("Town Square", 1000) {

    override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load(): String {
        return "loading Town Square room"
    }

}
