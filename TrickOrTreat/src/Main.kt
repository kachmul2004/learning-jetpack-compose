/**
 * Summary
 * Functions in Kotlin are first-class constructs and can be treated like data types.
 * Lambda expressions provide a shorthand syntax to write functions.
 * You can pass function types into other functions.
 * You can return a function type from another function.
 * A lambda expression returns the value of the last expression.
 * If a parameter label is omitted in a lambda expression with a single parameter, it's referred to with the it identifier.
 * Lambdas can be written inline without a variable name.
 * If a function's last parameter is a function type, you can use trailing lambda syntax to move the lambda expression after the last parenthesis when you call a function.
 * Higher-order functions are functions that take other functions as parameters or return a function.
 * The repeat() function is a higher-order function that works similarly to a for loop.
 */

//fun main() {
//    val trickFunction = ::trick // :: is the reflection operator. In this case, it's used to get a reference to the trick function.
//}
//
//fun trick() {
//    println("No treats!")
//}

fun main() {

    val cupcake: (Int) -> String = {
        "Have a cupcake!"
    }

    val treatFunction = trickOrTreat(false) { "$it quarters" }
    val trickFunction = trickOrTreat(true, null)
    repeat(4) {
        treatFunction()
    }
    trickFunction()
}


fun trickOrTreat(isTrick: Boolean, extraTreat: ((Int) -> String)?): () -> Unit {
    if (isTrick) {
        println(extraTreat?.invoke(5)) // safe call for nullable extraTreat
        return trick
    } else {
        if (extraTreat != null) {
            println(extraTreat(5))
        }
        return treat
    }
}

val trick = {
    println("No treats!")
}

val treat: () -> Unit = {
    println("Have a treat!")
}