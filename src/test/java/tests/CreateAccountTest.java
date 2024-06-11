package tests;

import objectData.request.RequestAccount;
//import objectData.request.RequestBook1;
import objectData.response.ResponseAccountSucces;
//import objectData.response.ResponseBookSuccess;
import objectData.response.ResponseTokenSuccess;
import restClient.RestClient;
import org.testng.annotations.Test;
import propertiesUtillity.PropertiesUtillity;
import service.AccountService;

public class CreateAccountTest {

//    public RequestBook1 requestBook1;
    public RequestAccount requestAccountBody;
    public String userID;
    public AccountService accountService;
    public String token;
    RestClient restClient = new RestClient();

    @Test
    public void testMethod(){
        System.out.println("===STEP 1: CREATE NEW ACCOUNT===");
        createAccont();

        System.out.println("===STEP 2: GENERATE TOKEN===");
        generateToken();

        System.out.println("===STEP 3: CHECK ACCOUNT===");
        checkAccountPresents();

        System.out.println("===STEP 4: DELETE ACCOUNT===");
        deleteUser();

        System.out.println("===STEP 5: RECHECK ACCOUNT===");
        checkAccountPresents();

//        System.out.println("===STEP 6: ADD BOOK===");
//        addBook();
        
    }

    public void createAccont(){

        //pregatim requestul
        PropertiesUtillity propertiesUtillity = new PropertiesUtillity("Request/CreateAccountData");
        requestAccountBody = new RequestAccount(propertiesUtillity.getAllData());

        accountService = new AccountService();

        ResponseAccountSucces responseAccountSucces = accountService.createAccount(requestAccountBody);
        userID = responseAccountSucces.getUserId();
    }

    public void generateToken(){

        ResponseTokenSuccess responseTokenSuccess = accountService.generateToken(requestAccountBody);
        token = responseTokenSuccess.getToken();

    }

    public void checkAccountPresents(){
        accountService.checkAccountPresence(userID,token);

    }

    public void deleteUser(){
        accountService.deleteAccount(userID,token);

    }



//    public void addBook(){
//        //configuram clientul
//        RequestSpecification requestSpecification = RestAssured.given();
//        requestSpecification.baseUri("https://demoqa.com");
//        requestSpecification.contentType("application/json");
//
//        //pregatim requestul
//        PropertiesUtillity propertiesUtillity = new PropertiesUtillity("Request/CreateAccountData");
//        requestBook1 = new RequestBook1(propertiesUtillity.getAllData());
//
//        //ne autorizam pe baza la token
//        requestSpecification.header("Authorization","Bearer " + token);
//
//        //executam request-ul
//        requestSpecification.body(requestBook1);
//        Response response = requestSpecification.post("/BookStore/v1/Books");
//
//        //validam respons-ul
//        System.out.println(response.getStatusLine());
//        Assert.assertTrue(response.getStatusLine().contains("1200"));
//        Assert.assertTrue(response.getStatusLine().contains("User not authorized!"));
//
//        ResponseBookSuccess responseBookSuccess = response.body().as(ResponseBookSuccess.class);
//        System.out.println(responseBookSuccess.getClass());
//        System.out.println(responseBookSuccess.getClass());

}
