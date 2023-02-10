package com.ialocation.plugin;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

class Utils
{
    public static JSONArray reactToJSON(ReadableArray args) throws JSONException
    {
        JSONArray jsonArray = new JSONArray();

        for (int i=0, l=args.size(); i<l; i++) {
            ReadableType valueType = readableArray.getType(i);

            switch (valueType){
                case Null:
                    jsonArray.put(JSONObject.NULL);
                    break;

                case Boolean:
                    jsonArray.put(readableArray.getBoolean(i));
                    break;

                case Number:
                    jsonArray.put(readableArray.getDouble(i));
                    break;

                case String:
                    jsonArray.put(readableArray.getString(i));
                    break;

                case Map:
                    jsonArray.put(reactToJSON(readableArray.getMap(i)));
                    break;

                case Array:
                    jsonArray.put(reactToJSON(readableArray.getArray(i)));
                    break;
            }
        }

        return jsonArray;
    }

    public static float[] getFloatValues(JSONArray floats, CallbackContext callbackContext) throws JSONException
    {
        if (floats.length() != 16) {
            callbackContext.error("Argument must be an array with exactly 16 floats");
        }

        float[] values = new float[16];

        for (int i = 0; i<16; i++) {
            values[i] = (float) floats.getDouble(i);
        }

        Log.d("IndoorAtlasUtils", "Input: " + floats);
        Log.d("IndoorAtlasUtils", "Output: " + Arrays.toString(values));

        return values;
    }

    public static float[] getFloatValues(ReadableArray floats, Callback error)
    {
        if (floats.size() != 16) {
            error.invoke("Argument must be an array with exactly 16 floats");
        }

        float[] values = new float[16];

        for (int i = 0; i<16; i++) {
            values[i] = (float) floats.getDouble(i);
        }

        Log.d("IndoorAtlasUtils", "Input: " + floats);
        Log.d("IndoorAtlasUtils", "Output: " + Arrays.toString(values));

        return values;
    }
}
