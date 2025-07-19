package hu.piware.bricklog.baselineprofile.util

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

fun MacrobenchmarkScope.waitUntilScreenLoads(screenName: String, timeout: Long = 1000) {
    println("Waiting for screen: $screenName")
    device.wait(Until.hasObject(By.res(screenName)), timeout)
}

fun MacrobenchmarkScope.waitUntilObject(resourceName: String, timeout: Long = 1000): UiObject2 {
    println("Waiting for object: $resourceName")
    device.wait(Until.hasObject(By.res(resourceName)), timeout)
    return device.findObject(By.res(resourceName)) ?: error("Object not found: $resourceName")
}

fun UiObject2.waitUntilObject(resourceName: String, timeout: Long = 1000): UiObject2 {
    println("Waiting for object: $resourceName")
    wait(Until.hasObject(By.res(resourceName)), timeout)
    return findObject(By.res(resourceName)) ?: error("Object not found: $resourceName")
}

fun MacrobenchmarkScope.waitUntilGone(resourceName: String, timeout: Long = 1000) {
    println("Waiting for gone: $resourceName")
    device.wait(Until.gone(By.res(resourceName)), timeout)
}

fun MacrobenchmarkScope.waitUntilObjects(
    resourceName: String,
    timeout: Long = 1000,
): List<UiObject2> {
    device.wait(Until.hasObject(By.res(resourceName)), timeout)
    return device.findObjects(By.res(resourceName))
}
