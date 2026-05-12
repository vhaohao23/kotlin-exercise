package org.goblin.lap1



fun main(){
//    Tạo một nhóm từ danh sách
    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    val setOfNumbers = numbers.toSet()

    println(setOfNumbers)
    val set1 = setOf(1,2,3)
    val set2 = mutableSetOf(3, 4, 5)

    set1.intersect(set2) // 3
    set1.union(set2) // 1, 2, 3, 4, 5

    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23
    )

    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51


    peopleAges.forEach { print("${it.key} is ${it.value}, ") }

    println(peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ") )

    val filteredNames = peopleAges.filter { it.key.length < 4 }

    val words = listOf("about", "acute", "balloon", "best", "brief", "class")
    val filteredWords = words.filter { it.startsWith("b", ignoreCase = true) }
        .shuffled() // [brief, balloon, best]
        .take(2) // [brief, balloon]
        .sorted() // [balloon, brief]

    println(filteredWords)
}