package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab1.ui.theme.Lab1Theme
import kotlin.math.PI

// ===== Bài 1: Biến, Hàm, Lớp, Điều kiện =====
private fun bai1Results(): List<String> {
    val results = mutableListOf<String>()

    val age = "5"
    val name = "Rover"
    var roll = 6
    val rolledValue: Int = 4
    results.add("val age=\"$age\", name=\"$name\"")
    results.add("var roll=$roll, val rolledValue=$rolledValue")
    results.add("String template: \"You are already ${age}!\"")
    results.add("You are already $age days old, $name!")

    fun printBorder(border: String, timesToRepeat: Int) = border.repeat(timesToRepeat)
    results.add("printBorder(\"=\", 10) = ${printBorder("=", 10)}")

    fun rollDice(): Int = (1..6).random()
    results.add("fun roll(): Int = (1..6).random() → ${rollDice()}")

    class Dice(val numSides: Int) {
        fun roll(): Int = (1..numSides).random()
    }
    val dice = Dice(6)
    results.add("Dice(6).roll() = ${dice.roll()}")

    val sb = StringBuilder()
    repeat(10) { sb.append("=") }
    results.add("repeat(10) { print(\"=\") } → $sb")

    val num = 4
    val ifResult = if (num > 4) "greater than 4" else if (num == 4) "equal to 4" else "less than 4"
    results.add("if/else (num=4): $ifResult")

    val r = (1..6).random()
    val whenResult = when (r) {
        1 -> "rolled 1"; 2 -> "rolled 2"; 3 -> "rolled 3"
        4 -> "rolled 4"; 5 -> "rolled 5"; else -> "rolled 6"
    }
    results.add("when(roll=$r): $whenResult")

    return results
}

// ===== Bài 2: Lớp trừu tượng, Kế thừa, Danh sách =====
private abstract class Dwelling(private val residents: Int) {
    abstract val buildingMaterial: String
    abstract val capacity: Int
    fun hasRoom() = residents < capacity
    abstract fun floorArea(): Double
    fun info() = "$buildingMaterial | area=${"%.1f".format(floorArea())} | hasRoom=${hasRoom()}"
}

private class SquareCabin(residents: Int, val length: Double) : Dwelling(residents) {
    override val buildingMaterial = "Wood"
    override val capacity = 6
    override fun floorArea() = length * length
}

private open class RoundHut(residents: Int, val radius: Double) : Dwelling(residents) {
    override val buildingMaterial = "Straw"
    override val capacity = 4
    override fun floorArea() = PI * radius * radius
}

private class RoundTower(residents: Int, radius: Double, val floors: Int) : RoundHut(residents, radius) {
    override val buildingMaterial = "Stone"
    override val capacity = 4 * floors
    override fun floorArea() = super.floorArea() * floors
}

private fun bai2Results(): List<String> {
    val results = mutableListOf<String>()

    val cabin = SquareCabin(6, 10.0)
    val hut = RoundHut(3, 5.0)
    val tower = RoundTower(4, 5.0, 3)
    results.add("SquareCabin: ${cabin.info()}")
    results.add("RoundHut: ${hut.info()}")
    results.add("RoundTower: ${tower.info()}")

    val numbers = listOf(1, 2, 3, 4, 5, 6)
    results.add("listOf size=${numbers.size}, first=${numbers[0]}")
    results.add("reversed: ${listOf("red","blue","green").reversed()}")

    val entrees = mutableListOf<String>()
    entrees.add("spaghetti")
    entrees[0] = "lasagna"
    results.add("mutableList add+modify: $entrees")

    val forResult = buildString { for (n in numbers) append("$n ") }
    results.add("for loop: $forResult")

    val whileResult = buildString {
        var i = 0
        while (i < numbers.size) { append("${numbers[i]} "); i++ }
    }
    results.add("while loop: $whileResult")

    val withResult = with(cabin) { "capacity=$capacity, material=$buildingMaterial, hasRoom=${hasRoom()}" }
    results.add("with(cabin): $withResult")

    results.add("kotlin.math.PI * 5^2 = ${"%.2f".format(PI * 5 * 5)}")

    return results
}

