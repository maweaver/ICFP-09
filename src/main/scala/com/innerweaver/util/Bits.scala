package com.innerweaver.util

object Bits {
  
  /**
   * Converts an array of bytes into a single integer.  The first byte in the
   * array is assumed to have the most significant bits.
   */
  def byteArrayToInt(bytes: Array[Byte]): Int = {
    var total = 0
    var pos = 0
    for(byte <- bytes.reverse) {
      total += (byte.toInt & 0xff) * Math.pow(2.0d, 8.0d * pos.toDouble).toInt
      pos += 1
    }
    
    total
  }
 
  /**
   * Converts a little-endian 32-bit value into a big-endian value.  The first
   * 16 bits and last 16 bits are swapped.
   */
  def littleEndianToBig(value: Int): Int = 
    ((value & 0x0000FFFF) << 16) | ((value & 0xFFFF0000) >>> 16)
    
  /**
   * Returns a range of bits from a larger number, based on the representation
   * usually used, with 31 being the index of the highest bit, and 0 being the
   * index of the lowest.
   *
   * <p>For example, range(n, 31, 28) would return the upper 4 bits of a number,
   * shifted down so that they themselves formed a new integer.</p>
   */
  def range(value: Int, start: Int, end: Int): Int =
    (value & (Math.pow(2.0d, start.toDouble + 1.0d).toInt - 1)) >>> (end - 1)
}
