package com.icfp.gui

import javax.swing.ImageIcon
import scala.swing.Action
import vm.Vm

/**
 * Action used to execute the next instruction on the VM.
 */
class NextInstructionAction(vm: Vm)
extends Action("Next Instruction") {
  
  icon = new ImageIcon(Thread.currentThread.getContextClassLoader.getResource("img/next-instruction.png"))
  toolTip = "Executes the next instruction"
  
  override def apply(): Unit = vm.nextInstruction()
  
}

/**
 * Action used to execute instructions sequentially until the current step is 
 * finished.
 */
class FinishStepAction(vm: Vm)
extends Action("Finish Step") {
  
  icon = new ImageIcon(Thread.currentThread.getContextClassLoader.getResource("img/next-step.png"))
  toolTip = "Executes instructions until all have been executed"
  
  override def apply(): Unit = vm.finishStep()
  
}
 
