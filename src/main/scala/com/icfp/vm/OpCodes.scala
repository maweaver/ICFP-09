package com.icfp.vm

import Vm.Address

/**
 * Base class for the Orbital VM opcodes
 */
class Opcode(code: Int, rd: Address)  {
  
  def opcode: String = getClass.getName.substring("com.icfp.vm".length + 1)

  override def toString(): String =
    "[" + code + "]" +
    "(" + opcode + ")" +
    "@" + Integer.toHexString(rd)
}

/**
 * Opcode constants
 */
object Opcode {
  
  /**
   * Opcode value for D-Type codes
   */
  object DCode {
    
    val Add = 0x1
    val Sub = 0x2
    val Mult = 0x3
    val Div = 0x4
    val Output = 0x5
    val Phi = 0x6
    
  }
  
  object SCode {
    
    val Noop = 0x0
    val Cmpz = 0x1
    val Sqrt = 0x2
    val Copy = 0x3
    val Input = 0x4
  }
}

/**
 * D-Codes take Double parameters
 */
class DCode(val code0: Int, val rd0: Address, val r10: Address, val r20: Address)
extends Opcode(code0, rd0) {
 
  override def toString(): String =
    super.toString() + 
    ", " + Integer.toHexString(r10) +
    ", " + Integer.toHexString(r20)
}

/**
 * S-Codes take a Single parameter
 */
class SCode(val code0: Int, val rd0: Address, val r10: Address)
extends Opcode(code0, rd0) {
 
  override def toString(): String =
    super.toString() +
    ", " + Integer.toHexString(r10)
}

/**
 * rd < - mem[r1] + mem[r2]
 */
case class Add(rd: Address, r1: Address, r2: Address)
extends DCode(Opcode.DCode.Add, rd, r1, r2)

/**
 * rd < - mem[r1] - mem[r2]
 */
case class Sub(rd: Address, r1: Address, r2: Address)
extends DCode(Opcode.DCode.Sub, rd, r1, r2)

/**
 * rd < - mem[r1] * mem[r2]
 */
case class Mult(rd: Address, r1: Address, r2: Address)
extends DCode(Opcode.DCode.Mult, rd, r1, r2)


/**
 * rd < - 0.0 if mem[r2] == 0.0 else mem[r1] / mem[r2]
 */
case class Div(rd: Address, r1: Address, r2: Address)
extends DCode(Opcode.DCode.Div, rd, r1, r2)

/**
 * port[r1] < - mem[r2]
 */
case class Output(rd: Address, r1: Address, r2: Address)
extends DCode(Opcode.DCode.Output, rd, r1, r2)

/**
 * rd < - mem[r1] if status == 1, else mem[r2]
 */
case class Phi(rd: Address, r1: Address, r2: Address)
extends DCode(Opcode.DCode.Phi, rd, r1, r2)


/**
 * rd < - mem[rd]
 */
case class Noop(rd: Address, r1: Address)
extends SCode(Opcode.SCode.Noop, rd, r1)


/**
 * status < - mem[r1] op 0.0
 */
case class Cmpz(rd: Address, imm: Int, r1: Address)
extends SCode(Opcode.SCode.Cmpz, rd, r1) {
 
  def op: ComparisonOperator = imm match {
    case ComparisonOperator.Ltz => Ltz()
    case ComparisonOperator.Lez => Lez()
    case ComparisonOperator.Eqz => Eqz()
    case ComparisonOperator.Gez => Gez()
    case ComparisonOperator.Gtz => Gtz()
  }
  
  override def toString(): String =
   super.toString() +
   " " + imm.toString() + " 0.0"
  
}

/**
 * status < - abs(sqrt(mem[r1]))
 */
case class Sqrt(rd: Address, r1: Address)
extends SCode(Opcode.SCode.Sqrt, rd, r1)


/**
 * rd < - mem[r1]
 */
case class Copy(rd: Address, r1: Address)
extends SCode(Opcode.SCode.Copy, rd, r1)

/**
 * rd < - port[r1]
 */
case class Input(rd: Address, r1: Address)
extends SCode(Opcode.SCode.Input, rd, r1)

/**
 * Comparison operators used for the cmpz instruction
 */
class ComparisonOperator(code: Int)

/**
 * Constants for comparison operators
 */
object ComparisonOperator {
 
  val Ltz = 0x0
  val Lez = 0x1
  val Eqz = 0x2
  val Gez = 0x3
  val Gtz = 0x4
  
}

/**
 * Less-than zero
 */
case class Ltz
extends ComparisonOperator(ComparisonOperator.Ltz) {
 
  override def toString(): String = "<"
}

/**
 * Less-than-or-equal-to zero
 */
case class Lez
extends ComparisonOperator(ComparisonOperator.Lez) {
 
  override def toString(): String = "<="
}

/**
 * Equal to zero
 */
case class Eqz
extends ComparisonOperator(ComparisonOperator.Eqz) {
 
  override def toString(): String = "="
}

/**
 * Greater-than-or-equal-to zero
 */
case class Gez
extends ComparisonOperator(ComparisonOperator.Gez) {
 
  override def toString(): String = ">="
}

/**
 * Greater than zero
 */
case class Gtz
extends ComparisonOperator(ComparisonOperator.Gtz) {
 
  override def toString(): String = ">"
}
