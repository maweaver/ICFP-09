package com.icfp.gui

import javax.swing.JToolBar
import scala.swing.{Component, ScrollPane, TabbedPane}
import scala.swing.TabbedPane.Page
import vm.Vm

/**
 * Control used to debug the VM
 */
class VmDebugger(vm: Vm)
extends MigPanel("", "[100%]", "[100%]") {
 
  val toolbar = new JToolBar()
  
  toolbar.add(new NextInstructionAction(vm).peer)

  add(Component.wrap(toolbar), "dock north")

  add(new TabbedPane {
    
    pages += new Page("Data", new ScrollPane(new DataList(vm) {
      def dataMap = vm.data
      def maxValue = vm.numData
    }))
    
    pages += new Page("Input Ports", new ScrollPane(new DataList(vm) {
      def dataMap = vm.inputPorts
      def maxValue = vm.numInputPorts
    }))
    
    pages += new Page("Output Ports", new ScrollPane(new DataList(vm) {
      def dataMap = vm.outputPorts
      def maxValue = vm.numOutputPorts
    }))
    
  }, "dock west, width 650")
  
  add(new ScrollPane(new CommandList(vm)), "dock south, height 300")
  
}
