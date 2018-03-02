## Analytics ActionService

[![Build Status](https://travis-ci.org/techery/janet-analytics.svg?branch=master)](https://travis-ci.org/techery/janet-analytics)

The [Janet](https://github.com/techery/janet) ActionService handles analytics events in a descriptive and command-based way throughout your project.

### Motivation

Usually, the analytics SDK (Adobe, Google's Firebase, Facebook, Amazon, etc.) expects to receive the data in a predefined format, which differs between libraries. It brings about several complexities:

 * ❗️ switching between various analytics service vendors is difficult and error-prone requiring a lot of monotonous work
 * ❗️ using two or more analytics SDKs in one project can give you a headache, as it implies a lot of boilerplate code
 * ❗️ your analytics data source and SDK are likely to have a totally different structure forcing you to place somewhere your code that collects data and organizes it into a suitable format

##### Typical issue

Imagine that you have a user interaction with some data logging:

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

The problem is the `SomeAnalyticsSdk.sendEvent()` method doesn't accept our entity directly. Instead, it requires `String eventName` and `Map<String, String> eventData`. On top of that, `PetEntity` has a number of characteristics stored as `int`s and `String`s and, of cource, a `Date birthDate` field.

You can write some code to reformat our data and feed it to the `sendEvent` method. But!

 * ⚠️ What if the reformatting code grows with time and takes up more and more space in your presenter?
 * ⚠️ What if we have some other presenter to send a similar event?
 * ⚠️ Testing the reformatting logic as part of the presenter might be difficult or impossible for a number of reasons.

##### Solution example

This library helps you overcome these complexities the way shown below. It allows to:

 * ✅ quickly add/remove events for multiple SDKs along with new SDKs
 * ✅ reuse analytics event/data conversion code
 * ✅ extract the analytics data from the business/view logic
 * ✅ isolate the analytics data convertion
 * ✅ cover the analytics data convertion with unit tests

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
The `PetBuyEvent` class knows how to reformat the data from `PetEntity` to something that the analytics SDK expects. It can be tested as a single unit, it can collect some other data to enrich the analytics event (e.g. adds info about the user that bought a pet), and it can be reused from some other presenter as-is.

### Usage

##### 1. Setup tracker

Create the implementation of the `Tracker` interface for the particular analytics SDK:

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

##### 3. Create a class for the analytics event

```java
@AnalyticsEvent(actionKey = "user_bought_pet:$action_key_param", trackerIds = { SomeAnalyticsTracker.ID })
public class BuyPetEvent {

    @KeyPath("action_key_param")
    String petType;

    @Attribute("pet_birth_date")
    String petBirthDate;

    @AttributeMap
    Map<String, Object> data = new HashMap<>();

    public BuyPetEvent(PetEntity pet) {
        petType = pet.petType.name().toLowerCase(Locale.US); // assuming PetType is a enum
        petBirthDate = DateFormat.getDateInstance().format(pet.birthDate);
        data.put("pet_name", pet.name);
    }
}
```

The `@AnalyticsEvent` annotation flags a class as the one to be processed by `AnalyticsService` providing the info:

 * `actionKey` - an event name. Different analytics SDKs all have the param tag of this kind for every event;
 * `trackerIds` - an array of trackers' identifiers where this action should be processed.

Annotations for class fields:

 * `@KeyPath` – use this annotation if you want to format your `actionKey` at runtime
 * `@Attribute` - the annotation `value` and field value will form a key-value pair in the `data` map that the tracker recieves
 * `@AttributeMap` – sometimes, it might be easier to form a map than to create a [big] number of annotated fields. These map contents will be merged with attributes.

For more info please see the `sample` project code.
For more sophisticated janet-usage please see the samples from [Janet repo](https://github.com/techery/janet) and Janet's [CommandActionService](https://github.com/techery/janet-command)

#### Additional features

 * `Kotlin` is supported
 * Testable, refer to [sample tests](sample/src/test/java/io/techery/analytics/sample)

#### Limitations

 * If event classes involve inheritance, only the bottom-most inheritor's instance can be sent to the service.
 * Event classes should not declare annotated fields as `private` - default visibility is applicable.

#### Advanced bits

 * In a multi-module project using the javac option `'-Ajanet.analytics.module.library=true'` is a **must**, while in the main module (e.g. your typical Android project `:app` module) this parameter should not be specified.

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

#### Android addons

 Two `Tracker` implementations for the Adobe and Apptentive analytics vendors are here for you. Just add some of these dependencies to your build.gradle:

 ```groovy
  dependencies {
      ...

      implementation 'com.github.techery:analytics:android-adobe-tracker:xxx'
      implementation 'com.github.techery:analytics:android-apptentive-tracker:xxx'
  }
 ```
 > NOTE: setting up a particular SDK in your application (e.g. providing `API_KEY`s and stuff) is up to you. These trackers only handle interface implementation, push processed event to SDK using its API and perform lifecycle tracking when used for Adobe.

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
