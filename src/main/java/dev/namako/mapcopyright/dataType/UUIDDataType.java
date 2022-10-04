package dev.namako.mapcopyright.dataType;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDDataType implements PersistentDataType<byte[], UUID> {
  @Override
  public Class<byte[]> getPrimitiveType() {
    return byte[].class;
  }

  @Override
  public Class<UUID> getComplexType() {
    return UUID.class;
  }

  @Override
  public byte[] toPrimitive(UUID complex, PersistentDataAdapterContext context) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
    byteBuffer.putLong(complex.getMostSignificantBits());
    byteBuffer.putLong(complex.getLeastSignificantBits());
    return byteBuffer.array();
  }

  @Override
  public UUID fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(primitive);
    long first = byteBuffer.getLong();
    long second = byteBuffer.getLong();
    return new UUID(first, second);
  }
}
