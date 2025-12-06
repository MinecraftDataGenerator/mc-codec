# Mutable POJO

The following code provides an example how a Mutable POJO can be expressed in codecs.
You can read or write a Handshake Packet using `CODEC.serializer()` and `CODEC.deserializer()`.

``` java
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.dietrichpaul.mccodec.Codec;
import de.dietrichpaul.mccodec.builder.MutablePOJOCodecBuilder;

import java.io.BufferedWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HandshakePacket {

    private static final Codec<DataInput, DataOutput, HandshakePacket> CODEC =
            MutablePOJOCodecBuilder.of(
                            HandshakePacket::new,
                            HandshakePacket.class,
                            DataInput.class,
                            DataOutput.class
                    )
                    .setName("Handshake")
                    .addLast("Address", HandshakePacket::getAddress, HandshakePacket::setAddress, GlobalCodecs.UTF)
                    .addLast("Port", HandshakePacket::getPort, HandshakePacket::setPort, GlobalCodecs.USHORT)
                    .create();

    private String address;
    private int port;

    public HandshakePacket(String address, int port) {
        this.address = address;
        this.port = port;
    }

    private HandshakePacket() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        JsonObject jsonDump = CODEC.toJsonDump();
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("dump.json"))) {
            new GsonBuilder().setPrettyPrinting().create().toJson(jsonDump, writer);
        }
    }
}

```

The global codecs are:
``` java
public class GlobalCodecs {

    public static final Codec<DataInput, DataOutput, String> UTF = io(String.class, "UTF")
            .setSerializer(DataOutput::writeUTF)
            .setDeserializer(DataInput::readUTF)
            .create();

    public static final Codec<DataInput, DataOutput, Integer> USHORT = io(Integer.class, "UShort")
            .setSerializer(DataOutput::writeShort)
            .setDeserializer(DataInput::readUnsignedShort)
            .create();

    public static <T> SingletonCodecBuilder<DataInput, DataOutput, T> io(Class<T> type, String name) {
        return new SingletonCodecBuilder<DataInput, DataOutput, T>().setType(type).setName(name);
    }
}
```

The dump shows the following:
``` json
{
  "name": "Handshake",
  "type": "test.HandshakePacket",
  "format": "mutable_pojo",
  "attributes": [
    {
      "name": "Address",
      "codec": {
        "name": "UTF",
        "type": "java.lang.String",
        "format": "singleton"
      }
    },
    {
      "name": "Port",
      "codec": {
        "name": "UShort",
        "type": "java.lang.Integer",
        "format": "singleton"
      }
    }
  ]
}
```