package com.innerweaver.vm

import Vm.Address

/**
 * Base class for the Orbital VM opcodes
 */
class Opcode(code: Int, rd: Address)  {

  override def toString(): String =
    "[" + code + "]" +
    "(" + getClass.getName + ")"
    "@" + Integer.toHexString(rd)
}

/**
 * D-Codes take Double parameters
 */
class DCode(code: Int, rd: Address, r1: Address, r2: Address)
extends Opcode(code, rd) {
 
  override def toString(): String =
    super.toString() + 
    ", " + Integer.toHexString(r1) +
    ", " + Integer.toHexString(r2)
}

object DCode {
 
  /**
   * List of all D-Type opcodes used when translating to/from binary
   */
  def all = Add :: Sub :: Mult :: Div :: Output :: Phi :: Nil

}

/**
 * S-Codes take a Single parameter
 */
class SCode(code: Int, rd: Address, r1: Address)
extends Opcode(code, rd) {
 
  override def toString(): String =
    super.toString() +
    ", " + Integer.toHexString(r1)
}

object SCode {
  
  /**
   * List of all S-Type opcodes used when translating to/from binary
   */
  def all = Noop :: Cmpz :: Sqrt :: Copy :: Input :: Nil
  
}

/**
 * rd < - mem[r1] + mem[r2]
 */
case class Add(rd: Address, r1: Address, r2: Address)
extends DCode(0x1, rd, r1, r2)

/**
 * rd < - mem[r1] - mem[r2]
 */
case class Sub(rd: Address, r1: Address, r2: Address)
extends DCode(0x2, rd, r1, r2)

/**
 * rd < - mem[r1] * mem[r2]
 */
case class Mult(rd: Address, r1: Address, r2: Address)
extends DCode(0x3, rd, r1, r2)


/**
 * rd < - 0.0 if mem[r2] == 0.0 else mem[r1] / mem[r2]
 */
case class Div(rd: Address, r1: Address, r2: Address)
extends DCode(0x4, rd, r1, r2)

/**
 * port[r1] < - mem[r2]
 */
case class Output(rd: Address, r1: Address, r2: Address)
extends DCode(0x5, rd, r1, r2)

/**p
 * rd < - mem[r1] if status == 1, else mem[r2]
 */
case class Phi(rd: Address, r1: Address, r2: Address)
extends DCode(0x6, rd, r1, r2)


/**
 * rd < - mem[rd]
 */
case class Noop(rd: Address, r1: Address)
extends SCode(0x0, rd, r1)


/**
 * status < - mem[r1] op 0.0
 */
case class Cmpz(rd: Address, op: ComparisonOperation, r1: Address)
extends SCode(0x1, rd, r1) {
 
  override def toString(): String =
   super.toString() +
   " " + op.toString() + " 0.0"
  
}

/**
 * status < - abs(sqrt(mem[r1]))
 */
case class Sqrt(code: Int, rd: Address, r1: Address)
extends SCode(0x2, rd, r1)


/**
 * rd < - mem[r1]
 */
case class Copy(code: int, rd: Address, r1: Address)
extends SCode(0x3, rd, r1)

/**
 * rd < - port[r1]
 */
case class Input(rd: Address, r1: Address)
extends SCode(0x4, rd, r1)

/**
 * Comparison operators used for the cmpz instruction
 */
class ComparisonOperation(code: Int)

object ComparisonOperator {
 
  /**
   * List of all comparison operations, used for translating to/from binary
   */
  val all = Ltz :: Lez :: Eqz :: Gez :: Gtz :: Nil
}

/**
 * Less-than zero
 */
case class Ltz
extends ComparisonOperation(0x0) {
 
  override def toString(): String = "<"
}

/**
 * Less-than-or-equal-to zero
 */
case class Lez
extends ComparisonOperation(0x1) {
 
  override def toString(): String = "<="
}

/**
 * Equal to zero
 */
case class Eqz
extends ComparisonOperation(0x2) {
 
  override def toString(): String = "="
}

/**
 * Greater-than-or-equal-to zero
 */
case class Gez
extends ComparisonOperation(0x3) {
 
  override def toString(): String = ">="
}

/**
 * Greater than zero
 */
case class Gtz
extends ComparisonOperation(0x4) {
 
  override def toString(): String = ">"
}
