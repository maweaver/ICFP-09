package com.icfp.gui

import scala.swing.ComboBox
import scala.swing.event.SelectionChanged
import problems.Problem
import vm.Vm

/**
 * GUI control to select the current problem
 */
class ProblemList(vm: Vm, guiState: GuiState)
extends ComboBox[Problem](Problem.all) {
  
  selection.reactions += {
    case SelectionChanged(_) => guiState.currentProblem = selection.item
  }
  
}
