package io.techery.analytics.sample

import io.techery.janet.analytics.annotation.ActionPart
import io.techery.janet.analytics.annotation.ActionPart.ACTION_PATH_PARAM
import io.techery.janet.analytics.annotation.AnalyticsEvent
import io.techery.janet.analytics.annotation.Attribute
import io.techery.janet.analytics.annotation.AttributeMap

@AnalyticsEvent(actionKey = "kotlin-blah-blah:$ACTION_PATH_PARAM", trackerIds = arrayOf("ClientProjectUsedTracker"))
class TestAnalyticEventKotlin (
        @field:Attribute("foo") val bar: String,
        val1: String,
        val2: String) : TestAnalyticEventBaseKotlin() {

    @field:ActionPart val actionPart: String = val1 + val2

    @field:AttributeMap val dataMape: HashMap<String, Any> = HashMap()

    init {
        dataMape.put("one", "oneValue")
        dataMape.put("two", "twoValue")
    }
}
