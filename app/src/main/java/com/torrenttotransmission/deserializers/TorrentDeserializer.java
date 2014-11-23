package com.torrenttotransmission.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.torrenttotransmission.model.Connection;
import com.torrenttotransmission.model.Torrent;
import com.torrenttotransmission.model.TorrentPiratebay;
import com.torrenttotransmission.model.TorrentT411;

import java.lang.reflect.Type;

public class TorrentDeserializer implements JsonDeserializer<Torrent> {


    @Override
    public Torrent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = (JsonObject) jsonElement;
        if(Connection.selectedAPI.getName().equals("t411"))
            return new TorrentT411(jObject.get("size").getAsString(), jObject.get("id").getAsString(), jObject.get("title").getAsString());
        else
            return new TorrentPiratebay(jObject.get("size").getAsString(), jObject.get("title").getAsString(), jObject.get("url").getAsString(), jObject.get("download_link").getAsString());
    }
}
