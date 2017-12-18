package io.techery.analytics.sample;

import io.techery.janet.analytics.annotation.AnalyticsEvent;

import static io.techery.janet.analytics.annotation.ActionPart.ACTION_PATH_PARAM;

@AnalyticsEvent(actionKey = "userlistviewaction:" + ACTION_PATH_PARAM, trackerIds = "tracker1")
public class UserListViewAction extends SomeBaseAction {

   public UserListViewAction(String baseClassAttribValName) {
      super(baseClassAttribValName);
   }
}
