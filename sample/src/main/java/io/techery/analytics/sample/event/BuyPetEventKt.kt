package io.techery.analytics.sample.event

import io.techery.analytics.sample.SomeAnalyticsTracker
import io.techery.analytics.sample_common.entity.PetEntity
import io.techery.analytics.sample_common.janet.action.BaseAnalyticsAction
import io.techery.janet.analytics.annotation.AnalyticsEvent
import io.techery.janet.analytics.annotation.Attribute
import io.techery.janet.analytics.annotation.AttributeMap
import io.techery.janet.analytics.annotation.KeyPath
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

@AnalyticsEvent(actionKey = "user_bought_pet:\$pet_type:\$store_type",
        trackerIds = [(SomeAnalyticsTracker.ID)])
class BuyPetEventKt(petEntity: PetEntity) : BaseAnalyticsAction { // kotlin classes supported

    @KeyPath("pet_type")
    val petType: String = petEntity.petType.name.toLowerCase(Locale.US)

    @KeyPath("store_type")
    val storeType: String = "mall"

    @Attribute("pet_birth_date")
    val petBirthDate: String = DateFormat.getDateInstance().format(petEntity.birthDate.time)

    @AttributeMap
    val data: HashMap<String, Any> = HashMap()

    init {
        data["pet_name"] = petEntity.name
    }
}
