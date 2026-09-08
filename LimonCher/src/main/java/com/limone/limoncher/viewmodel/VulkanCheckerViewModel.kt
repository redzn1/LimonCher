/*
 * LimonCher
 * Copyright (C) 2025 MovTery <movtery228@qq.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.limone.limoncher.viewmodel

import androidx.lifecycle.ViewModel
import com.limone.limoncher.game.plugin.driver.Driver
import com.limone.limoncher.game.plugin.driver.DriverPluginManager
import com.limone.limoncher.game.version.installed.Version
import com.limone.limoncher.path.PathManager
import com.limone.limoncher.setting.launcherMMKV
import com.limone.limoncher.ui.vulkan_checker.VCOperation
import com.limone.limoncher.utils.GSON
import com.limone.limoncher.utils.device.VulkanCapabilities
import com.limone.limoncher.utils.device.VulkanChecker
import com.limone.limoncher.utils.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

private const val TAG = "VulkanCheckerViewModel"
private const val KEY_VULKAN_CHECK_RECORD = "vulkanCheckRecord"

data class VulkanCheckRecord(
    val allSupported: Boolean,
    val useTurnip: Boolean,
    val driverPath: String
)

class VulkanCheckerViewModel: ViewModel() {
    private val _vcOperation = MutableStateFlow<VCOperation>(VCOperation.None)
    val vcOperation = _vcOperation.asStateFlow()

    private val mutex = Mutex()

    var vulkanCheckerCont: (Continuation<Unit>)? = null
        private set

    fun changeOperation(operation: VCOperation) {
        _vcOperation.update { operation }
    }

    suspend fun waitForVulkanChecker(version: Version) {
        suspendCancellableCoroutine { cont ->
            vulkanCheckerCont = cont
            changeOperation(VCOperation.Tip(version))

            cont.invokeOnCancellation {
                vulkanCheckerCont = null
            }
        }
    }

    fun resumeCont() {
        vulkanCheckerCont?.resume(Unit)
        vulkanCheckerCont = null
    }

    suspend fun check(version: Version): Pair<VulkanCapabilities?, Boolean> {
        return mutex.withLock {
            val driver = DriverPluginManager.getDriver(version.getDriver())
            val useTurnip = !driver.isLauncher
            val capabilities = doCheck(useTurnip, driver)
            saveRecord(
                VulkanCheckRecord(
                    allSupported = capabilities?.isAllSupported == true,
                    useTurnip = useTurnip,
                    driverPath = driverPath(useTurnip, driver)
                )
            )
            capabilities to useTurnip
        }
    }

    suspend fun ensureSupported(version: Version): Boolean {
        val driver = DriverPluginManager.getDriver(version.getDriver())
        val useTurnip = !driver.isLauncher
        val path = driverPath(useTurnip, driver)

        loadRecord()?.takeIf { last ->
            last.useTurnip == useTurnip && last.driverPath == path
        }?.let {
            return it.allSupported
        }

        //记录不存在或状态不一致时走完整的检测流程，检测完成后结果已保存
        waitForVulkanChecker(version)
        loadRecord()?.let { return it.allSupported }
        return false
    }

    private fun driverPath(useTurnip: Boolean, driver: Driver): String {
        return if (useTurnip) driver.path else ""
    }

    private suspend fun doCheck(useTurnip: Boolean, driver: Driver): VulkanCapabilities? {
        return withContext(Dispatchers.IO) {
            if (useTurnip) {
                val tempDir = File(PathManager.DIR_CACHE, "vulkan_temp")
                VulkanChecker.checkCapabilities(null, driver.path, tempDir.absolutePath)
            } else {
                VulkanChecker.checkCapabilities(null, null, null)
            }
        }
    }

    private fun loadRecord(): VulkanCheckRecord? {
        val json = launcherMMKV().getString(KEY_VULKAN_CHECK_RECORD, null) ?: return null
        return runCatching {
            GSON.fromJson(json, VulkanCheckRecord::class.java)
        }.onFailure { e ->
            Logger.warning(TAG, "Failed to read vulkan check record", e)
        }.getOrNull()
    }

    private fun saveRecord(record: VulkanCheckRecord) {
        runCatching {
            launcherMMKV()
                .putString(KEY_VULKAN_CHECK_RECORD, GSON.toJson(record))
                .apply()
        }.onFailure { e ->
            Logger.warning(TAG, "Failed to save vulkan check record", e)
        }
    }
}