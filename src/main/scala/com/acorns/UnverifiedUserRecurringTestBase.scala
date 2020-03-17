package com.acorns

import java.util

class UnverifiedUserRecurringTestBase {
  //This class will be used to setup Acceptance Criteria and to update the Criteria as needed

//Sets up an Array List of Expected Results to check for presence of cards for Unverified New Users with Recurring
  def unverifiedRecExpectedResult(): util.ArrayList[String] = {
    val expectedCardValues: util.ArrayList[String] = new util.ArrayList[String]

    expectedCardValues.add("a3_present_account_strength_widget")
    expectedCardValues.add("a3_present_onboarding_roundups_video_v2")
    expectedCardValues.add("a3_present_onboarding_recurring_investment")
    expectedCardValues.add("a3_present_recurring_calculator")
    expectedCardValues.add("a3_present_grow_carousel")
    println("Expected Card Type: " + expectedCardValues.toString)
     return expectedCardValues
  }
}



