package com.blepoc.utility

/**
 *
 * Lazily creates and initializes a Singleton the first time it is invoked.
 *
 * This is a Kotlin implementation of the recommended Android pattern: LocalBroadcastManager.getInstance(context)
 *
 *@sample
 *
 * companion object : SingletonHolder<YourSingleton, Context>({ code... })
 *
 * class YourSingleton private constructor(context: Context) {
 *      companion object : SingletonHolder<YourSingleton, Context>(::YourSingleton)
 *  }
 */
open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}