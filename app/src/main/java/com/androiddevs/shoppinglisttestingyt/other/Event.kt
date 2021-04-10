package com.androiddevs.shoppinglisttestingyt.other

//class is built for single lifetime events
//they will persist through recreation of activities, like rotation
//without this snackbars would emit on each recreation
open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
    private set //Allow external read but not write

    //return content and prevents its use again
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    //returns content even if already handled
    fun peekContent(): T = content
}