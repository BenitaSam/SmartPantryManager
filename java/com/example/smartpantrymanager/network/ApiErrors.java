package com.example.smartpantrymanager.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Response;

/**turn API fails into readable messages for the user*/
public class ApiErrors {

    public static String fromResponse(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                JsonObject obj = JsonParser.parseString(response.errorBody().string()).getAsJsonObject();
                if (obj.has("errors")) {
                    StringBuilder sb = new StringBuilder();
                    JsonArray arr = obj.getAsJsonArray("errors");
                    for (JsonElement e : arr) {
                        if (sb.length() > 0) sb.append("\n");
                        sb.append(e.getAsString());
                    }
                    return sb.toString();
                }
                if (obj.has("error")) return obj.get("error").getAsString();
            }
        } catch (Exception ignored) { }
        return "Server error (" + response.code() + ")";
    }

    //request never reached server bc API stopped, wrong address or no network
    public static String fromFailure(Throwable t) {
        return "Could not reach the server. Is the Node.js API running and the address correct?";
    }
}