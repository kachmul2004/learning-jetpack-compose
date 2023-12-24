/**
 * - There are four main principles of OOP: encapsulation, abstraction, inheritance, and polymorphism.
 * - Classes are defined with the class keyword, and contain properties and methods.
 * - Properties are similar to variables except properties can have custom getters and setters.
 * - A constructor specifies how to instantiate objects of a class.
 * - You can omit the constructor keyword when you define a primary constructor.
 * - Inheritance makes it easier to reuse code.
 * - The IS-A relationship refers to inheritance.
 * - The HAS-A relationship refers to composition.
 * - Visibility modifiers play an important role in the achievement of encapsulation.
 * - Kotlin provides four visibility modifiers: the public, private, protected, and internal modifiers.
 * - A property delegate lets you reuse the getter and setter code in multiple classes.
 */

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// open modifier is used to make the class inheritable
private open class SmartDevice(val name: String, val category: String) {

    var deviceStatus = "online"
        protected set  // parentheses removed because we are using the default implementation

    open val deviceType = "unknown"


    // protected modifier is used to make the method accessible only inside the class and its subclasses.
    // In the main function, we can't call this method on the SmartDevice object.
    open fun turnOn() {
        deviceStatus = "on"
    }

    open fun turnOff() {
        deviceStatus = "off"
    }

    fun printDeviceInfo() {
        println("Device name: $name, category: $category, type: $deviceType")
    }
}

private class SmartLightDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart Light" // Overriding the deviceType property

    private var brightnessLevel by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 10)

    fun increaseBrightness() {
        brightnessLevel++
        println("Brightness increased to $brightnessLevel.")
    }

    fun decreaseBrightness() {
        brightnessLevel--
        println("Brightness decreased to $brightnessLevel.")
    }

    // Overriding the turnOn method
    // This method will be called when we call the turnOn method on SmartLightDevice object
    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }

    // Overriding the turnOff method
    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("Smart Light turned off")
    }
}

// private modifier is used to make the class accessible only inside the file
private class SmartTvDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {
    override val deviceType = "Smart TV"

    // private modifier is used to make the property accessible only inside the class
    private var speakerVolume by RangeRegulator(
        initialValue = 2,
        minValue = 0,
        maxValue = 100
    ) // example of delegated property

    // The above line is equivalent to the following code
//    private var speakerVolume = 2
//        set(value) {
//            if (value in 0..100) {
//                field = value
//            }
//        }

    private var channelNumber by RangeRegulator(initialValue = 1, minValue = 1, maxValue = 100)

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased to $speakerVolume.")
    }

    fun decreaseSpeakerVolume() {
        speakerVolume--
        println("Speaker volume decreased to $speakerVolume.")
    }

    fun nextChannel() {
        channelNumber++
        println("Channel number increased to $channelNumber.")
    }

    fun previousChannel() {
        channelNumber--
        println("Channel number decreased to $channelNumber.")
    }

    override fun turnOn() {
        super.turnOn() // Calling the parent class method
        println(
            "$name is turned on. Speaker volume is set to $speakerVolume and channel number is " +
                    "set to $channelNumber."
        )
    }

    override fun turnOff() {
        super.turnOff() // Calling the parent class method
        println("$name turned off")
    }
}

private class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice,
) {

    var deviceTurnOnCount = 0
        private set

    fun turnOnTv() {
        deviceTurnOnCount++
        smartTvDevice.turnOn()
    }

    fun turnOffTv() {
        if (smartTvDevice.deviceStatus == "on") {
            deviceTurnOnCount--
            smartTvDevice.turnOff()
        }
    }

    fun turnOnLight() {
        deviceTurnOnCount++
        smartLightDevice.turnOn()
    }

    fun turnOffLight() {
        if (smartLightDevice.deviceStatus == "on") {
            deviceTurnOnCount--
            smartLightDevice.turnOff()
        }
    }

    fun decreaseTvVolume() {
        smartTvDevice.decreaseSpeakerVolume()
    }

    fun changeTvChannelToPrevious() {
        smartTvDevice.previousChannel()
    }

    fun printSmartTvInfo() {
        smartTvDevice.printDeviceInfo()
    }

    fun printSmartLightInfo() {
        smartLightDevice.printDeviceInfo()
    }

    fun decreaseLightBrightness() {
        smartLightDevice.decreaseBrightness()
    }

}

class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int,
) : ReadWriteProperty<Any?, Int> {

    var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }

}

fun main() {
    val smartLight = SmartLightDevice("Phillips Bulb", "Light")
    smartLight.increaseBrightness()

    val smartDevice: SmartDevice = SmartTvDevice("TV", "Electronics")
    smartLight.printDeviceInfo()

    val smartHome = SmartHome(
        smartTvDevice = smartDevice as SmartTvDevice, // Casting the smartDevice to SmartTvDevice
        smartLightDevice = smartLight
    )
    println()
    println()
    smartHome.turnOnTv()
    smartHome.turnOnLight()
    smartHome.decreaseTvVolume()
    smartHome.changeTvChannelToPrevious()
    smartHome.printSmartTvInfo()
    smartHome.printSmartLightInfo()
    smartHome.decreaseLightBrightness()

    println("Number of devices turned on: ${smartHome.deviceTurnOnCount}")
}