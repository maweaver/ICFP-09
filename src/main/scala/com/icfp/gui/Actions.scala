package com.icfp.gui

import java.io.FileOutputStream
import javax.swing.ImageIcon
import scala.swing.{Action, FileChooser}
import vm.Vm

/**
 * Action used to execute the next instruction on the VM.
 */
class NextInstructionAction(vm: Vm, guiState: GuiState)
extends Action("Next Instruction") {
  
  icon = new ImageIcon(Thread.currentThread.getContextClassLoader.getResource("img/next-instruction.png"))
  toolTip = "Executes the next instruction"
  
  override def apply(): Unit = vm.nextInstruction()
  
}

/**
 * Action used to execute instructions sequentially until the current step is 
 * finished.
 */
class FinishStepAction(vm: Vm, guiState: GuiState)
extends Action("Finish Step") {
  
  icon = new ImageIcon(Thread.currentThread.getContextClassLoader.getResource("img/next-step.png"))
  toolTip = "Executes instructions until all have been executed"
  
  override def apply(): Unit = vm.finishStep()
  
} 

/**
 * Action used to reset the current problem
 */
class ResetAction(vm: Vm, guiState: GuiState)
extends Action("Reset") {
  
  icon = new ImageIcon(Thread.currentThread.getContextClassLoader.getResource("img/reload.png"))
  toolTip = "Resets the current problem"
  
  override def apply() {
    guiState.currentProblem = guiState.currentProblem
  }
  
} 

/**
 * Action used to run the current problem
 */
class RunAction(vm: Vm, guiState: GuiState)
extends Action("Run") {
  
  icon = new ImageIcon(Thread.currentThread.getContextClassLoader.getResource("img/go.png"))
  toolTip = "Runs the current problem until the target step is reached"
  
  override def apply() {
    for(i <- 0 until guiState.stepIncrement) 
      vm.finishStep()
  }
  
} 

/**
 * Action used to export the current trace
 */
class TraceAction(vm: Vm, guiState: GuiState)
extends Action("Export Trace") {

  icon = new ImageIcon(Thread.currentThread.getContextClassLoader.getResource("img/trace.png"))
  toolTip = "Export a trace file"
  
  override def apply() {
    val chooser = new FileChooser()
    
    if(chooser.showSaveDialog(guiState.component) == FileChooser.Result.Approve) {
      val data = vm.trace.dump()
      val output = new FileOutputStream(chooser.selectedFile)
      output.write(data)
      output.close()
    }
  }
  
}
