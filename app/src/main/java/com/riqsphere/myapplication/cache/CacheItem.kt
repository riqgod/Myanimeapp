package com.riqsphere.myapplication.cache

class CacheItem {
    private var itemHash: Long = 0
    var storedItem: Any = noData
    var lastUsed: Long = 0

    fun hashEquals(otherHash: Long) = itemHash == otherHash

    inline fun <reified T> attemptToCastStoredItem(): T? {
        return if (storedItem is T) {
            this.lastUsed = System.currentTimeMillis()
            storedItem as T
        } else {
            invalidate()
            null
        }
    }

    fun invalidate() {
        itemHash = 0
        storedItem = noData
        lastUsed = 0
    }

    fun newData(hashCode: Long, item: Any) {
        itemHash = hashCode
        storedItem = item
        lastUsed = System.currentTimeMillis()
    }

    companion object {
        val noData = Any()
    }
}