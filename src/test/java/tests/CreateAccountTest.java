package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import objectData.request.RequestAccount;
import objectData.response.ResponseAccountSucces;
import objectData.response.ResponseTokenSuccess;
import org.testng.Assert;
import org.testng.annotations.Test;
import propertiesUtillity.PropertiesUtillity;

public class CreateAccountTest {

    public RequestAccount requestAccountBody;

    public String userID;
    public String token;


    @Test
    public void testMethod(){
        System.out.println("===STEP 1: CREATE NEW ACCOUNT===");
        createAccont();

        System.out.println("===STEP 2: GENERATE TOKEN");
        generateToken();

        System.out.println("===STEP 3: CHECK ACCOUNT");
        checkAccountPresents();

        System.out.println("===STEP 4: DELETE ACCOUNT");
        deleteUser();

        System.out.println("===STEP 5: RECHECK ACCOUNT");
        checkAccountPresents();
        
    }

    public void createAccont(){
        //configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        //pregatim requestul
        PropertiesUtillity propertiesUtillity = new PropertiesUtillity("Request/CreateAccountData");
        requestAccountBody = new RequestAccount(propertiesUtillity.getAllData());

        //executam request-ul
        requestSpecification.body(requestAccountBody);
        Response response = requestSpecification.post("/Account/v1/User");

        //validam respons-ul
        System.out.println(response.getStatusLine());
        Assert.assertTrue(response.getStatusLine().contains("201"));
        Assert.assertTrue(response.getStatusLine().contains("Created"));

        ResponseAccountSucces responseAccountSucces = response.body().as(ResponseAccountSucces.class);
//        responseBody.prettyPrint();
        userID = responseAccountSucces.getUserId();

        Assert.assertTrue(responseAccountSucces.getUsername().equals(requestAccountBody.getUserName()));
        System.out.println(responseAccountSucces.getUserId());
    }

    public void generateToken(){
        //configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        //executam request-ul
        requestSpecification.body(requestAccountBody);
        Response response = requestSpecification.post("/Account/v1/GenerateToken");

        //validam respons-ul
        System.out.println(response.getStatusLine());
        Assert.assertTrue(response.getStatusLine().contains("200"));
        Assert.assertTrue(response.getStatusLine().contains("OK"));

        ResponseTokenSuccess responseTokenSuccess = response.body().as(ResponseTokenSuccess.class);
        token = responseTokenSuccess.getToken();
        Assert.assertEquals(responseTokenSuccess.getStatus(),"Success");
        Assert.assertEquals(responseTokenSuccess.getResult(),"User authorized successfully.");

    }

    public void checkAccountPresents(){
        //configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        //ne autorizam pe baza la token
        requestSpecification.header("Authorization","Bearer " + token);

        //executam request-ul
        Response response = requestSpecification.get("/Account/v1/User/" + userID);

        //validam respons-ul
        System.out.println(response.getStatusLine());

        if (response.getStatusLine().contains("200")){
            Assert.assertTrue(response.getStatusLine().contains("200"));
            Assert.assertTrue(response.getStatusLine().contains("OK"));
        } else {
            Assert.assertTrue(response.getStatusLine().contains("401"));
            Assert.assertTrue(response.getStatusLine().contains("Unauthorized"));
        }

    }

    public void deleteUser(){
        //configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        //ne autorizam pe baza la token
        requestSpecification.header("Authorization","Bearer " + token);

        //executam request-ul
        Response response = requestSpecification.delete("/Account/v1/User/" + userID);
        System.out.println(response.getStatusLine());

        //validam respons-ul
        Assert.assertTrue(response.getStatusLine().contains("204"));
        Assert.assertTrue(response.getStatusLine().contains("No Content"));
    }
}
