package com.icfp.main

import scala.swing.{MainFrame, SimpleGUIApplication}
import gui.VmDebugger
import vm.{Vm, VmReader}
import util.Bits

object Icfp09 
extends SimpleGUIApplication {

  val vm = new Vm()
  VmReader.populateVm(vm, Thread.currentThread.getContextClassLoader.getResourceAsStream("binaries/bin1.obf"))

  def top = new MainFrame {
    
    title = "Orbit VM Debugger"
    
    preferredSize = (1000, 700)
    
    contents = new VmDebugger(vm)
  }
}
