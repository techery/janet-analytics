package io.techery.analytics.sample

import io.techery.janet.analytics.annotation.AttributeMap
import io.techery.janet.analytics.annotation.KeyPath

abstract class SomeVeryBasicViewAction {

    @AttributeMap
    val map: HashMap<String, String> = HashMap()

    @KeyPath("action_path")
    val actionPart: String = "ololo"

    init {
        map.put("valueKey1", "value1")
        map.put("valueKey2", "value2")
    }
}
