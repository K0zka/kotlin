package kotlin

import kotlin.support.AbstractIterator
import java.util.*

public trait Stream<out T> {
    public fun iterator(): Iterator<T>
}

public fun <T> streamOf(vararg elements: T): Stream<T> = elements.stream()

public class FilteringStream<T>(private val stream: Stream<T>,
                                private val sendWhen: Boolean = true,
                                private val predicate: (T) -> Boolean
                               ) : Stream<T> {

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val iterator = stream.iterator()
        var next: T? = null

        override fun next(): T {
            val result = next as T

            // Clean next to avoid keeping reference on yielded instance
            next = null
            return result
        }

        override fun hasNext(): Boolean {
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (predicate(item) == sendWhen) {
                    next = item
                    return true
                }
            }
            return false
        }
    }
}

public class TransformingStream<T, R>(private val stream: Stream<T>, private val transformer: (T) -> R) : Stream<R> {
    override fun iterator(): Iterator<R> = object : Iterator<R> {
        val iterator = stream.iterator()
        override fun next(): R {
            return transformer(iterator.next())
        }
        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }
    }
}

public class MergingStream<T1, T2, V>(private val stream1: Stream<T1>,
                                      private val stream2: Stream<T2>,
                                      private val transform: (T1, T2) -> V
                                     ) : Stream<V> {
    override fun iterator(): Iterator<V> = object : Iterator<V> {
        val iterator1 = stream1.iterator()
        val iterator2 = stream2.iterator()
        override fun next(): V {
            return transform(iterator1.next(), iterator2.next())
        }
        override fun hasNext(): Boolean {
            return iterator1.hasNext() && iterator2.hasNext()
        }
    }
}

public class FlatteningStream<T, R>(private val stream: Stream<T>,
                                    private val transformer: (T) -> Stream<R>
                                   ) : Stream<R> {
    override fun iterator(): Iterator<R> = object : Iterator<R> {
        val iterator = stream.iterator()
        var itemIterator: Iterator<R>? = null

        override fun next(): R {
            if (!ensureItemIterator())
                throw NoSuchElementException()
            return itemIterator!!.next()
        }

        override fun hasNext(): Boolean {
            return ensureItemIterator()
        }

        private fun ensureItemIterator(): Boolean {
            if (itemIterator?.hasNext() == false)
                itemIterator = null

            while (itemIterator == null) {
                if (!iterator.hasNext()) {
                    return false
                } else {
                    val element = iterator.next()
                    val nextItemIterator = transformer(element).iterator()
                    if (nextItemIterator.hasNext()) {
                        itemIterator = nextItemIterator
                        return true
                    }
                }
            }
            return true
        }
    }
}

public class Multistream<T>(private val stream: Stream<Stream<T>>) : Stream<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val iterator = stream.iterator()
        var itemIterator: Iterator<T>? = null

        override fun next(): T {
            if (!ensureItemIterator())
                throw NoSuchElementException()
            return itemIterator!!.next()
        }

        override fun hasNext(): Boolean {
            return ensureItemIterator()
        }

        private fun ensureItemIterator(): Boolean {
            if (itemIterator?.hasNext() == false)
                itemIterator = null

            while (itemIterator == null) {
                if (!iterator.hasNext()) {
                    return false
                } else {
                    val element = iterator.next()
                    val nextItemIterator = element.iterator()
                    if (nextItemIterator.hasNext()) {
                        itemIterator = nextItemIterator
                        return true
                    }
                }
            }
            return true
        }
    }
}

public class TakeStream<T>(private val stream: Stream<T>,
                           private var count: Int
                          ) : Stream<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val iterator = stream.iterator();

        override fun next(): T {
            if (count == 0)
                throw NoSuchElementException()
            count--
            return iterator.next()
        }

        override fun hasNext(): Boolean {
            return count > 0 && iterator.hasNext()
        }
    }
}

public class TakeWhileStream<T>(private val stream: Stream<T>,
                              private val predicate: (T) -> Boolean
                             ) : Stream<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val iterator = stream.iterator();
        var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
        var nextItem: T? = null

        fun calcNext() {
            if (iterator.hasNext()) {
                val item = iterator.next()
                if (predicate(item)) {
                    nextState = 1
                } else {
                    nextState = 0
                }
                nextItem = item
            } else {
                nextState = 0
            }
        }

        override fun next(): T {
            if (nextState == -1)
                calcNext() // will change hasNext
            if (nextState == 0)
                throw NoSuchElementException()
            val result = nextItem as T

            // Clean next to avoid keeping reference on yielded instance
            nextItem = null
            nextState = -1
            return result
        }

        override fun hasNext(): Boolean {
            if (nextState == -1)
                calcNext() // will change hasNext
            return nextState == 1
        }
    }
}

public class DropStream<T>(private val stream: Stream<T>,
                           private var count: Int
                          ) : Stream<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val iterator = stream.iterator();

        // Shouldn't be called from constructor to avoid premature iteration
        fun drop() {
            while (count > 0 && iterator.hasNext() ) {
                iterator.next()
                count--
            }
        }

        override fun next(): T {
            drop()
            return iterator.next()
        }

        override fun hasNext(): Boolean {
            drop()
            return iterator.hasNext()
        }
    }
}

public class DropWhileStream<T>(private val stream: Stream<T>,
                                private val predicate: (T) -> Boolean
                          ) : Stream<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val iterator = stream.iterator();
        var dropState: Int = -1 // -1 for not dropping, 1 for nextItem, 0 for normal iteration
        var nextItem: T? = null

        fun drop() {
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (!predicate(item)) {
                    nextItem = item
                    dropState = 1
                    return
                }
            }
            dropState = 0
        }

        override fun next(): T {
            if (dropState == -1)
                drop()

            if (dropState == 1) {
                val result = nextItem as T
                nextItem = null
                dropState = 0
                return result
            }
            return iterator.next()
        }

        override fun hasNext(): Boolean {
            if (dropState == -1)
                drop()
            return dropState == 1 || iterator.hasNext()
        }
    }
}

public class FunctionStream<T : Any>(private val producer: () -> T?) : Stream<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
        var nextItem: T? = null

        override fun next(): T {
            if (nextState == -1)
                computeNext()
            if (nextState == 0)
                throw NoSuchElementException()
            val result = nextItem as T
            // Clean next to avoid keeping reference on yielded instance
            nextItem = null
            nextState = -1
            return result
        }

        override fun hasNext(): Boolean {
            if (nextState == -1)
                computeNext()
            return nextState == 1
        }

        fun computeNext() {
            val item = producer()
            if (item == null) {
                nextState = 0
            } else {
                nextState = 1
                nextItem = item
            }
        }
    }
}

/**
 * Returns a stream which invokes the function to calculate the next value on each iteration until the function returns *null*
 */
public fun <T : Any> stream(nextFunction: () -> T?): Stream<T> {
    return FunctionStream(nextFunction)
}

/**
 * Returns a stream which invokes the function to calculate the next value based on the previous one on each iteration
 * until the function returns *null*
 */
public /*inline*/ fun <T : Any> stream(initialValue: T, nextFunction: (T) -> T?): Stream<T> =
        stream(nextFunction.toGenerator(initialValue))

