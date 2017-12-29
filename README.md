## Analytics ActionService

Janet ActionService to handle analytics events throughout your project - in a descriptive, command-based way

### Main usage scenario

Usually analytics SDK (Adobe, Google's Firebase, Facebook, Amazon etc) expects data in a predefined format, which differs between different libraries. This brings several complexities, such as:

 * switching between various analytic service vendors is difficult, error-prone and a lot of mechanical work.
 * using two or more analytic SDK's in one project is a headache as it requires a lot of boilerplate code
 * your analytics data source and SDK are likely to have totally different structure forcing you to place somewhere your code that collects data and organizes it into format, suitable for analytics SDK

This library deals with all of these complexities in a way that's shown below.

### Typical usage scenario

Imagine you have a user interaction, where you want to log some data.

```java
public class PetBuyPresenter extends BasePresenter {

	@Inject MyAnalyticsSdk myAnalyticsSdk;
    
	// some other code here
    
	public void onPetBuyButtonClick(PetEntity petEntity) {
    	// execute some business-related logic
        myAnalyticsSdk.sendEvent(petEntity); // problem here
    }
```

Of course we have a problem - `MyAnalyticsSdk.sendEvent()` method is very unlikely to be able to accept given entity. Instead, it wants a `String eventName` and a `Map<String, String> eventData`. On the other side - `PetEntity` has a number of characteristics, stored as `int`'s and `String`'s and, of cource, a `Date birthDate` field.

It is no problem to write some code to re-format our data and feed it to `sendEvent` method.

But:

 * what if re-formatting code grows with time and takes up more and more space in your presenter?
 * what if we have some other presenter, where we also have to send similar event?
 * testing re-formatting logic as a part of presenter might be difficult or impossible by number of reasons

Instead, with help of this library, we might have this:

```java
public class PetBuyPresenter extends BasePresenter {

	@Inject Janet janet;
    
	// some other code here
    
	public void onPetBuyButtonClick(PetEntity petEntity) {
    	// execute some business-related logic
        janet.createPipe(PetBuyEvent.class)
        	.send(new PetBuyEvent(petEntity)); // no problem here
    }
```
`PetBuyEvent` class knows how to reformat data from `petEntity` to something that analytics SDK expects, it can be tested as a single unit, it can collect some other data to enrich analytics event (e.g. add info about user, that bought a pet), it can be re-used from some other presenter as-is.

### Usage

##### 1. Setup tracker

Knowing needs of your analytics vendor's SDK create implementation of `Tracker` interface:

```java
	public class MyAnalyticsSdkTracker implements Tracker {
    
    public static final String MYANALYTICSSDK_TRACKER_KEY = "MyAnalyticsSdkTrackerKey";
    
    	@Override
        public String getKey() {
        	return MYANALYTICSSDK_TRACKER_KEY;
        }
        
        @Override
        public void trackEvent(String action, Map<String, Object> data) {
        	myAnalyticsSdk.sendEvent(action, prepareData(data));
        }
        
        private Map<String, String> prepareData(Map<String, Object> data) {
        	// final transformation - this will be special for any Tracker implementation
            // while Map<String, Object> is a more general contract.
            // E.g. you can put Integer or Boolean as a map item value
        }
    }
```

##### 2. Setup Janet with ActionService added

```java
	ActionService actionService = new AnalyticsService(provideTrackers());
	Janet janet = new Janet.Builder().addService(actionService).build();
```

##### 3. Create class for analytic event

```java
  @AnalyticsEvent(actionKey = "user_bought_pet" + ACTION_PATH_PARAM,
  		trackerIds = { MyAnalyticsSdkTracker.MYANALYTICSSDK_TRACKER_KEY })
  public class BuyPetEvent {

     @ActionPart
     String petType;

     @Attribute("pet_birth_date")
     String petBirthDate;

     @AttributeMap
     Map<String, Object> data;

     public BuyPetEvent(PetEntity petEntity) {
        petType = petEntity.petType.name().toLowerCase(Locale.US);
        petBirthDate = DateFormat.getDateInstance().format(petEntity.birthDate);
        data.put("pet_name", petEntity.name);
     }
  }
```
`@AnalyticsEvent` annotation - flags class as the one to be processed by `AnalyticsService` and also brings some additional info:

 * `actionKey` - name of event. Different analytics SDKs are similar to have param of this kind for every event
 * `trackerIds` - array of trackers' identifiers, where this action should end up

Annotations for class fields:

 * `@ActionPart` use this annotation in case if you want to format your actionKey at runtime
 * `@Attribute` - attribute's value and field value will form a key-value pair in `data` map that tracker recieves
 * `@AttributeMap` same as above but it might be easier to form a map than to create a [big] number of annotated fields

For more info please see sample project code.
For more sophisticated janet-usage - please see samples from [Janet repo](https://github.com/techery/janet) and Janet's [CommandActionService](https://github.com/techery/janet-command)

### Features

 * Event-classes can be written in Kotlin
 * Testable: for sample, refer to sample tests (will be implemented in future)

### Limitations

 * if event classes involve inheritance - only bottom-most inheritor's instance can be sent to service
 * event classes should not declare annotated fields as `private` - default visibility is applicable

### Download

Grab via Gradle:

```groovy
  repositories {
      ...
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
 * janet: [![](https://jitpack.io/v/techery/janet.svg)](https://jitpack.io/#techery/janet)

### Links

 * [Janet](https://github.com/techery/janet)
 * Janet's [CommandActionService](https://github.com/techery/janet-command)
 * [markdown-editor](https://jbt.github.io/markdown-editor) - handy GitHub-flavored markdown editor used to compose this README

## License

    Copyright (c) 2017 Techery

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
