# Immutable POJO

The following code shows how are immutable POJO can be expressed using codecs.
To not bloat the code in length Attributes should be saved in a constant.

PaintingVariant:

``` java
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.dietrichpaul.mccodec.Codec;
import de.dietrichpaul.mccodec.builder.ImmutablePOJOCodecBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public record PaintingVariant(
        String asset,
        int height,
        int width,
        String title,
        String author
) {

    public static Codec<JsonObject, JsonObject, PaintingVariant> JSON_CODEC
            = new ImmutablePOJOCodecBuilder<JsonObject, JsonObject, PaintingVariant, PaintingVariantBuilder>(
            PaintingVariantBuilder::new,
            PaintingVariantBuilder::new,
            PaintingVariant.class)
            .addLast("asset", PaintingVariantBuilder::asset, PaintingVariantBuilder::asset, JsonCodecs.jsonString("asset_id"))
            .addLast("author", PaintingVariantBuilder::author, PaintingVariantBuilder::author, JsonCodecs.minecraftText("author"))
            .addLast("title", PaintingVariantBuilder::title, PaintingVariantBuilder::title, JsonCodecs.minecraftText("title"))
            .addLast("width", PaintingVariantBuilder::width, PaintingVariantBuilder::width, JsonCodecs.jsonInt("width"))
            .addLast("height", PaintingVariantBuilder::height, PaintingVariantBuilder::height, JsonCodecs.jsonInt("height"))
            .create();

    public static void main(String[] args) throws IOException {
        JsonObject jsonDump = JSON_CODEC.toJsonDump();
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("json_paintingvariant.json"))) {
            new GsonBuilder().setPrettyPrinting().create().toJson(jsonDump, writer);
        }

        JsonObject burningSkull = new Gson().fromJson("""
                {
                  "asset_id": "minecraft:burning_skull",
                  "author": {
                    "color": "gray",
                    "translate": "painting.minecraft.burning_skull.author"
                  },
                  "height": 4,
                  "title": {
                    "color": "yellow",
                    "translate": "painting.minecraft.burning_skull.title"
                  },
                  "width": 4
                }
                """, JsonObject.class);

        PaintingVariant deserialize = PaintingVariant.JSON_CODEC.deserializer().deserialize(burningSkull);
        System.out.println(deserialize);
    }
}
```

PaintingVariantBuilder:

``` java
import de.dietrichpaul.mccodec.builder.Builder;

import java.util.Objects;

public class PaintingVariantBuilder implements Builder<PaintingVariant> {
    private String asset;
    private int height;
    private int width;
    private String title;
    private String author;

    /* package */ PaintingVariantBuilder() {
    }

    /* package */ PaintingVariantBuilder(PaintingVariant of) {
        this.asset = of.asset();
        this.height = of.height();
        this.width = of.width();
        this.title = of.title();
        this.author = of.author();
    }

    public String asset() {
        return asset;
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public String title() {
        return title;
    }

    public String author() {
        return author;
    }

    public PaintingVariantBuilder asset(String asset) {
        this.asset = asset;
        return this;
    }

    public PaintingVariantBuilder height(int height) {
        this.height = height;
        return this;
    }

    public PaintingVariantBuilder width(int width) {
        this.width = width;
        return this;
    }

    public PaintingVariantBuilder title(String title) {
        this.title = title;
        return this;
    }

    public PaintingVariantBuilder author(String author) {
        this.author = author;
        return this;
    }

    @Override
    public PaintingVariant create() {
        Objects.requireNonNull(asset, "asset must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(author, "author must not be null");

        return new PaintingVariant(asset, height, width, title, author);
    }
}
```

These are my codecs:

``` java
public class JsonCodecs {

    public static Codec<JsonObject, JsonObject, String> jsonString(String attribute) {
        return new SingletonCodecBuilder<JsonObject, JsonObject, String>()
                .setName("JsonString(key:" + attribute + ")")
                .setType(String.class)
                .setSerializer((out, object) -> out.addProperty(attribute, object))
                .setDeserializer(input -> input.get(attribute).getAsString())
                .create();
    }

    public static Codec<JsonObject, JsonObject, String> minecraftText(String attribute) {
        return new SingletonCodecBuilder<JsonObject, JsonObject, String>()
                .setName("MinecraftText(key:" + attribute + ")")
                .setType(String.class)
                .setSerializer((out, object) -> out.addProperty(attribute, object))
                .setDeserializer(input -> input.get(attribute).toString())
                .create();
    }

    public static Codec<JsonObject, JsonObject, Integer> jsonInt(String attribute) {
        return new SingletonCodecBuilder<JsonObject, JsonObject, Integer>()
                .setName("JsonInt(key:" + attribute + ")")
                .setType(Integer.class)
                .setSerializer((out, object) -> out.addProperty(attribute, object))
                .setDeserializer(input -> input.get(attribute).getAsInt())
                .create();
    }

}
```

The json dump shows the following:

``` json
{
  "type": "nbt.PaintingVariant",
  "format": "immutable_pojo",
  "attributes": {
    "builder": "nbt.PaintingVariantBuilder",
    "attributes": [
      {
        "name": "asset",
        "codec": {
          "name": "JsonString(key:asset_id)",
          "type": "java.lang.String",
          "format": "singleton"
        }
      },
      {
        "name": "author",
        "codec": {
          "name": "MinecraftText(key:author)",
          "type": "java.lang.String",
          "format": "singleton"
        }
      },
      {
        "name": "title",
        "codec": {
          "name": "MinecraftText(key:title)",
          "type": "java.lang.String",
          "format": "singleton"
        }
      },
      {
        "name": "width",
        "codec": {
          "name": "JsonInt(key:width)",
          "type": "java.lang.Integer",
          "format": "singleton"
        }
      },
      {
        "name": "height",
        "codec": {
          "name": "JsonInt(key:height)",
          "type": "java.lang.Integer",
          "format": "singleton"
        }
      }
    ]
  }
}
```

Running the programms outputs the correctly parsed "burning_skull" Registry Entry:
`PaintingVariant[asset=minecraft:burning_skull, height=4, width=4, title={"color":"yellow","translate":"painting.minecraft.burning_skull.title"}, author={"color":"gray","translate":"painting.minecraft.burning_skull.author"}]
`