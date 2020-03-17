package com.acorns

import java.util

import io.restassured.RestAssured
import io.restassured.RestAssured.baseURI
import io.restassured.http.Header
import io.restassured.path.json.JsonPath
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.testng.annotations.Test


@Test
class TestAFUnverifiedNoFS extends UnverUserNoFSTestBase {
  def createUnverNoFSUserAF(): Unit = {
    //Get Acceptance Version UUID
    RestAssured.baseURI = "https://backend.staging.acorns.io/v1/acceptance_documents/program_agreement"

    val acctprghttprequest2: RequestSpecification = RestAssured.given()
    acctprghttprequest2.header(new Header("Content-Type", "application/json"))
    acctprghttprequest2.header(new Header("Accept", "*/*"))
    acctprghttprequest2.header(new Header("Accept-Encoding", "gzip, deflate"))

    val accpDocResponse: Response = acctprghttprequest2.get(baseURI)
    println("Response is: " + accpDocResponse.asString())

    val acctPrgPath: JsonPath = accpDocResponse.jsonPath()
    val accptVersUUID = acctPrgPath.getString("acceptance_document.acceptance_document_version_uuid")
    println("This is Acceptance Version UUID " + accptVersUUID)

    //Get ETF Acceptance Version UUID

    RestAssured.baseURI = "https://backend.staging.acorns.io/v1/acceptance_documents/eft_agreement"

    val acctETFResponse: Response = acctprghttprequest2.get(baseURI)
    println("Response is: " + acctETFResponse.asString())

    val acctETFPath: JsonPath = acctETFResponse.jsonPath()
    val accptVersETFUUID = acctETFPath.getString("acceptance_document.acceptance_document_version_uuid")
    println("This is Acceptance ETF Version UUID " + accptVersETFUUID)

    //Setup a call to create user
    RestAssured.baseURI = "https://backend.staging.acorns.io/v1/user"

    val crUserhttprequest2: RequestSpecification = RestAssured.given()


    crUserhttprequest2.header(new Header("Content-Type", "application/json"))
    crUserhttprequest2.header(new Header("Accept", "*/*"))
    crUserhttprequest2.header(new Header("Accept-Encoding", "gzip, deflate"))
    crUserhttprequest2.header(new Header("X-Client-App", "acorns_qa"))
    crUserhttprequest2.header(new Header("X-Client-Platform", "ios"))
    crUserhttprequest2.header(new Header("X-Client-Build", "2.9.4"))
    crUserhttprequest2.header(new Header("X-Client-Os", "2.4.2"))

    crUserhttprequest2.body("{\n \"user\": {\n \"email\": \"tstunvnofs1965@acorns.com\",\n  \"password\": \"Welcome1\"\n" +
      "  },\n  \"udid\": \"08ce4a99-ca5c-4b34-a80a-19f4fef24c19\",\n  \"pin\": \"1234\",\n" +
      "  \"invitation_code\": null,\n  \"code_group\": \"acorns\",\n" +
      "  \"acceptance_document_version_uuids\": [\n    \"" + accptVersUUID + "\",\"" + accptVersETFUUID + "\"]\n}")

    val crUserResponse: Response = crUserhttprequest2.post(baseURI)
    println("Response is: " + crUserResponse.asString())

    //Get Token and UUID

    val tokenUserPath: JsonPath = crUserResponse.jsonPath()
    val usrToken = tokenUserPath.getString("token")
    val usrUUID = tokenUserPath.getString("user_uuid")
    println("This is Token and UUID :" + usrToken, usrUUID)

    //Update First/Last Name, Address, Phone Number

    crUserhttprequest2.body("{ \"token\":\"" + usrToken + "\",\"user\":{\"profile\":{\"first_name\"" +
      ":\"TestUnverNoFS\",\"last_name\":\"Test\",\"dob\":\"1976/01/22\",\"phone_number\":\"7145565656\"," +
      "\"address1\":\"5601 East Orangethorpe Avenue\",\"city\":\"Anaheim\",\"state\":\"CA\",\"zip\":\"92807\"," +
      "\"ssn\":\"966796798\",\"us_resident\":true,\"us_citizen\":true}}}")

    val updUserResponse: Response = crUserhttprequest2.put(baseURI)
    println("This is Updated User Response : " + updUserResponse.asString())

    //Setup Scrt Question

    RestAssured.baseURI = "https://graphql.staging.acorns.io/graphql"
    val gqlSrthttprequest2: RequestSpecification = RestAssured.given().queryParam("token", usrToken)
    gqlSrthttprequest2.header(new Header("Content-Type", "application/json"))
    gqlSrthttprequest2.header(new Header("Authorization", "token " + usrToken))
    gqlSrthttprequest2.header(new Header("Accept", "*/*"))
    gqlSrthttprequest2.header(new Header("Cache-Control", "no-cache"))
    gqlSrthttprequest2.header(new Header("cache-control", "no-cache"))
    gqlSrthttprequest2.header(new Header("Connection", "keep-alive"))
    gqlSrthttprequest2.header(new Header("Accept-Encoding", "gzip, deflate"))

    gqlSrthttprequest2.body("{\"operationName\":\"UpdateSecurityQuestion\",\"variables\":{\"answer1\":\"Chap\"," +
      "\"question1\":\"ecb4fee2-9e7d-4f90-b79f-09312244c0a0\"}," +
      "\"query\":\"mutation UpdateSecurityQuestion($answer1: String!, $question1: String!)" +
      " {\\n  updateSecurityQuestion(input: {answer1: $answer1, question1: $question1})" +
      " {\\n    uuid\\n    __typename\\n  }\\n}\\n\"}")

    val gqlSrtResponse: Response = gqlSrthttprequest2.post(baseURI)
    println("This is Scrt Question Response: " + gqlSrtResponse.asString())

    //Setup Broker Response

    RestAssured.baseURI = "https://graphql.staging.acorns.io/graphql"
    val gqlAfflQsthttprequest2: RequestSpecification = RestAssured.given()
    gqlAfflQsthttprequest2.header(new Header("Content-Type", "application/json"))
    gqlAfflQsthttprequest2.header(new Header("Authorization", "token " + usrToken))
    gqlAfflQsthttprequest2.header(new Header("Accept", "*/*"))
    gqlAfflQsthttprequest2.header(new Header("Cache-Control", "no-cache"))
    gqlAfflQsthttprequest2.header(new Header("cache-control", "no-cache"))
    gqlAfflQsthttprequest2.header(new Header("Connection", "keep-alive"))
    gqlAfflQsthttprequest2.header(new Header("Accept-Encoding", "gzip, deflate"))

    gqlAfflQsthttprequest2.body("{\"operationName\":\"UpdateInvestorQuestions\"," +
      "\"variables\":{\"employedAffiliatedWithBrokerDealer\":false,\"shareholderOrDirector\":false," +
      "\"subjectToBackupWithholding\":false}," +
      "\"query\":\"mutation UpdateInvestorQuestions($employedAffiliatedWithBrokerDealer: Boolean!," +
      " $shareholderOrDirector: Boolean!, $subjectToBackupWithholding: Boolean!)" +

      " {\\n  updateInvestorQuestions(input: {employedAffiliatedWithBrokerDealer:" +
      " $employedAffiliatedWithBrokerDealer, shareholderOrDirector: $shareholderOrDirector," +
      " subjectToBackupWithholding: $subjectToBackupWithholding}) {\\n    uuid\\n    __typename\\n  }\\n}\\n\"}")
    val gqlAfflResponse: Response = gqlAfflQsthttprequest2.post(baseURI)
    println("This is Affiliated Response: " + gqlAfflResponse.asString())

    //Setup Suitability

    RestAssured.baseURI = "https://graphql.staging.acorns.io/graphql"
    val gqlStblhttprequest2: RequestSpecification = RestAssured.given()
    gqlStblhttprequest2.header(new Header("Content-Type", "application/json"))
    gqlStblhttprequest2.header(new Header("Authorization", "token " + usrToken))
    gqlStblhttprequest2.header(new Header("Accept", "*/*"))
    gqlStblhttprequest2.header(new Header("Cache-Control", "no-cache"))
    gqlStblhttprequest2.header(new Header("cache-control", "no-cache"))
    gqlStblhttprequest2.header(new Header("Connection", "keep-alive"))
    gqlStblhttprequest2.header(new Header("Accept-Encoding", "gzip, deflate"))

    gqlStblhttprequest2.body("{\"operationName\":\"updateSuitability\",\"variables\":" +
      "{\"input\":[{\"questionId\":\"7e7073ed-dd28-4f06-877f-317d3e3026d1\"," +
      "\"answerId\":\"da988219-7fc0-47f7-a714-9665fabf7253\"}," +
      "{\"questionId\":\"1f259237-22bc-4331-8d50-50bd27b49292\"," +
      "\"answerId\":\"30bb6e27-4c5d-4d28-bd8f-fda433f9e041\"}," +
      "{\"questionId\":\"d9e22c19-4237-43b6-a0d2-7459ae2cda38\"," +
      "\"answerId\":\"26e52561-01d9-4c43-a4bb-c18a42abbe1c\"}," +
      "{\"questionId\":\"395dbf91-2410-425a-ab43-7a49827a4cae\"," +
      "\"answerId\":\"a35fcc4e-e280-4082-9f3f-b6f76d75b332\"}," +
      "{\"questionId\":\"dbd1bfe2-9a07-490d-90c0-4295a0adbdef\"," +
      "\"answerId\":\"21e83496-1d21-4227-a2cf-5c97fe85a4fa\"}," +
      "{\"questionId\":\"3f3555fd-1cf5-444d-ab2c-b5734fee717f\"," +
      "\"answerId\":\"b46ea7c2-1469-4120-b557-7d48f3ec9095\"}," +
      "{\"questionId\":\"4b698048-ec72-4be3-a2fb-6be5ef85b069\"," +
      "\"answerId\":\"a19407fd-a288-447c-bd04-3bd1c9e4ce9f\"}]}," +
      "\"query\":\"mutation updateSuitability($input: [suitability_SuitabilityInput!]!)" +
      " {\\n  updateSuitability(suitabilityInput: $input)" +
      " {\\n    recommendedPortfolio {\\n      id\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}")
    val gqlStblResponse: Response = gqlStblhttprequest2.post(baseURI)
    println("This is Suitability Response: " + gqlStblResponse.asString())

    //Setup Portfolio

    RestAssured.baseURI = "https://graphql.staging.acorns.io/graphql"
    val gqlPrhttprequest2: RequestSpecification = RestAssured.given()
    gqlPrhttprequest2.header(new Header("Content-Type", "application/json"))
    gqlPrhttprequest2.header(new Header("Authorization", "token " + usrToken))
    gqlPrhttprequest2.header(new Header("Accept", "*/*"))
    gqlPrhttprequest2.header(new Header("Cache-Control", "no-cache"))
    gqlPrhttprequest2.header(new Header("cache-control", "no-cache"))
    gqlPrhttprequest2.header(new Header("Connection", "keep-alive"))
    gqlPrhttprequest2.header(new Header("Accept-Encoding", "gzip, deflate"))

    gqlPrhttprequest2.body("{\"operationName\":\"setPortfolio\"," +
      "\"variables\":{\"input\":{\"id\":\"d695af20-e18c-413d-ae92-d0bd28bcf066\"}}," +
      "\"query\":\"mutation setPortfolio($input: SetPortfolioInput!)" +
      " {\\n  setPortfolio(input: $input)" +
      " {\\n    portfolio {\\n      id\\n      name\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}")
    val gqlPrResponse: Response = gqlPrhttprequest2.post(baseURI)
    println("This is Portfolio Response: " + gqlPrResponse.asString())

    //Setup Referral Agreement

    RestAssured.baseURI = "https://graphql.staging.acorns.io/graphql"
    val gqlRefAgrhttprequest2: RequestSpecification = RestAssured.given()
    gqlRefAgrhttprequest2.header(new Header("Content-Type", "application/json"))
    gqlRefAgrhttprequest2.header(new Header("Authorization", "token " + usrToken))
    gqlRefAgrhttprequest2.header(new Header("Accept", "*/*"))
    gqlRefAgrhttprequest2.header(new Header("Cache-Control", "no-cache"))
    gqlRefAgrhttprequest2.header(new Header("cache-control", "no-cache"))
    gqlRefAgrhttprequest2.header(new Header("Connection", "keep-alive"))
    gqlRefAgrhttprequest2.header(new Header("Accept-Encoding", "gzip, deflate"))

    gqlRefAgrhttprequest2.body("{\"operationName\":\"ReferralAgreement\",\"variables\":{}," +
      "\"query\":\"query ReferralAgreement {\\n  acceptanceDocuments(requiredFor: \\\"verification\\\")" +
      " {\\n    id\\n    name\\n    type\\n    __typename\\n  }\\n}\\n\"}")
    val gqlRefAgrResponse: Response = gqlRefAgrhttprequest2.post(baseURI)
    println("This is Referral Agreement Response: " + gqlRefAgrResponse.asString())

    //Setup Complete Registration

    RestAssured.baseURI = "https://backend.staging.acorns.io/v1/user"
    val gqlCmtltprequest2: RequestSpecification = RestAssured.given()
    gqlCmtltprequest2.header(new Header("Content-Type", "application/json"))
    gqlCmtltprequest2.header(new Header("Authorization", "token " + usrToken))
    gqlCmtltprequest2.header(new Header("Accept", "*/*"))
    gqlCmtltprequest2.header(new Header("Cache-Control", "no-cache"))
    gqlCmtltprequest2.header(new Header("cache-control", "no-cache"))
    gqlCmtltprequest2.header(new Header("Connection", "keep-alive"))
    gqlCmtltprequest2.header(new Header("Accept-Encoding", "gzip, deflate"))

    gqlCmtltprequest2.body("{\"token\":\"" + usrToken + "\",\"user\":{\"profile\":{\"registration_complete\":true}}}")
    val gqlCmtlResponse: Response = gqlCmtltprequest2.put(baseURI)
    println("This is Complete Registration Response: " + gqlCmtlResponse.asString())

    //Setup AF Call

    val AFRequest: RequestSpecification = RestAssured.given().pathParam("uuid", usrUUID)
    RestAssured.baseURI = "https://sm-action-feed.staging.acorns.io/users/{uuid}/" +
      "action_feed_cards?feed_context=a3_present"
    val AFResponse: Response = AFRequest.get(baseURI)

    //Navigate to "card_type" to get the list of AF Cards
    val AFCards: JsonPath = AFResponse.jsonPath()
    val actualCardValues: util.ArrayList[String] = AFCards.get("card_type")

    println("Card Type: " + actualCardValues.toString)

    //Create Base Class instance
    val testBase: UnverUserNoFSTestBase = new UnverUserNoFSTestBase

    if (actualCardValues equals testBase.unverifiedNoFSExpectedResult()) {
      println("Test Passed!")
    } else {
      println("Test Failed!")
    }

  }
}
