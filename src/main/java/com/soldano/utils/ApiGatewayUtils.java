package com.soldano.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public final class ApiGatewayUtils {

    private ApiGatewayUtils() {
    }

    public static APIGatewayProxyResponseEvent getResponse(int statusCode) {
        return getResponse(statusCode, null);
    }

    public static APIGatewayProxyResponseEvent getResponse(int statusCode, String body) {
        APIGatewayProxyResponseEvent res = new APIGatewayProxyResponseEvent();
        res.setStatusCode(statusCode);
        res.setBody(body);

        return res;
    }

    public static String getPathParamValue(APIGatewayProxyRequestEvent req, String paramName) {
        String paramEncoded = req.getPathParameters().get(paramName);

        return URLDecoder.decode(paramEncoded, StandardCharsets.UTF_8);
    }
}
