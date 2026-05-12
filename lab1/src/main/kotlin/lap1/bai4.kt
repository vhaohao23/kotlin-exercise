package org.goblin.lap1



object DataProviderManager {
    private var green="green"
    fun getGreen():String{
        return green
    }
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

fun main() {
    suspend fun getValue(): Double {
        return 123.0
    }


    var obj=DataProviderManager
    println(obj.getGreen())
    try{

    }
    catch (exception: Exception){

    }


}