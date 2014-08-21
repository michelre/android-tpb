package com.remimichel.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TorrentDeserializer implements JsonDeserializer<Torrent> {


    @Override
    public Torrent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = (JsonObject) jsonElement;

        return new Torrent(jObject.get("download_link").getAsString(),
                jObject.get("size").getAsString(), jObject.get("title").getAsString());
    }
}
