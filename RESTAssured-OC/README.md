# REST Assured API Automation from scratch + Framework + CI

* This repo contains the code snippets following
  the [Udemy course](https://www.udemy.com/course/rest-assured-api-automation/) created by Omparkash Chavan.
* It also includes the extended practice exercises and enhancement to the original framework developed during the
  course.
* [REST Assured wiki](https://github.com/rest-assured/rest-assured/wiki/Usage)

---

## Learning Outcomes

<img src="doc/topics.png" alt="course topics">

<img src="doc/framework-design.png" alt="framework design">

## Demo Websites

* https://reqres.in/
* https://httpbin.org/
* http://dummy.restapiexample.com/
* https://jsonplaceholder.typicode.com/
* https://randomuser.me/

## Postman Essentials

* Create a new collection and a new test environment.
* Setup `baseUrl` https://api.postman.com as a variable in the test environment and link this environment to the newly
  created collection.
* Now the endpoint can be written as: {{baseUrl}}/workspaces
* Generate API Key:
    * Click on the profile icon in Postman APP and then Settings.
    * It will open up the browser. Click on the API keys section and then `+ Generate API Key`
    * Note the API key somewhere safe as Postman will mask it later.
    * Save this key as an environment variable
    * [Using your Postman API key](https://learning.postman.com/docs/developer/postman-api/authentication/#generate-a-postman-api-key)
* [Postman API overview](https://learning.postman.com/docs/developer/postman-api/intro-api/)
* [Postman API Documentation](https://www.postman.com/postman/workspace/postman-public-workspace/documentation/12959542-c8142d51-e97c-46b6-bd77-52bb66712c9a)
* Navigate to Postman API -> Workspaces -> Get all workspaces, and read the documentation

<img src="doc/postmanAPI-getWorkspaces.png">

<img src="doc/postmanAPI-getWorkspace.png">

---

## JSON Essentials

* [JSON Path Finder](https://jsonpathfinder.com/)
* [JSONPath Online Evaluator - jsonpath.com](https://jsonpath.com/)
* [JSON (using JsonPath)](https://github.com/rest-assured/rest-assured/wiki/Usage#json-using-jsonpath)
* JsonPath uses Groovy's GPath syntax. [Groovy JSON](https://groovy-lang.org/json.html)
* GPath is a path expression language integrated into Groovy which allows parts of nested structured data to be
  identified.
* [GPath](https://docs.groovy-lang.org/latest/html/documentation/core-semantics.html#gpath_expressions) use a dot-object
  notation to perform object navigation.
* Groovy Playground
    * http://groovyconsole.appspot.com/
    * https://www.jdoodle.com/execute-groovy-online/
    * https://onecompiler.com/groovy

```json
{
  "firstName": "Larry",
  "lastName": "Sheen",
  "age": 30,
  "children": [],
  "spouse": null,
  "vehicle": true,
  "address": {
    "street": "6301 Richardson Drive",
    "city": "New York City",
    "state": "New York",
    "zipCode": "65027"
  },
  "phoneNumbers": [
    {
      "type": "mobile",
      "number": "543 666-6794"
    },
    {
      "type": "home",
      "number": "555 650-2200"
    }
  ]
}
```

```groovy
import groovy.json.JsonSlurper

def object = new JsonSlurper().parseText(
        '''
{INSERT_YOUR_JSON_HERE}
'''
)

def query = object
println query
```

* Following paths can be constructed to retrieve the data:
    * **Street Address** `object.address.street`
    * **First phone number** `object.phoneNumbers[0].number`
    * **All phone numbers as array** `object.phoneNumbers.number`

* [Jayway JsonPath](https://github.com/json-path/JsonPath)
* RestAssured uses Groovy JsonPath syntax by default. For using Jayway JsonPath, it has to be added as a dependency.
* Both implementations are different syntax-wise. For example, to extract all the phone numbers from sample JSON:
    * Groovy syntax: `phoneNumbers.number`
    * Jayway syntax: `$.phoneNumbers..number`

---

## HTTP Essentials

<img src="doc/http-methods.png" alt="http methods">

---

## Postman Mock Server

* Mock server is useful when backend APIs are not completely ready therefore developers can develop the frontend with
  the help of API specifications.
* Create a new mock collection and add a mock server. Use the Url of the mock server and create an example response for
  the APIs to automate.
* Mock server can also be executed from the browser and it will return the same response as in Postman.
* Use the `x-mock-response-code` header with different status codes to test different status codes for examples.

---

## REST Assured

[REST-assured official website](https://rest-assured.io/)
[REST Assured Usage Wiki](https://github.com/rest-assured/rest-assured/wiki/Usage)

### Setup

#### Pre-requisites

* Java JDK 17
* TestNG
* IDE - Intellij
* Maven

> JSONPath and XMLPath are added as transitive dependencies automatically when rest-assured is added.

> Intellij Tip! Use the shortcut Ctrl+alt+shift+v to paste the code without auto-formatting. Hence it keeps the
> formatting of the copied code.

* [Static Imports](https://github.com/rest-assured/rest-assured/wiki/Usage#static-imports)
* In order to use REST assured effectively it's recommended to statically import methods from the following classes:

```java
import static io.restassured.RestAssured.*
import static io.restassured.matcher.RestAssuredMatchers.*
import static org.hamcrest.Matchers.*
```

* Example Test Case to verify if dependencies are copnfigired properly:

```java
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class HelloWorldTest {

    @Test
    public void example_test() {
        given().when().then();
    }
}
```

### Getting Started

#### Static Imports

* With Static imports, we do not have to use the Class name together with the method name to use it e.g. `given()`
  method comes from RestAssured class.
* The disadvantage of static imports is we do not get to know what all methods come with the class as the Intellij
  suggests the methods when the Class name is followed by dot.
* The difference can be seen by the example code in `NonStaticImports` and `StaticImports` classes in Practice package.

#### Method Chaining

* also known as builder pattern.
* `given()` method returns `RequestSpecification` interface which has definitions for most of the methods.
* All these methods return an object of the same class that implements the `RequestSpecification` interface. Since all
  the methods return the same object, it is possible to chain them.
* The concept is further explained in the `MethodChaining` class in Practice package.
* Since all the methods in the class return the object of the same class, so these methods can be chained together.
  Similar concept is implemented by REST assured.

#### Automate GET Request

* All the information that needs to be sent as a part of the request such as path parameters, query parameters, headers,
  authorization, and request body etc are to be included in `given()` method.
* `when()` represents the action that is going to take place such as execute HTTP request methods like GET POST etc on a
  given URL.
* `then()` represents the outcome of the event that got executed in `when()`. The output can be validated in `then()`
  such as response code, headers body, cookie.
* `AutomateGet` class in the Practice package has the example of Postman API for automating workspaces endpoint.
* The response can be extracted with `extract()` method and save it as an object of `Response` class. Response can be
  converted to string with `asString()`method.
* Following code snipped shows multiple ways to extract the single field from `Response res` object:

```java
        //first way
        System.out.println("Workspace name = "+JsonPath.from(res.asString()).getString("workspaces[0].name"));

                //second way
                System.out.println("Workspace name = "+res.path("workspaces[0].name"));

                //third way
                JsonPath jpath=new JsonPath(res.asString());
                System.out.println("Workspace name = "+jpath.getString("workspaces[0].name"));
```

#### Hamcrest Assertion on Extracted response

* `import static org.hamcrest.MatcherAssert.assertThat;` and assert with hamcrest `assertThat()` method.

```java
        //Hamcrest assertion
        assertThat(name,equalTo("My Workspace"));

                //TestNG assertion
                Assert.assertEquals(name,"My Workspace");
```

#### What is Hamcrest

* Hamcrest is a well known assertion library used for unit testing along with JUnit.
* Hamcrest can be used along with Rest Assured for assertions.
* Uses matcher classes for making assertions
* [Hamcrest Matcher Interface Documentation](https://hamcrest.org/JavaHamcrest/javadoc/2.2/)

##### Advantages

* Human readable and in plain english
* Code is neat and intuitive
* Provides thin methods like "is" and "not", also called as decorators, for more readibility

##### Hamcrest vs TestNG

* Readability
* Descriptive error messages
* Type Safety

#### Hamcrest Collection Matchers

##### Collection matchers (List, Array, Map, etc.)

* hasItem() -> check single element in a collection
* not(hasItem()) -> check single element is NOT in a collection
* hasItems() -> Check all elements are in a collection
* contains() -> Check all elements are in a collection and in a strict order
* containsInAnyOrder() -> Check all elements are in a collection and in any order
* empty() -> Check if collection is empty
* not(emptyArray()) -> Check if the Array is not empty
* hasSize() -> Check size of a collection
* everyItem(startsWith()) -> Check if every item in a collection starts with specified string
* hasKey() -> Map -> Check if Map has the specified key [value is not checked]
* hasValue() -> Map -> Check if Map has at least one key matching specified value
* hasEntry() -> Maps -> Check if Map has the specified key value pair
* equalTo(Collections.EMPTY_MAP) -> Maps [Check if empty]
* allOf() -> Matches if all matchers matches
* anyOf() -> Matches if any of the matchers matches

##### Numbers:

* greaterThanOrEqualTo()
* lessThan()
* lessThanOrEqualTo()

##### String:

* containsString()
* emptyString()

* The matchers usage is demonstrated in the methods `validate_response_body_hamcrest_learnings()`
  and `validate_response_body()` in the `AutomateGet` class in the Practice package.

### Logging

* [REST Assured logging documentation](https://github.com/rest-assured/rest-assured/wiki/Usage#logging)
* The `Logging` class in the practice package demonstrates the different logging methods with example test cases.

**Request Logging**

```java
given().log().all()... // Log all request specification details including parameters, headers and body
        given().log().params()... // Log only the parameters of the request
        given().log().body()... // Log only the request body
        given().log().headers()... // Log only the request headers
        given().log().cookies()... // Log only the request cookies
        given().log().method()... // Log only the request method
        given().log().path()... // Log only the request path
```

**Response Logging**

```java
get("/x").then().log().statusLine()... // Only log the status line
        get("/x").then().log().headers()... // Only log the response headers
        get("/x").then().log().cookies()... // Only log the response cookies
```

* If you're only interested in printing the response body if an error occur then you can use:

```java
get("/x").then().log().ifError()...
```

* If you want to print the response body regardless of the status code you can do:

```java
get("/x").then().log().body()..
```

**Log if validation fails**

* Since REST Assured 2.3.1 you can log the request or response only if the validation fails. To log the request do:

```java
given().log().ifValidationFails()...

        ...then().log().ifValidationFails()...
```

* It's also possible to enable this for both the request and the response at the same time using the LogConfig:

```java
given().config(RestAssured.config().logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails(HEADERS)))...
```

* This will log only the headers if validation fails.

**Blacklist Headers from Logging**

* As of REST Assured 4.2.0 it's possible to blacklist headers so that they are not shown in the request or response log.
  Instead the header value will be replaced with [ BLACKLISTED ]. You can enable this per header basis using the
  LogConfig:

```java
given().config(config().logConfig(logConfig().blacklistHeader("Accept")))...
```

### Handling Headers

* Headers consist of meta-data that are sent along with a request and response.
* `AutomateHeaders` class contains the example code snippets for demonstrating various headers methods in REST Assured.

##### Set Mock Environment for Headers

* copy the Url of the Mock Server from created mock collection.
* create the mock examples `headerValue1` and `headerValue2` and add the headers `responseHeader`
  and `Content-Type: application/json` and body message information in examples.
* `x-mock-match-request-headers` request header should contain the name of the header in examples so the server can
  decide which response to return based on this header value.
* Server will return the response header and body according to the request header.
*

<img src="doc/mock-headers.png" alt="mock headers">
<img src="doc/mock-header-value1.png" alt="mock headers example 1">

```java
        Header header=new Header("headerName","value1");
        Header matchHeader=new Header("x-mock-match-request-headers","headerName");
        given().
        baseUri(BASEURI).
        header(header).
        header(matchHeader).
        when().
```

##### Multiple Headers in Request - Using Headers

* The disadvantage of the above approach is that for many headers, the `header()` method has to be repeated many times.
* We can use the `Headers` object and `headers()` method provided by REST Assured to group the headers and pass
  together.

```java
        Header header=new Header("headerName","value1");
        Header matchHeader=new Header("x-mock-match-request-headers","headerName");

        Headers headers=new Headers(header,matchHeader);
        given().
        baseUri(BASEURI).
        headers(headers).
```

##### Multiple Headers in Request - Using Map

* Alternatively, we can create the map of headers as key-value pairs strings and pass it directly to `headers()` method.
* `headers()` method is overloaded and it can accept map as well as Headers object.

```java
        HashMap<String, String> headers=new HashMap<String, String>();
        headers.put("headerName","value1");
        headers.put("x-mock-match-request-headers","headerName");

        given().
        baseUri(BASEURI).
        headers(headers).
```

##### Multi Value Header

* [Multi value parameters](https://github.com/rest-assured/rest-assured/wiki/Usage#multi-value-parameter)
* Multi-value headers can be passed either directly to headers() method or with `Headers` object and passing
  multiple `Header` object key-value pairs to it.

```java
        Header header1=new Header("multiValueHeader","value1");
        Header header2=new Header("multiValueHeader","value2");

        Headers headers=new Headers(header1,header2);

        given().
        baseUri(BASEURI).
        headers(headers).
```

* Response header can also be validated by putting inside `header()` method after `then()`. You can assert for multiple
  headers in the same call to `headers()` also.
* If you need to perform further operations on header, then you can `extract().headers()` method and save it
  as `Headers` object.
* you can use the `Headers.getValues()` method to extract the multi value header in a list.

### Request Specification
* All the code snippets for this section are implemented under `RequestSpecificationExample.java` class in Practice package.
* RequestSpecification can be used to write test cases in style other than BDD.
* `given()` method returns the reference of the interface `RequestSpecification`.
* So what is happening is when we say that the given method returns, the reference of the interface, it is indirectly
  returning the object of the class that implements the interface. So basically the object of the class that implements
  all the abstract methods that are available in the interface. So basically, once we get the object of the class, it
  means we get access to the implemented methods.

```java
        RequestSpecification requestSpecification=given()
        .baseUri(BASEURI)
        .header("X-Api-Key",API_KEY);

        given()
        .spec(requestSpecification).
        when()
        .get("/workspaces")
        .then()
        .log().all()
        .assertThat().statusCode(200)
        ;
```

* you can also use `with()` method instead of `given()` method as there is no difference functionality wise.
* To reuse the requestSpecification object, create a class variable and initialize the requestSpecification in TestNG
  BeforeClass hook.

```java
    RequestSpecification requestSpecification;

@BeforeClass
public void beforeClass(){
        requestSpecification=with()
        .baseUri(BASEURI)
        .header("X-Api-Key",API_KEY);
        }

@Test
public void validate_status_code(){
        given()
        .spec(requestSpecification).
        when()
        .get("/workspaces")
        .then()
        .log().all()
        .assertThat().statusCode(200)
        ;
        }
```

* Now we can start to convert our test cases from BDD to non-BDD style.

```java
    RequestSpecification requestSpecification;

@BeforeClass
public void beforeClass(){
        requestSpecification=with()
        .baseUri(BASEURI)
        .header("X-Api-Key",API_KEY)
        .log().all()
        ;
        }

@Test
public void validate_status_code(){
        Response response=requestSpecification.get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(),is(equalTo(200)));
        }

@Test
public void validate_response_body(){
        Response response=requestSpecification.get("/workspaces").then().log().all().extract().response();
        assertThat(response.path("workspaces[0].name").toString(),equalTo("My Workspace"));
        }
```

* Rest Assured provides us an alternative way of creating RequestSpecification using `RequestSpecBuilder`
* You can also add more information to the request header for individual tests
  by `given(requestSpecification).header("dummyHeader", "dummyValue")`
* You can also chain the methods in RequestSpecBuilder instead of defining them separately as in below snippet.
* if you have got multiple request specifications, then you can set one of those specifications as default. So in that
  case you do not have to explicitly use the requestSpecification in the given() method in your test case.
* Instead of collecting the value in the requestSpecification instance variable, we can use the static variable from the RestAssured class.
* If you want to get the information from the RequestSpecification like headers or BaseUrl, you can query it using `QueryableRequestSpecification`

```java
    @BeforeClass
public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder=new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(BASEURI);
        requestSpecBuilder.addHeader("X-Api-Key",API_KEY);
        requestSpecBuilder.log(LogDetail.ALL);

        RestAssured.requestSpecification=requestSpecBuilder.build();
        }

@Test
public void queryTest(){
        QueryableRequestSpecification queryableRequestSpecification=SpecificationQuerier
                                        .query(RestAssured.requestSpecification);
        System.out.println(queryableRequestSpecification.getBaseUri());
        System.out.println(queryableRequestSpecification.getHeaders());
        }

@Test
public void validate_status_code(){
        //Method 3: non BDD Style using Spec Builder
        Response response=get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(),is(equalTo(200)));
        }

@Test
public void validate_response_body(){
        Response response=get("/workspaces").then().log().all().extract().response();
        assertThat(response.path("workspaces[0].name").toString(),equalTo("My Workspace"));
        }
```

### Response Specification

### Automate POST, PUT and DELETE

### Send Request Payload multiple ways

### Send Complex JSON as Request Payload

### Handling Request Parameters

### Multipart Form Data

### File upload and download

### Form URL Encoding

### JSON Schema Validation

### Filters

### Serialization and De-serialization

### Jackson Annotations

### Complex POJO

### Coding Challenges

### Authentication and Authorization

### OAuth Flows

### Google OAuth2.0

### Google OAuth2.0 - Implicit Grant Flow and OpenID Connect

### Google OAuth2.0 - Automate

### Form Based Authentication

### Handling Cookies

---

## Framework

<img src="doc/framework-design-project-structure.png" alt="framework design and project structure">



