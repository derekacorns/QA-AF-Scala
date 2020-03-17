//package com.acorns
//
//import org.junit._
//import Assert._
//import io.restassured.RestAssured._
//import org.hamcrest.Matchers.hasItem
//
//@Test
//class AppTest {
//
//    @Test
//    def testOK() = assertTrue(true)
//
//
//    get("https://action-feed.staging.acorns.io/users/e89bfe03-4bde-4c4d-8ac6-c5affd997129/" + "action_feed_cards" +
//      "?feed_context=a3_present").then.statusCode(200).assertThat.body("card_type", hasItem("a3_present_portfolio_education"))
//    System.out.println("Success!")
//
////    @Test
////    def testKO() = assertTrue(false)
//
//}
//
//
