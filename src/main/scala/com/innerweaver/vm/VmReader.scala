package com.innerweaver.vm

import java.io.InputStream
import Vm.Address
import Vm.Data

/**
 * This class reads from binary files following the Orbital Executable Format.  
 * See Section 2.4 for details on this format.
 *
 * <p>It uses a Java InputStream rather than a Scala Source so that it can get
 * access to the raw byte data.</p>
 */
class VmReader(input: InputStream)
extends Iterator[Frame] {
  
  /**
   * Frames are stored sequentially by address, so this value is incremented
   * with each read
   */
  private var address: Address = 0
  
  /**
   * Marker, set when we hit eof
   */
  private var eof: Boolean = false
  
  /**
   * @inheritDoc
   */
  override def hasNext: Boolean = !eof
  
  /**
   * @inheritDoc
   */
  override def next(): Frame = {
    if(eof)
      return blankFrame
      
     // Each frame consists of a 32-bit opcode and 64-bit data value, for a
     // total of 96-bits, or 12 bytes.
     val frameSize = 12
     
     val buffer = new Array[Byte](frameSize)
     val bytesRead = input.read(buffer, 0, frameSize)
     
     if(bytesRead != frameSize) {
       eof = true
       val frame = blankFrame()
       address += 1
       frame
     } else {
       val data = (if(address % 2 == 0) buffer.slice(0, 8 ) else buffer.slice(4, 12)).force
       val op   = (if(address % 2 == 0) buffer.slice(8, 12) else buffer.slice(0,  4)).force
       
       
       
       println(Integer.toHexString(address) + " = " + op.mkString("[", ", ", "]") + ", " + data.mkString("[", ", ", "]"))
       val frame = blankFrame()
       address += 1
       frame
     }
  }
  
  /**
   * Used after eof has been hit.  Every value past eof has an address, a NOOP
   * opcode, and 0.0d for data
   */
  protected def blankFrame(): Frame = {
    val frameAddr = address
    Frame(address, Noop(address, address), 0.0d)
  }
}

/**
 * A frame consists of a pair with an opcode and a memory value.  These are both
 * located at the same address, in different memory spaces.
 */
case class Frame(address: Address, opcode: Opcode, data: Data)
