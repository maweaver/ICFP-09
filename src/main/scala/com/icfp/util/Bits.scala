package com.icfp.util

object Bits {
  
  /**
   * Converts an array of bytes into a single number.  The first byte in the
   * array is assumed to have the most significant bits.
   */
  def byteArrayToLong(bytes: Array[Byte]): Long = {
    var total = 0l
    var pos = 0
    for(byte <- bytes.reverse) {
      total += (byte.toInt & 0xff) * Math.pow(2.0d, 8.0d * pos.toDouble).toInt
      pos += 1
    }
    
    total
  }
 
  /**
   * Returns a range of bits from a larger number, based on the representation
   * usually used, with 31 being the index of the highest bit, and 0 being the
   * index of the lowest.
   *
   * <p>For example, range(n, 31, 28) would return the upper 4 bits of a number,
   * shifted down so that they themselves formed a new integer.</p>
   */
  def range(value: Int, start: Int, end: Int): Int =
    (value & (Math.pow(2.0d, start.toDouble + 1.0d).toInt - 1)) >>> end
}