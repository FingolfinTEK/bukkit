package com.github.fingolfintek.bukkit;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;

public class KryoSerializationUtil {

    public static byte[] serialize(Object toSerialize) {
        Kryo kryo = new Kryo();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeClassAndObject(output, toSerialize);
        return outputStream.toByteArray();
    }

    public static <T> T deserialize(byte[] toDeserialize) {
        Kryo kryo = new Kryo();
        Input input = new Input(toDeserialize);
        return (T) kryo.readClassAndObject(input);
    }
}
