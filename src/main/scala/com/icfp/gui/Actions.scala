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
