package com.icfp.gui

import java.awt.Color
import javax.swing.JToolBar
import javax.swing.JToolBar.Separator
import scala.swing.{Component, FlowPanel, Label, ScrollPane, TabbedPane}
import scala.swing.TabbedPane.Page
import scala.swing.event.SelectionChanged
import problems.Problem
import vm.Vm

/**
 * Control used to debug the VM
 */
class VmDebugger(vm: Vm)
extends MigPanel("", "[100%]", "[100%]") {
  
  val guiState = new GuiState(this)
  
  guiState.reactions += {
    case ProblemChanged(p) => 
      p.reset()
      resetConfigurations()
      visualizationHolder.peer.removeAll()
      visualizationHolder.add(p.visualizer, "growx, growy")
  }
  
  // Initialize each problem's vms here... hackish, but oh well
  Problem.all.foreach { p => p.vm = vm }
  
  val problemList = new ProblemList(vm, guiState)
  
  /**
   * Panel used to hold the list of valid configurations.  Used because a new
   * control has to be created each time the problem changes, since the
   * combobox values are immutable
   */
  val configurationHolder = new FlowPanel
  
  val toolbar = new JToolBar()
  
  toolbar.add(new TraceAction(vm, guiState).peer)
  toolbar.add(new NextInstructionAction(vm, guiState).peer)
  toolbar.add(new FinishStepAction(vm, guiState).peer)
  toolbar.add(new JToolBar.Separator())
  toolbar.add(new ResetAction(vm, guiState).peer)
  toolbar.add(new FlowPanel {
    contents += problemList
    contents += configurationHolder
  }.peer)
  
  toolbar.add(new FlowPanel {
    contents += new Label("Step Increment:")
    contents += new StepIncrement(vm, guiState)
  }.peer)
  toolbar.add(new RunAction(vm, guiState).peer)
  
  add(Component.wrap(toolbar), "dock north")

  add(new TabbedPane {
    
    pages += new Page("Data", new ScrollPane(new DataList(vm) {
      def data = vm.data
      def maxValue = vm.numData
    }))
    
    pages += new Page("Input Ports", new ScrollPane(new DataList(vm) {
      def data = vm.inputPorts
      def maxValue = vm.numInputPorts
    }))
    
    pages += new Page("Output Ports", new ScrollPane(new DataList(vm) {
      def data = vm.outputPorts
      def maxValue = vm.numOutputPorts
    }))
    
  }, "dock west, width 650")
  
  val visualizationHolder = new MigPanel("", "[100%]", "[100%]") {
    add(Problem.all.first.visualizer, "growx, growy")
  }
  
  add(visualizationHolder, "growx, growy")
  
  add(new ScrollPane(new CommandList(vm)), "dock south, height 300")
  
  resetConfigurations()
  
  /**
   * Retrieves the current problem
   */
  def currentProblem: Problem = problemList.selection.item
  
  /**
   * Creates a new list of configurations based on the currently-selected
   * problem.
   */
  def resetConfigurations() {
    configurationHolder.contents.clear
    configurationHolder.contents += new ConfigurationList(vm, currentProblem)
  }
  
}
