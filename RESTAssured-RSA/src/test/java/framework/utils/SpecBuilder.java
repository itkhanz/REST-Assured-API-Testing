package framework.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class SpecBuilder {
    public static RequestSpecification reqSpec;

    public RequestSpecification getRequestSpecification() throws IOException {
        if (reqSpec == null) {
            PrintStream log  = new PrintStream(new FileOutputStream("logging.txt"));
            reqSpec = new RequestSpecBuilder()
                    .setBaseUri(PropUtils.getGlobalValue("baseUrl"))
                    .addQueryParam("key", "qaclick123")
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON)
                    .build();
        }
        return reqSpec;
    }
}
