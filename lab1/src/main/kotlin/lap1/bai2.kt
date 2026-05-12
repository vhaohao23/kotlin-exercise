package org.goblin.lap1

//Lớp trừu tượng
abstract class Dwelling() {
}

//Lớp trừu tượng có một phương thức trừu tượng
abstract class House: Dwelling() {
    abstract fun paint()
}

//Lớp trừu tượng có một phương thức trừu tượng
abstract class Apartment: Dwelling() {
    abstract fun florArea() : Double
}

//Danh sách
fun listTest(){
    val list = listOf(1,2,3,4,5)
//    Kich thuoc
    println(list.size)
//    Muc dau
    println(list.first())
//    Lay ban sao dao nguoc
    println(list.reversed())

    val entrees = mutableListOf<String>()
    entrees.add("pizza")
    entrees.add("burger")
    entrees.add("fries")
    println(entrees)
    entrees[0] = "chicken"
    println(entrees)
    entrees.removeAt(2)
    println(entrees)

    for(item in 0..entrees.size-1){
        println(entrees[item])
    }
}


//Chuoi
fun stringTest(){
    val name = "Goblin"
    println(name.length)

    val number:Int = 10
    val group:Int = 5
    println("${number * group} people in group")
}



fun main() {
    listTest()
    stringTest()
}