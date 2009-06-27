package com.icfp.gui

import scala.swing.ComboBox
import problems.Problem
import vm.Vm

/**
 * GUI control to select the current problem
 */
class ProblemList(vm: Vm)
extends ComboBox[Problem](Problem.all) {

}
