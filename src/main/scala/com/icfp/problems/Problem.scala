package com.icfp.problems

import scala.swing.{Panel}
import vm.{Vm, VmReader}

object Problem {
  
  /**
   * All available problems
   */
  def all: Seq[Problem] = Hohmann :: Nil
  
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
  def vm_=(value: Vm) { _vm = Some(value) }

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
  def visualizer: Panel
  
  /**
   * Resets the VM to work the current problem
   */
  def reset() {
     VmReader.populateVm(vm, Thread.currentThread.getContextClassLoader.getResourceAsStream("binaries/" + binary + ".obf"))
  }
  
  /**
   * @inheritDoc
   */
  override def toString(): String = name
}