// ===== Bài 3: Set, Map, Lambda, Elvis =====
private fun bai3Results(): List<String> {
    val results = mutableListOf<String>()

    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    val setOfNumbers = numbers.toSet()
    results.add("list→set (removes duplicates): $setOfNumbers")

    val set1 = setOf(1, 2, 3)
    val set2 = mutableSetOf(3, 4, 5)
    results.add("set1.intersect(set2): ${set1.intersect(set2)}")
    results.add("set1.union(set2): ${set1.union(set2)}")

    val peopleAges = mutableMapOf("Fred" to 30, "Ann" to 23)
    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51
    results.add("map forEach: ${peopleAges.map { "${it.key}=${it.value}" }.joinToString(", ")}")
    results.add("filter key.length<4: ${peopleAges.filter { it.key.length < 4 }}")

    val words = listOf("about", "acute", "balloon", "best", "brief", "class")
    val filtered = words.filter { it.startsWith("b", ignoreCase = true) }.sorted().take(2)
    results.add("filter+sorted+take(2): $filtered")

    val triple: (Int) -> Int = { a: Int -> a * 3 }
    results.add("lambda triple(5) = ${triple(5)}")

    var _word = "test"
    val word: String get() = _word
    _word = "kotlin"
    results.add("backing property: $word")

    var qty: Int? = null
    results.add("Elvis: null ?: 0 = ${qty ?: 0}")
    qty = 4
    results.add("Elvis: 4 ?: 0 = ${qty ?: 0}")

    return results
}

// ===== Bài 4: Coroutines, Enum, Object, Try/Catch =====
private enum class Direction { NORTH, SOUTH, WEST, EAST }

private object DataProviderManager {
    val data = listOf("item1", "item2", "item3")
}

private fun bai4Results(): List<String> {
    val results = mutableListOf<String>()

    val direction = Direction.NORTH
    val dirResult = when (direction) {
        Direction.NORTH -> "Going North"
        Direction.SOUTH -> "Going South"
        Direction.WEST -> "Going West"
        Direction.EAST -> "Going East"
    }
    results.add("enum Direction: $dirResult")
    results.add("object DataProviderManager.data: ${DataProviderManager.data}")

    val ok = try { "123".toInt(); "Parsed 123 OK" } catch (e: Exception) { "Error" }
    results.add("try/catch success: $ok")
    val err = try { "abc".toInt(); "ok" } catch (e: NumberFormatException) { "Caught: ${e.message}" }
    results.add("try/catch error: $err")

    results.add("suspend fun getValue(): Double { /* long running */ }")
    results.add("GlobalScope.launch { val output = getValue() }")
    results.add("runBlocking { val output = getValue() }")
    results.add("val job: Job = GlobalScope.launch { ... }")
    results.add("job.cancel() // cancel coroutine")
    results.add("async { getValue() }.await() // deferred result")

    return results
}

data class Section(val title: String, val items: List<String>)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab1Theme {
                KotlinBasicsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KotlinBasicsScreen() {
    val sections = remember {
        listOf(
            Section("Bài 1 - Biến, Hàm, Lớp, Điều kiện", bai1Results()),
            Section("Bài 2 - Kế thừa, Danh sách, with()", bai2Results()),
            Section("Bài 3 - Set, Map, Lambda, Elvis Operator", bai3Results()),
            Section("Bài 4 - Enum, Object, Coroutines, Try/Catch", bai4Results())
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lab 1 - Kotlin Basics") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            sections.forEach { section ->
                item {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }
                items(section.items) { resultItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = resultItem,
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                item { HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp)) }
            }
        }
    }
}
