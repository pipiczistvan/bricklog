package hu.piware.bricklog.util

inline fun <K, V> MutableMap<K, V>.getOrPutNullable(key: K, defaultValue: () -> V): V? {
    if (containsKey(key)) {
        return this[key]
    }

    this[key] = defaultValue()
    return this[key]
}
