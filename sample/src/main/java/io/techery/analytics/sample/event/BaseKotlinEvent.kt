package io.techery.analytics.sample.event

import io.techery.analytics.sample_common.janet.action.BaseAnalyticsAction
import io.techery.janet.analytics.annotation.Attribute
import io.techery.janet.analytics.annotation.KeyPath

// inheritance is supported - you can re-use event-filling logic from superclass as you need
open class BaseKotlinEvent(
      @Attribute("attribute_from_parent") val attributeFromParent: String) : BaseAnalyticsAction {

   @KeyPath("superclass_name")
   val actionKeyParam: String = this.javaClass.simpleName
}
