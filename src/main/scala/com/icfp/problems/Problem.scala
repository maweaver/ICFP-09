package com.icfp.problems

import scala.swing.{Component}
import vm.{Vm, VmReader, StepFinished}

object Problem {
  
  /**
   * All available problems
   */
  def all: Seq[Problem] = Hohmann :: MeetAndGreet :: EccentricMeetAndGreet :: ClearSkies :: Nil
  
}

/**
 * Abstract class for problems
 */
abstract class Problem {

  private var _vm: Option[Vm] = None
  
  /**
   * Retrieve the current vm
   */
  def vm: Vm = _vm.get
  
  /**
   * Called when the current VM is set
   */
  def vm_=(value: Vm) { 
    if(_vm.isEmpty) {
      _vm = Some(value)
      vm.reactions += {
        case StepFinished(_) =>
          observe(vm.currentStep)
          control(vm.currentStep)
      }
      control(vm.currentStep)
    } else {
      _vm = Some(value) 
    }
  }

  /**
   * The name of the problem
   */
  def name: String
  
  /**
   * The problem configurations
   */
  def configurations: Seq[Int]
  
  /**
   * The name of the binary for this problem
   */
  def binary: String
  
  /**
   * GUI used to visualize the problem
   */
  def visualizer: Component
  
  /**
   * Control the machine for this step
   */
  def control(stepNumber: Int)
  
  /**
   * Make any observations about the current step
   */
  def observe(stepNumber: Int)
  
  /**
   * Resets the VM to work the current problem
   */
  def reset() {
    VmReader.populateVm(vm, Thread.currentThread.getContextClassLoader.getResourceAsStream("binaries/" + binary + ".obf"))
     control(vm.currentStep)
  }
  
  /**
   * @inheritDoc
   */
  override def toString(): String = name
}
