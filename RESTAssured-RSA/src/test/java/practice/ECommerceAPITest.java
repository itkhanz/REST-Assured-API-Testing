package practice;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import practice.pojo.LoginRequest;
import practice.pojo.LoginResponse;
import practice.pojo.OrderDetail;
import practice.pojo.Orders;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ECommerceAPITest {

    private static String EMAIL = ""; ////register with dummy email and add your email and password before test execution
    private static String PASSWORD = "";

    /**
     * URL: https://rahulshettyacademy.com/client/
     * Test Steps
     * 1. Login and extract authorization token
     * 2. Create product
     * 3. Add product to the cart
     * 4. Checkout product
     * 5. Delete product
     */
    @Test
    public void test_E2EEcommerce() {
        //Step 01: login
        Map<String, String> loginData = loginAndGetAuthToken(); //get auth-token and userID

        //Step 02: Create Product
        String productId = createProduct(loginData);

        //Step 03 and 04: Add product to the cart and checkout
        String responseAddOrder = createOrder(loginData, productId);

        //extracting the order response
        JsonPath js = new JsonPath(responseAddOrder);
        String orderId =js.get("orders[0]");
        String productOrderId = js.get("productOrderId[0]");
        String orderMessage = js.get("message");

        Assert.assertEquals(productOrderId, productId);
        Assert.assertEquals(orderMessage, "Order Placed Successfully");

        //Step 05: Delete Product
        String deleteProductResponse = deleteProduct(loginData, productId);
        JsonPath js1 = new JsonPath(deleteProductResponse);
        Assert.assertEquals(js1.get("message"),"Product Deleted Successfully");

    }

    private Map<String, String> loginAndGetAuthToken() {
        //Build req specification for login with URL
        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();

        //Fill in user credentials for LoginRequest POJO
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail(EMAIL);
        loginRequest.setUserPassword(PASSWORD);

        //Build req specification with login credentials from LoginRequest POJO
        RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);

        //Perform login and extract response as LoginResponse POJO
        LoginResponse loginResponse = reqLogin
                .when().post("/api/ecom/auth/login")
                .then().log().all()
                .extract().response().as(LoginResponse.class);

        System.out.println(loginResponse.getToken());
        String token = loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        String userId =loginResponse.getUserId();

        Map<String, String> loginMap = Map.ofEntries(
                new AbstractMap.SimpleEntry<String, String>("token", token),
                new AbstractMap.SimpleEntry<String, String>("userId", userId)
        );

        return loginMap;
    }

    private String createProduct(Map<String, String> loginData) {
        //Creating basic specification for adding product
        RequestSpecification addProductBaseReq=	new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", loginData.get("token"))
                .build();

        //creating specification for new product with form-data params and image
        RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq)
                .param("productName", "addidas shirt")
                .param("productAddedBy", loginData.get("userId"))
                .param("productCategory", "fashion")
                .param("productSubCategory", "shirts")
                .param("productPrice", "11500")
                .param("productDescription", "Addias Originals")
                .param("productFor", "women")
                .multiPart("productImage",new File("src/test/java/practice/files/shirt.jpeg"));

        //making api call create new product
        String addProductResponse = reqAddProduct
                .when()
                .post("/api/ecom/product/add-product")
                .then()
                .log().all()
                .extract().response().asString();

        //extracting the productId
        JsonPath js = new JsonPath(addProductResponse);
        String productId =js.get("productId");

        return productId;
    }

    private String createOrder(Map<String, String> loginData, String productId){
        //creating base request specification for adding product to cart
        RequestSpecification createOrderBaseReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", loginData.get("token"))
                .setContentType(ContentType.JSON)
                .build();

        //filling in order details for checkout
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("India");
        orderDetail.setProductOrderedId(productId);

        //creating POJO classes for OrderDetail and Orders
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        orderDetailList.add(orderDetail);
        Orders orders = new Orders();
        orders.setOrders(orderDetailList);

        //adding request body with order detail to base req spec
        RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(orders);

        //Making API call to create order
        String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
        System.out.println(responseAddOrder);

        return responseAddOrder;
    }

    private String deleteProduct (Map<String, String> loginData, String productId){
        //creating base req specification for deleting order
        RequestSpecification deleteProdBaseReq=	new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", loginData.get("token"))
                .setContentType(ContentType.JSON)
                .build();


        //adding product ID to req spec for deleting product
        RequestSpecification deleteProdReq = given().log().all().spec(deleteProdBaseReq).pathParam("productId",productId);

        //making api call to delete product
        String deleteProductResponse = deleteProdReq
                .when()
                    .delete("/api/ecom/product/delete-product/{productId}")
                .then()
                    .log().all().
                extract().response().asString();

        return deleteProductResponse;
    }

}
