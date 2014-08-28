package com.remimichel.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DownloadDeserializer implements JsonDeserializer<Download> {


    @Override
    public Download deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = (JsonObject) jsonElement;

        return new Download(jObject.get("id").getAsInt(),
                jObject.get("name").getAsString(), jObject.get("percentDone").getAsFloat());
    }
}
