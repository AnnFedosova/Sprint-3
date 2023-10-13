package ru.sber.oop

fun main(args: Array<String>) {
    val player1 = Player("Tom", true, "magic", 100, 10)
    val room = Room()

    room.load()
    println("Start ${player1.name}'s healthPoints = ${player1.healthPoints}")
    println("Start ${room.monster.name}'s healthPoints = ${room.monster.healthPoints}")

    while(room.monster.healthPoints > 0) {
        player1.attack(room.monster)
        println("Monster's healthPoints = ${room.monster.healthPoints}")
    }

    println("Finish ${player1.name}'s healthPoints = ${player1.healthPoints}")
    println("Finish ${room.monster.name}'s healthPoints = ${room.monster.healthPoints}")
}

