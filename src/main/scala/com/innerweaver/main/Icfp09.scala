package com.innerweaver.main

import vm.VmReader
import util.Bits

object Icfp09 
extends Application {
  
  for(frame <- new VmReader(Thread.currentThread.getContextClassLoader.getResourceAsStream("binaries/bin1.obf"))) {
  }
  
}
