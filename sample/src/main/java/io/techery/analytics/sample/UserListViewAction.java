package io.techery.analytics.sample;

import io.techery.janet.analytics.annotation.AnalyticsEvent;

@AnalyticsEvent(actionKey = "userlistviewaction:$action_path", trackerIds = "tracker1")
public class UserListViewAction extends SomeBaseAction {

   public UserListViewAction(String baseClassAttribValName) {
      super(baseClassAttribValName);
   }
}
