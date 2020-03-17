package com.acorns

import java.util

class VerUserRecTestBase {
  //Sets up an Array List of Expected Results to check for presence of cards for Verified New Users with Recurring
  def verRecExpectedResult(): util.ArrayList[String] = {
    val expectedCardValues: util.ArrayList[String] = new util.ArrayList[String]

    expectedCardValues.add("a3_present_account_strength_widget")
    expectedCardValues.add("a3_present_roundup_widget")
    expectedCardValues.add("a3_present_onboarding_roundups_video_v2")
    expectedCardValues.add("a3_present_onboarding_recurring_investment")
    expectedCardValues.add("a3_present_recurring_calculator")
    expectedCardValues.add("a3_present_one_time_invest_widget")
    expectedCardValues.add("a3_present_found_money_carousel")
    expectedCardValues.add("a3_present_grow_carousel")
    expectedCardValues.add("a3_present_referral_widget")
    println("Expected Card Type: " + expectedCardValues.toString)
    return expectedCardValues
  }


}
