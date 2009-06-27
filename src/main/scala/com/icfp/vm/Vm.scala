package com.icfp.vm

import scala.collection.mutable.{Map, ObservableMap}

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

class Vm {
  
  /**
   * The instruction space.  This space is stored as a map, rather than a list,
   * because it is so huge, but likely sparsely populated.
   */
  val instructions = Map[Vm.Address, Opcode]()
  
  
  /**
   * The data space.  This space is stored as a map, rather than a list,
   * because it is so huge, but likely sparsely populated.
   */
  val data = Map[Vm.Address, Vm.Data]()
  
  /**
   * Ports that provide data to the VM
   */
  val inputPorts = Map[Vm.Address, Vm.Data]()
  
  
  /**
   * Ports by which the data interacts with the outside world
   */
  val outputPorts = Map[Vm.Address, Vm.Data]()
  
  /**
   * Status flag.
   */
  var status = false
}
