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
      total += (byte.toInt & 0xff) * Math.pow(2.0d, 8.0d * pos.toDouble).toLong
      pos += 1
    }
    
    total
  }
  
  def intToByteArray(value: Int): Array[Byte] = 
    (((value & 0xFF000000) >>> 24).toByte ::
     ((value & 0x00FF0000) >>> 16).toByte ::
     ((value & 0x0000FF00) >>> 8).toByte ::
     ((value & 0x000000FF) >>> 0).toByte ::
     Nil).toArray
     
  def longToByteArray(value: long): Array[Byte] =
    (((value & 0xFF00000000000000l) >>> 56).toByte ::
     ((value & 0x00FF000000000000l) >>> 48).toByte ::
     ((value & 0x0000FF0000000000l) >>> 40).toByte ::
     ((value & 0x000000FF00000000l) >>> 32).toByte ::
     ((value & 0x00000000FF000000l) >>> 24).toByte ::
     ((value & 0x0000000000FF0000l) >>> 16).toByte ::
     ((value & 0x000000000000FF00l) >>> 8).toByte ::
     ((value & 0x00000000000000FFl) >>> 0).toByte ::
     Nil).toArray
  
  def doubleToByteArray(value: Double): Array[Byte] =
    longToByteArray(java.lang.Double.doubleToLongBits(value))
 
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
