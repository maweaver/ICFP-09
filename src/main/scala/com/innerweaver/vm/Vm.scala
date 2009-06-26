package com.innerweaver.vm

object Vm {
  /**
  * Used to address ports, instructions, data, etc.  Technically this is only
  * a 14-bit value, but we use an Int here.  In the future it might be good to
  * extend int and do bounds-checking?
  */
  type Address = Int

  /**
  * Actual data value.  Thankfully, Java's double matches the 64-bit IEEE-754
  * format used by the Orbital VM.
  */
  type Data = double
  
}
