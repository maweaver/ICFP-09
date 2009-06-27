package com.icfp.gui

import scala.swing.{ScrollPane, TabbedPane}
import scala.swing.TabbedPane.Page
import vm.Vm

/**
 * Control used to debug the VM
 */
class VmDebugger(vm: Vm)
extends MigPanel("", "[100%]", "[100%]") {
 
  add(new TabbedPane {
    
    pages += new Page("Data", new ScrollPane(new DataList {
      def dataMap = vm.data
    }))
    
    pages += new Page("Input Ports", new ScrollPane(new DataList {
      def dataMap = vm.inputPorts
    }))
    
    pages += new Page("Output Ports", new ScrollPane(new DataList {
      def dataMap = vm.outputPorts
    }))
    
  }, "dock west, width 650")
  
  add(new ScrollPane(new CommandList(vm)), "dock south, height 300")
  
}
