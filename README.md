## Analytics ActionService

[Janet](https://github.com/techery/janet) ActionService to handle analytics events throughout your project — in a descriptive, command-based way.

### Motivation

Usually analytics SDK (Adobe, Google's Firebase, Facebook, Amazon, etc.) expects data in a predefined format, which differs between libraries. It brings several complexities:

 * switching between various analytics service vendors is difficult, error-prone and a lot of mechanical work;
 * using two or more analytics SDK's in one project is a headache as it requires a lot of boilerplate code;
 * your analytics data source and SDK are likely to have totally different structure forcing you to place somewhere your code that collects data and organizes it into suitable format.

##### Typical issue

Imagine you have a user interaction, where you want to log some data:

```java
public class PetBuyPresenter {

	@Inject SomeAnalyticsSdk analyticsSdk;
	...
	public void onPetBuyButtonClick(PetEntity pet) {
		...
		analyticsSdk.sendEvent(pet); // <-- problem
	}
}
```

The problem is `SomeAnalyticsSdk.sendEvent()` method doesn't accept our entity directly. Instead, it wants a `String eventName` and a `Map<String, String> eventData`. On the other side - `PetEntity` has a number of characteristics, stored as `int`'s and `String`'s and, of cource, a `Date birthDate` field.

You can write some code to re-format our data and feed it to `sendEvent` method. But!

 * ⚠️ What if re-formatting code grows with time and takes up more and more space in your presenter?
 * ⚠️ What if we have some other presenter, where we also have to send similar event?
 * ⚠️ Testing re-formatting logic as a part of presenter might be difficult or impossible by number of reasons.

##### Solution Example

This library deals with all of these complexities in a way that's shown below:

 * ✅ quickly add/remove events for multiple SDKs along with new SDKs;
 * ✅ extract analytics data from business/view logic;
 * ✅ isolate analytics data convertion.

```java
public class PetBuyPresenter {

	@Inject Janet janet;    
    
	public void onPetBuyButtonClick(PetEntity pet) {
		...
	        janet.createPipe(PetBuyEvent.class)
                    .send(new PetBuyEvent(pet)); // no problem here
    	}
}
```
`PetBuyEvent` class knows how to reformat data from `PetEntity` to something that analytics SDK expects, it can be tested as a single unit, it can collect some other data to enrich analytics event (e.g. add info about user, that bought a pet), it can be re-used from some other presenter as-is.

### Usage

##### 1. Setup tracker

Create implementation of `Tracker` interface for particular analytics SDK:

```java
public class SomeAnalyticsTracker implements Tracker {

    public static final String ID = "SomeAnalyticsSdkTrackerId";
    private final SomeAnalyticsSdk sdk;

    public SomeAnalyticsTracker() {
        sdk = new SomeAnalyticsSdk();
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public void trackEvent(String actionKey, Map<String, Object> data) {
        sdk.sendEvent(actionKey, prepareData(data));
    }

    private Map<String, String> prepareData(Map<String, Object> data) {
        // final transformation - this will be special for any Tracker implementation
        // while Map<String, Object> is a more general contract.
        // E.g. you can put Integer or Boolean as a map item value
    }
}
```

##### 2. Setup Janet

```java
Collection<Tracker> trackers = Arrays.asList(new SomeAnalyticsTracker());
ActionService actionService = new AnalyticsService(trackers);
Janet janet = new Janet.Builder().addService(actionService).build();
```

##### 3. Create class for analytic event

```java
@AnalyticsEvent(actionKey = "user_bought_pet:$action_key_param", trackerIds = { SomeAnalyticsTracker.ID })
public class BuyPetEvent {

    @KeyPath("action_key_param")
    String petType;

    @Attribute("pet_birth_date")
    String petBirthDate;

    @AttributeMap
    Map<String, Object> data = new HashMap<>();

    public BuyPetEvent(PetEntity petEntity) {
        petType = petEntity.petType.name().toLowerCase(Locale.US);
        petBirthDate = DateFormat.getDateInstance().format(petEntity.birthDate);
        data.put("pet_name", petEntity.name);
    }
}
```

`@AnalyticsEvent` annotation - flags class as the one to be processed by `AnalyticsService` with info:

 * `actionKey` - name of event. Different analytics SDKs are similar to have param of this kind for every event;
 * `trackerIds` - array of trackers' identifiers, where this action should be processed.

Annotations for class fields:

 * `@KeyPath` – use this annotation if you want to format your `actionKey` at runtime;
 * `@Attribute` - annotation `value` and field value will form a key-value pair in `data` map that tracker recieves;
 * `@AttributeMap` – sometimes it might be easier to form a map than to create a [big] number of annotated fields. This map contents will be merged with attributes.

For more info please see `sample` project code.
For more sophisticated janet-usage - please see samples from [Janet repo](https://github.com/techery/janet) and Janet's [CommandActionService](https://github.com/techery/janet-command)

#### Additional Features

 * `Kotlin` is supported.
 * Testable, refer to [sample tests](sample/src/test/java/io/techery/analytics/sample).

#### Limitations

 * If event classes involve inheritance - only bottom-most inheritor's instance can be sent to service.
 * Event classes should not declare annotated fields as `private` - default visibility is applicable.

#### Advanced bits

 * In multi-module project using javac option `'-Ajanet.analytics.module.library=true'` is a **must**, while in main module (e.g. your typical Android-project `:app` module) - this parameter should not be specified.

### Download

Grab via Gradle:

```groovy
  repositories {
      maven { url "https://jitpack.io" }
  }
  dependencies {
      implementation 'com.github.techery.janet-analytics:library:xxx'
      apt 'com.github.techery.janet-analytics:service-compiler:xxx'
      // use kapt if your project has analytics events written in Kotlin
      
      // also use explicit latest version Janet dependency to incorporate new features and bugfixes
      implementation 'com.github.techery:janet:zzz'
  }
```

 * janet-analytics: [![](https://jitpack.io/v/techery/janet-analytics.svg)](https://jitpack.io/#techery/janet-analytics)
 * janet: [![](https://jitpack.io/v/janet-io/janet.svg)](https://jitpack.io/#janet-io/janet)

## License

    Copyright (c) 2018 Techery

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
