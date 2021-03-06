package com.idanatz.oneadapter.tests.modules.paging

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.holders.LoadingIndicator
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.external.modules.PagingModuleConfig
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.Metadata
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenReachingThresholdOnBindShouldBeCalledOnce : BaseTest() {

    private var onBindCalls = 0

    @Test
    fun test() {
        configure {
            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule(R.layout.test_model_large))
                    attachPagingModule(TestPagingModule())
                    oneAdapter.internalAdapter.data = modelGenerator.generateModels(10).toMutableList()
                }
            }
            actOnActivity {
                runWithDelay { // run with delay to let the items settle
                    recyclerView.smoothScrollToPosition(oneAdapter.itemCount)
                }
            }
            untilAsserted {
                onBindCalls shouldEqualTo 1
            }
        }
    }

    inner class TestPagingModule : PagingModule() {
        override fun provideModuleConfig(): PagingModuleConfig = modulesGenerator.generateValidPagingModuleConfig()
        override fun onLoadMore(currentPage: Int) {}
        override fun onBind(item: Item<LoadingIndicator>, viewBinder: ViewBinder) {
            onBindCalls++
        }
    }
}