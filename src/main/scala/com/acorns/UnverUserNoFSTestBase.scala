package com.acorns

import java.util

class UnverUserNoFSTestBase {
  def unverifiedNoFSExpectedResult(): util.ArrayList[String] = {
    val expectedCardValues: util.ArrayList[String] = new util.ArrayList[String]

    expectedCardValues.add("a3_present_account_strength_widget")
    expectedCardValues.add("a3_present_onboarding_roundups_video_v2")
    expectedCardValues.add("a3_present_grow_carousel")
    println("Expected Card Type: " + expectedCardValues.toString)
    return expectedCardValues
  }

}
