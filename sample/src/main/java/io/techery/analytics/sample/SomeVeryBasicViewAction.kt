package io.techery.analytics.sample

import io.techery.janet.analytics.annotation.ActionPart
import io.techery.janet.analytics.annotation.AttributeMap

abstract class SomeVeryBasicViewAction {

    @AttributeMap
    val map: HashMap<String, String> = HashMap()

    @ActionPart
    val actionPart: String = "ololo"

    init {
        map.put("valueKey1", "value1")
        map.put("valueKey2", "value2")
    }
}
