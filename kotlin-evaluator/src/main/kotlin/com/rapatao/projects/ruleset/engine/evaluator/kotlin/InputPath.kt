package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Resolves an operand path such as `item.tags[0]` against the input object, walking only the nodes the path names.
 *
 * This replaced a flat map of every path in the input, built once per evaluation, so the rules about what exists are
 * inherited from it: a map entry exists when the map holds the key or a key whose `toString` matches it, an object
 * property exists when Kotlin reflection reports it, an index exists when the node is a collection or an array and
 * the position is in range, and nothing exists below a value, a null, a collection or an array.
 */
internal object InputPath {

    /** Returned when a step of the path does not exist, which is distinct from a step resolving to `null`. */
    val ABSENT = Any()

    // ponytail: unbounded and keyed by Class, so it pins classloaders in a redeploy container.
    // Swap for a weak-keyed cache if that ever matters.
    private val PROPERTIES = ConcurrentHashMap<Class<*>, Map<String, KProperty1<Any, *>>>()

    @Suppress("ReturnCount")
    fun resolve(root: Any, path: String): Any? {
        if (path.isEmpty()) {
            return if (root.isValue() || root is Collection<*> || root is Array<*>) root else ABSENT
        }

        var current: Any? = root
        var index = 0

        while (index < path.length && current !== ABSENT) {
            when (path[index]) {
                '.' -> index++

                '[' -> {
                    val close = path.indexOf(']', index + 1)
                    if (close < 0) return ABSENT

                    current = current.atIndex(path.substring(index + 1, close))
                    index = close + 1
                }

                else -> {
                    val end = path.nameEnd(index)

                    current = current.member(path.substring(index, end))
                    index = end
                }
            }
        }

        return current
    }

    private fun String.nameEnd(from: Int): Int {
        var end = from

        while (end < this.length && this[end] != '.' && this[end] != '[') {
            end++
        }

        return end
    }

    private fun Any?.member(name: String): Any? = when {
        this == null || this.isValue() -> ABSENT
        this is Map<*, *> -> lookup(name)
        this is Collection<*> || this is Array<*> -> ABSENT
        else -> {
            val property = propertiesOf(this.javaClass)[name]
            if (property == null) ABSENT else property.get(this)
        }
    }

    private fun Map<*, *>.lookup(name: String): Any? {
        if (this.containsKey(name)) {
            return this[name]
        }

        // The flat map keyed entries by key.toString(), so keys that are not strings stay reachable.
        val entry = this.entries.firstOrNull { it.key.toString() == name }

        return if (entry == null) ABSENT else entry.value
    }

    private fun Any?.atIndex(text: String): Any? {
        val position = text.toIntOrNull() ?: return ABSENT

        return when {
            // Collection, not List: the flat map indexed by iteration order.
            this is Collection<*> -> if (position >= 0 && position < this.size) this.elementAt(position) else ABSENT
            this is Array<*> -> if (position in this.indices) this[position] else ABSENT
            else -> ABSENT
        }
    }

    private fun Any?.isValue(): Boolean =
        this == null ||
            this.javaClass.isPrimitive ||
            this is Boolean ||
            this is String ||
            this is Number

    @Suppress("UNCHECKED_CAST")
    private fun propertiesOf(type: Class<*>): Map<String, KProperty1<Any, *>> =
        PROPERTIES.computeIfAbsent(type) {
            (it.kotlin.memberProperties as Collection<KProperty1<Any, *>>).associateBy { property -> property.name }
        }
}
