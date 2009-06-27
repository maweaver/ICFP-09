package com.icfp.gui

import scala.swing.TextField
import scala.swing.event.ValueChanged
import vm.Vm

class TargetStepInput(vm: Vm, guiState: GuiState)
extends TextField {
  
  columns = 5
  text = guiState.targetStep.toString()
  
  reactions += {
    case ValueChanged(_) =>
      if(text != "") {
        try {
          guiState.targetStep = text.toInt
        } catch { 
          case _ => text = guiState.targetStep.toString()
        }
      } else {
        guiState.targetStep = 0
      }
  }
  
}
