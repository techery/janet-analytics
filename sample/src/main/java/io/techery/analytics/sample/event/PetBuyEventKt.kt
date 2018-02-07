package io.techery.analytics.sample.event

import io.techery.analytics.sample.MyAnalyticsSdkTracker
import io.techery.analytics.sample_common.entity.PetEntity
import io.techery.analytics.sample_common.janet.action.BaseAnalyticsAction
import io.techery.janet.analytics.annotation.AnalyticsEvent
import io.techery.janet.analytics.annotation.Attribute
import io.techery.janet.analytics.annotation.AttributeMap
import io.techery.janet.analytics.annotation.KeyPath
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

@AnalyticsEvent(actionKey = "user_bought_pet\$pet_type",
        trackerIds = arrayOf(MyAnalyticsSdkTracker.MYANALYTICSSDK_TRACKER_KEY))
class PetBuyEventKt(petEntity: PetEntity) : BaseAnalyticsAction {

    @KeyPath("pet_type")
    val petType: String = petEntity.petType.name.toLowerCase(Locale.US)

    @Attribute("pet_birth_date")
    val petBirthDate: String = DateFormat.getDateInstance().format(petEntity.birthDate)

    @AttributeMap
    val data: HashMap<String, Any> = HashMap()

    init {
        data.put("pet_name", petEntity.name)
    }
}
