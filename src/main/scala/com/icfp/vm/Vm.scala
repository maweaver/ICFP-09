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
  
  lazy val MaxAddr = Math.pow(2.0d, 14).toInt
  
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

/**
 * Event dispatched when a step is complete
 */
case class StepFinished(vm: Vm)
extends Event

class Vm 
extends Publisher{
  
  /**
   * The instruction space.  This space is stored as a map, rather than a list,
   * because it is so huge, but likely sparsely populated.
   */
  var instructions: Array[Opcode] = Array.make(Vm.MaxAddr, Eof())
  
  /**
   * The number of instructions.  This is the highest address where an 
   * instruction has been set.
   */
  var numInstructions = Vm.MaxAddr
  
  /**
   * The data space.  This space is stored as a map, rather than a list,
   * because it is so huge, but likely sparsely populated.
   */
  var data = Array.make(Vm.MaxAddr, 0.0d)
  
  /**
   * The number of data elements.  This is the highest address where data
   * has been set.
   */
  def numData = Vm.MaxAddr
  
  /**
   * Ports that provide data to the VM
   */
  var inputPorts = Array.make(Vm.MaxAddr, 0.0d)
  
  /**
   * The number of input ports.  This is hardcoded at 0x3E81 for now, since
   * port number 0x3E80 is the highest address used
   */
  def numInputPorts = Vm.MaxAddr
  
  /**
   * Ports by which the data interacts with the outside world
   */
  var outputPorts = Array.make(Vm.MaxAddr, 0.0d)
  
  /**
   * The number of output ports.  This is the highest address where an 
   * output port's value has been set.
   */
  def numOutputPorts = Vm.MaxAddr
  
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
   * Resets the current VM
   */
  def reset() {
    instructions = Array.make(Vm.MaxAddr, Eof())
    data = Array.make(Vm.MaxAddr, 0.0d)
    inputPorts = Array.make(Vm.MaxAddr, 0.0d)
    outputPorts = Array.make(Vm.MaxAddr, 0.0d)
    currentAddress = 0
    currentStep = 0
  }
  
  /**
   * Executes the next instruction, and publishes an update method
   */
  def nextInstruction() {
    val instruction = instructions(currentAddress)
    instruction match {
      case Eof() =>
      case _ =>
        instruction.execute(this)
        currentAddress += 1
        publish(InstructionExecuted(this))
    }
  }
  
  /**
   * Execute until the end of the step is reached
   */
  def finishStep() {
    while(currentAddress < numInstructions)
      nextInstruction()
      
    currentAddress = 0
    currentStep += 1
    publish(StepFinished(this))
  }
}
