package com.icfp.vm

import scala.collection.mutable.{Map, Message}
import scala.swing.{Publisher}
import scala.swing.event.{Event}

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

/**
 * Event dispatched when the VM executes an instruction
 */
case class InstructionExecuted(vm: Vm)
extends Event

/**
 * Event dispatched when the VM is initialized
 */
case class VmInitialized(vm: Vm)
extends Event

class Vm 
extends Publisher{
  
  /**
   * The instruction space.  This space is stored as a map, rather than a list,
   * because it is so huge, but likely sparsely populated.
   */
  val instructions = Map[Vm.Address, Opcode]()
  
  /**
   * The number of instructions.  This is the highest address where an 
   * instruction has been set.
   */
  def numInstructions = instructions.keys.foldLeft(0) { (a, b) => Math.max(a, b) }
  
  /**
   * The data space.  This space is stored as a map, rather than a list,
   * because it is so huge, but likely sparsely populated.
   */
  val data = Map[Vm.Address, Vm.Data]()
  
  /**
   * The number of data elements.  This is the highest address where data
   * has been set.
   */
  def numData = data.keys.foldLeft(0) { (a, b) => Math.max(a, b) }
  
  /**
   * Ports that provide data to the VM
   */
  val inputPorts = Map[Vm.Address, Vm.Data]()
  
  /**
   * The number of input ports.  This is the highest address where an input
   * port has been set.
   */
  def numInputPorts = inputPorts.keys.foldLeft(0) { (a, b) => Math.max(a, b) }
  
  /**
   * Ports by which the data interacts with the outside world
   */
  val outputPorts = Map[Vm.Address, Vm.Data]()
  
  /**
   * The number of output ports.  This is the highest address where an 
   * output port's value has been set.
   */
  def numOutputPorts = outputPorts.keys.foldLeft(0) { (a, b) => Math.max(a, b) }
  
  /**
   * Status flag.
   */
  var status = false
  
  /**
   * The number of the current step.  Each step is an execution through each
   * instruction in the VM's address space.
   */
  var currentStep = 0
  
  /**
   * The address of the next instruction to be executed
   */
  var currentAddress = 0
  
  /**
   * Executes the next instruction, and publishes an update method
   */
  def nextInstruction() {
    val instruction = instructions.getOrElse(currentAddress, Noop(currentAddress, currentAddress))
    instruction.execute(this)
    currentAddress += 1
    publish(InstructionExecuted(this))
  }
}
