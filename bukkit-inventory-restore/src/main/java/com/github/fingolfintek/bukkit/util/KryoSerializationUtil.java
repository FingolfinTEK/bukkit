package com.github.fingolfintek.bukkit.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayOutputStream;

public class KryoSerializationUtil {

    public static byte[] serialize(Object toSerialize) {
        Kryo kryo = new Kryo();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeClassAndObject(output, toSerialize);
        output.close();
        return outputStream.toByteArray();
    }

    public static <T> T deserialize(byte[] toDeserialize) {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        Input input = new Input(toDeserialize);
        T deserialized = (T) kryo.readClassAndObject(input);
        input.close();
        return deserialized;
    }
}
