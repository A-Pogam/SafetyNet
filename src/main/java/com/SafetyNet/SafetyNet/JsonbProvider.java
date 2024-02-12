package com.SafetyNet.SafetyNet;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class JsonbProvider {

    private final Jsonb jsonb;

    public JsonbProvider() {
        this.jsonb = JsonbBuilder.create();
    }

    public Jsonb getJsonb() {
        return jsonb;
    }
}
