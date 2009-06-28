package com.icfp.gui

import scala.swing.ComboBox
import scala.swing.event.SelectionChanged
import problems.Problem
import vm.Vm

/**
 * Drop-down for selecting the current configuration.  Available values depend
 * on the problem
 */
class ConfigurationList(vm: Vm, problem: Problem) 
extends ComboBox(problem.configurations) {

  selection.reactions += {
    case SelectionChanged(_) => {
      problem.reset()
      setConfiguration()
    }
  }
  
  setConfiguration()

  /**
   * Updates the configuration port based on the selected configuration
   */
  def setConfiguration() {
    vm.inputPorts(0x3E80) = selection.item.toDouble
    vm.scenarioId = selection.item
  }
  
}
