package com.rapatao.projects.ruleset.engine.evaluator.kotlin

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

/**
 * Top level on purpose: a nested class loaded apart from its outer class makes kotlin-reflect pin the isolated loader,
 * a split that a real redeploy never produces.
 */
data class Unloadable(val value: String)

class InputPathClassUnloadTest {

    /** Defines [className] itself instead of delegating, so the class belongs to this loader alone. */
    private class IsolatingClassLoader(parent: ClassLoader, private val className: String) : ClassLoader(parent) {
        override fun loadClass(name: String, resolve: Boolean): Class<*> {
            if (name != className) {
                return super.loadClass(name, resolve)
            }

            return synchronized(getClassLoadingLock(name)) {
                findLoadedClass(name) ?: run {
                    val bytes = parent.getResourceAsStream(name.replace('.', '/') + ".class")!!.use { it.readBytes() }
                    defineClass(name, bytes, 0, bytes.size)
                }
            }
        }
    }

    @Test
    @DisplayName("caching the properties of an input class does not keep its classloader alive")
    fun assertClassLoaderIsCollected() {
        val loader = resolveThroughIsolatedLoader()

        repeat(GC_ATTEMPTS) {
            if (loader.get() != null) {
                System.gc()
                Thread.sleep(GC_PAUSE_MILLIS)
            }
        }

        assertThat(loader.get(), nullValue())
    }

    private fun resolveThroughIsolatedLoader(): WeakReference<ClassLoader> {
        val loader = IsolatingClassLoader(javaClass.classLoader, Unloadable::class.java.name)
        val type = loader.loadClass(Unloadable::class.java.name)
        val input = type.getConstructor(String::class.java).newInstance("x")

        assertThat(type.classLoader, equalTo(loader))
        assertThat(InputPath.resolve(input, "value"), equalTo("x"))

        return WeakReference(loader)
    }

    private companion object {
        const val GC_ATTEMPTS = 50
        const val GC_PAUSE_MILLIS = 20L
    }
}
