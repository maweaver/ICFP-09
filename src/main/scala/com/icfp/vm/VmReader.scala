package com.icfp.vm

import java.io.InputStream
import util.Bits
import Vm.Address
import Vm.Data

object VmReader {
 
  /**
   * Reads binary data from an input stream and uses it to populate the given
   * VM.
   */
  def populateVm(vm: Vm, input: InputStream) {
    val reader = new VmReader(input)
    for(frame <- reader) {
      vm.data += frame.address -> frame.data
      vm.instructions += frame.address -> frame.opcode
    }
  }
  
}

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
      print("Raw frame data: ")
      for(byte <- buffer)
        print(Integer.toHexString(byte.toInt & 0xFF) + " ")
      println()
      
      val dataBytes = (if(address % 2 == 0) { buffer.take(8) } else { buffer.drop(4) }).force.reverse
      print("Raw data: ")
      for(byte <- dataBytes)
        print(Integer.toHexString(byte.toInt & 0xFF) + " ")
      println()
      
      println("As long: " + Bits.byteArrayToLong(dataBytes))
      
      val data = java.lang.Double.longBitsToDouble(Bits.byteArrayToLong(dataBytes))
      val opData = Bits.byteArrayToLong((if(address % 2 == 0) { buffer.drop(8) } else { buffer.take(4) }).force.reverse).toInt
       
      val byteCode = Bits.range(opData, 31, 28)
       
      val opCode = if(byteCode == 0) sCode(opData) else dCode(opData)
       
      println(opCode + ", " + data)
      val frame = Frame(address, opCode, data)
      address += 1
      frame
    }
  }
  
  /**
   * Parses some data as an S-Type OpCode
   */
  protected def sCode(opData: Int): Opcode = {
    val op = Bits.range(opData, 27, 24)
    val imm = Bits.range(opData, 23, 21)
    val r1 = Bits.range(opData, 13, 0)
    
    println("SCode found, op = " + Integer.toHexString(op) + ", imm = " + Integer.toHexString(imm) + ", r1 = " + Integer.toHexString(r1))
    
    op match {
      case Opcode.SCode.Noop => Noop(address, r1)
      case Opcode.SCode.Cmpz => Cmpz(address, imm, r1)
      case Opcode.SCode.Sqrt => Sqrt(address, r1)
      case Opcode.SCode.Copy => Copy(address, r1)
      case Opcode.SCode.Input => Input(address, r1)
    }
  }
  
  /**
   * Parses some data as a D-Type OpCode
   */
  protected def dCode(opData: Int): Opcode = {
    val op = Bits.range(opData, 31, 28)
    val r1 = Bits.range(opData, 27, 14)
    val r2 = Bits.range(opData, 13, 0)
    
    println("SCode found, op = " + Integer.toHexString(op) + ", r1 = " + Integer.toHexString(r1) + ", r2 = " + Integer.toHexString(r2))
    
    op match {
      case Opcode.DCode.Add => Add(address, r1, r2)
      case Opcode.DCode.Sub => Sub(address, r1, r2)
      case Opcode.DCode.Mult => Mult(address, r1, r2)
      case Opcode.DCode.Div => Div(address, r1, r2)
      case Opcode.DCode.Output => Output(address, r1, r2)
      case Opcode.DCode.Phi => Phi(address, r1, r2)
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
