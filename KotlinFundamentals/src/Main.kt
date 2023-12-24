class Song(val title: String, private val artist: String, private val yearPublished: Int, var playCount: Int = 0) {
    var isPopular = false
        get() = playCount >= 1000

    fun play() {
        playCount++
    }

    fun showInfo() {
        println("$title, performed by $artist, was released in $yearPublished")
    }
}

class Person(
    private val name: String,
    private val age: Int,
    private val hobby: String?,
    private val referrer: Person?,
) {
    fun showProfile() {
        println("Name: $name")
        println("Age: $age")
        println("Likes to $hobby. ${if (referrer != null) "Has a referrer named ${referrer.name}, who likes to ${referrer.hobby}." else "Doesn't have a referrer"}")
        println()
    }
}

open class Phone(var isScreenLightOn: Boolean = false) {
    open fun switchOn() {
        isScreenLightOn = true
        println("The phone is switched on.")
    }

    fun switchOff() {
        isScreenLightOn = false
        println("The phone is switched off.")
    }

    fun checkPhoneScreenLight() {
        val phoneScreenLight = if (isScreenLightOn) "on" else "off"
        println("The phone screen's light is $phoneScreenLight.")
    }
}

class FoldablePhone(private var isFolded: Boolean = false) : Phone() {

    override fun switchOn() {
        if (!isFolded) {
            super.switchOn()
            // isScreenLightOn = true //alternative to super.switchOn()
        }
        println("The phone is folded, the screen's light cannot be turned on.")
    }

    fun fold() {
        super.switchOff()
        isFolded = true
        println("The phone is folded.")
    }

    fun unfold() {
        super.switchOn()
        isFolded = false
        println("The phone is unfolded.")
    }
}

class Bid(val amount: Int, val bidder: String)


fun main() {
    // 1. Mobile notifications
    val morningNotification = 51
    val eveningNotification = 135

    printNotificationSummary(morningNotification)
    printNotificationSummary(eveningNotification)

    repeat(1) { println() }

    //2. Movie-ticket Price
    val child = 5
    val adult = 28
    val senior = 87

    val isMonday = true

    println("The movie ticket price for a person aged $child is \$${ticketPrice(child, isMonday)}.")
    println("The movie ticket price for a person aged $adult is \$${ticketPrice(adult, isMonday)}.")
    println("The movie ticket price for a person aged $senior is \$${ticketPrice(senior, isMonday)}.")

    repeat(1) { println() }

    //3. Temperature Converter
    printFinalTemperature(27.0, "Celcius", "Fahrenheit") { 9.0 / 5.0 * it + 32.0 }
    printFinalTemperature(350.0, "Kelvin", "Celcius") { it - 273.15 }
    printFinalTemperature(10.0, "Fahrenheit", "Kelvin") { 5.0 / 9.0 * (it - 32) + 273.15 }

    repeat(1) { println() }

    //4. Song catalog
    val song = Song("The Mesopotamians", "They Might Be Giants", 2007)
    song.play()
    song.play()
    song.play()
    song.play()
    song.showInfo()
    println("The song ${song.title} has been played ${song.playCount} times.")
    println(if (song.isPopular) "The song is popular." else "The song is not popular.")
    repeat(1200) { song.play() }
    println("The song ${song.title} has been played ${song.playCount} times.")
    println(if (song.isPopular) "The song is popular." else "The song is not popular.")

    repeat(1) { println() }

    //5. Internet profile
    val amanda = Person("Amanda", 33, "play tennis", null)
    val atiqah = Person("Atiqah", 28, "climb", amanda)

    amanda.showProfile()
    atiqah.showProfile()

    repeat(1) { println() }

    //6. Foldable Phones
    val samsung = FoldablePhone(false)
    val huawei = FoldablePhone(false)

    samsung.switchOn()
    samsung.checkPhoneScreenLight()
    samsung.fold()
    samsung.checkPhoneScreenLight()
    samsung.switchOn()
    samsung.unfold()
    samsung.checkPhoneScreenLight()
    samsung.switchOff()
    samsung.checkPhoneScreenLight()
    println()
    huawei.switchOn()
    huawei.checkPhoneScreenLight()
    huawei.fold()
    huawei.checkPhoneScreenLight()
    huawei.switchOn()
    huawei.unfold()
    huawei.checkPhoneScreenLight()
    huawei.switchOff()
    huawei.checkPhoneScreenLight()

    repeat(1) { println() }

    //7. Special Auction
    val winningBid = Bid(5000, "Private Collector")

    println("Item A is sold at ${auctionPrice(winningBid, 2000)}.")
    println("Item B is sold at ${auctionPrice(null, 3000)}.")

}

// print number of notifications
fun printNotificationSummary(numberOfMessages: Int) {
    when {
        numberOfMessages < 100 -> println("You have 51 notifications.")
        else -> println("Your phone is blowing up! You have 99+ notifications.")
    }
}

fun ticketPrice(age: Int, isMonday: Boolean): Int {
    return when (age) {
        in 0..12 -> 15
        in 13..60 -> if (isMonday) 25 else 30
        in 61..100 -> 20
        else -> -1
    }
}

fun printFinalTemperature(
    initialMeasurement: Double,
    initialUnit: String,
    finalUnit: String,
    conversionFormula: (Double) -> Double,
) {
    val finalMeasurement = String.format("%.2f", conversionFormula(initialMeasurement)) // two decimal places
    println("$initialMeasurement degrees $initialUnit is $finalMeasurement degrees $finalUnit.")
}

fun auctionPrice(bid: Bid?, minimumPrice: Int): Int {
    return bid?.amount ?: minimumPrice
}
