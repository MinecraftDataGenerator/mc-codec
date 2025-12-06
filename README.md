# MC-Codec

**MC-Codec** is a flexible codec system for Java, originally designed for Minecraft objects, but fully general-purpose
for any DTO (Data Transfer Object).

It provides:

* **Declarative serialization and deserialization** of objects to various formats (JSON, NBT, DataInput/DataOutput,
  etc.). **Important:** All Codec operations are treated as an I/O layer and **must** throw a `java.io.IOException` if
  parsing or transfer fails.
* **Cloning / copying** objects via CopyFunctions.
* Declarative definition of **attributes**, including getter, setter, and associated codec.
* Representation of object structures **declaratively and descriptively** (useful for debugging or analysis).
* Ability to construct codecs dynamically after object definition.
* Versioning support via different codecs in a `NavigableMap`, e.g., using `Protocol` as a key.

---

## Features

### Codec Types

1. **SingletonCodecBuilder**: For primitive types, Strings, or single attributes. Ideal for creating your own reusable
   codecs.
2. **[MutablePOJOCodecBuilder](Mutable%20POJO.md)**: For POJOs with getters/setters and a no-argument constructor.
3. **[ImmutablePOJOCodecBuilder](Immutable%20POJO.md)**: For immutable objects (e.g., Java Records) using a builder
   pattern.

A detailed example for creating custom codecs is provided in the separate documentation *
*[Codec Basics](Codec%20Basics.md)**.

### Advantages

* Fully declarative object structure definition.
* All codecs store **name**, **description**, type information, and serialization logic.
* JSON dumps allow visualization of the object structure.
* Easy to **add, replace, remove, or reposition attributes**.

---

## Example: Deserialization and Error Handling

All deserialization operations must be handled due to their I/O nature:

``` java
try {
    MyObject obj = MY_OBJECT_CODEC.deserializer().deserialize(inputSource);
    // Success
} catch (java.io.IOException e) {
    // Error during reading (DataInput/DataOutput) or parsing (JSON/NBT).
    System.err.println("Error decoding: " + e.getMessage());
}
```
---
## Versioning
This is just an example how you can store different codecs per version.
``` java
NavigableMap<Integer, Codec<DataInput, DataOutput, MyObject>> versions = new TreeMap<>();
versions.put(754, MY_OBJECT_CODEC_1_16); // Example protocol version
versions.put(755, MY_OBJECT_CODEC_1_17); // Example protocol version
```