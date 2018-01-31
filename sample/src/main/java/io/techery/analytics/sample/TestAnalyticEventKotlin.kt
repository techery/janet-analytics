package io.techery.analytics.sample

import io.techery.janet.analytics.annotation.AnalyticsEvent
import io.techery.janet.analytics.annotation.Attribute
import io.techery.janet.analytics.annotation.AttributeMap
import io.techery.janet.analytics.annotation.KeyPath

@AnalyticsEvent(actionKey = "kotlin-blah-blah:\$action_path", trackerIds = arrayOf("ClientProjectUsedTracker"))
class TestAnalyticEventKotlin (
        @field:Attribute("foo") val bar: String,
        val1: String,
        val2: String) : TestAnalyticEventBaseKotlin() {

    @field:KeyPath("action_path")
    val actionPart: String = val1 + val2

    @field:AttributeMap val dataMape: HashMap<String, Any> = HashMap()

    init {
        dataMape.put("one", "oneValue")
        dataMape.put("two", "twoValue")
    }
}
