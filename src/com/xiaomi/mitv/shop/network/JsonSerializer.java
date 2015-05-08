/**
 *   Copyright(c) 2012 DuoKan TV Group
 *
 *   JsonSerializer.java
 *
 *   @author tianli (tianli@duokan.com)
 *
 *   @date 2012-6-24
 */
package com.xiaomi.mitv.shop.network;

import android.util.Log;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import java.util.Collection;

/**
 * @author tianli
 *
 */
public class JsonSerializer
{
    private static final String TAG = JsonSerializer.class.getName();
    private static JsonSerializer instance = new JsonSerializer(); 
    
    private ObjectMapper impl;

    public static JsonSerializer getInstance()
    {
        return instance;
    }
    
    JsonSerializer()
    {
        try
        {
            impl = new ObjectMapper();
        }
        catch (Exception e)
        {
        }
        DeserializationConfig cfg = impl.getDeserializationConfig();
        cfg.set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public String serialize(Object object) {
        try {
            return impl.writeValueAsString(object);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public <T> T deserialize(String json,  Class<T> clazz) {
        try {
            return impl.readValue(json, clazz);
        } catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public <T extends Collection<?>,V> Object deserialize(String json, Class<T> collection, Class<V> data) {
        try {
            return impl.readValue(json, TypeFactory.collectionType(collection, data));
        }catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }
}
