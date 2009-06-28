package com.icfp.gui

import scala.swing.TextField
import scala.swing.event.ValueChanged
import vm.Vm

class StepIncrement(vm: Vm, guiState: GuiState)
extends TextField {
  
  columns = 5
  text = guiState.stepIncrement.toString()
  
  reactions += {
    case ValueChanged(_) =>
      if(text != "") {
        try {
          guiState.stepIncrement = text.toInt
        } catch { 
          case _ => text = guiState.stepIncrement.toString()
        }
      } else {
        guiState.stepIncrement = 0
      }
  }
  
}
