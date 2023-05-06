# REST Assured API Automation from scratch + Framework + CI

* This repo contains the code snippets following
  the [Udemy course](https://www.udemy.com/course/rest-assured-api-automation/) created by Omparkash Chavan.
* It also includes the extended practice exercises and enhancement to the original framework developed during the
  course.
* [REST Assured wiki](https://github.com/rest-assured/rest-assured/wiki/Usage)

---

## Learning Outcomes

<img src="doc/topics.png" alt="course topics">

<img src="doc/framework-design-project-structure.png" alt="framework design and project structure">

<img src="doc/framework-design.png" alt="framework design">

## Postman Essentials

* Create a new collection and a new test environment.
* Setup `baseUrl` https://api.postman.com as a variable in the test environment and link this environment to the newly created collection.
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
* GPath is a path expression language integrated into Groovy which allows parts of nested structured data to be identified. 
* [GPath](https://docs.groovy-lang.org/latest/html/documentation/core-semantics.html#gpath_expressions) use a dot-object notation to perform object navigation.
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

** HTTP Essentials

<img src="doc/http-methods.png" alt="http methods">

---

** Postman Mock Server

* Mock server is useful when backend APIs are not completely ready therefore developers can develop the frontend with the help of API specifications.
* Create a new mock collection and add a mock server. Use the Url of the mock server and create an example response for the APIs to automate.
* Mock server can also be executed from the browser and it will return the same response as in Postman.
* Use the `x-mock-response-code` header with different status codes to test different status codes for examples.

---

