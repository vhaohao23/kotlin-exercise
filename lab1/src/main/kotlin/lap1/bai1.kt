package org.goblin.lap1

import kotlin.math.sqrt

//Goblin program

//In một dòng văn bản
fun printWord() {
    println("Hello Goblin, This is the text to print!")
}

//Variable
fun variable() {
    val name:String = "Goblin"
    val age:Int = 69
    println("Goblin's name is $name and age is $age")
}

//Hàm có đối số
fun printName(name:String) {
    println("Hello $name")
}

//Check số nguyên tố
fun isPrime(number:Int):Boolean {
    if (number <=1) return false
    for (i in 2..sqrt(number.toDouble()).toInt()) {
        if (number % i == 0) return false
    }
    return true
}
//Lặp lại một thao tác bằng repeat()
fun printBorder(char:Char, size:Int) = repeat(size) {
    repeat(size) {
        print(char)
    }
    println()
}

//Câu lệnh có điều kiện với when
fun whenExample(number:Int){
    when(number) {
        1 -> println("One")
        2 -> println("Two")
        else -> println("More than two")
    }
}
//Class
class Dice {
    var sides = 6

    fun roll() {
        val randomNumber = (1..6).random()
        println(randomNumber)
    }
}

class Dice1 (val numSides: Int) {
    fun roll(): Int {
        val randomNumber = (1..numSides).random()
        return randomNumber
    }
}

fun main() {
    println("Hello Goblin")
    printWord()
    variable()
    printName("Goblin")
    println("Số 13  ${if (isPrime(13)) "là số nguyên tố" else "không phải số nguyên tố"}")
    printBorder('*', 10)
    whenExample(3)
    val myFirstDice = Dice1(6)
    println(myFirstDice.roll())
}

