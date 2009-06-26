package com.innerweaver.main

import vm.VmReader

object Icfp09 
extends Application {
  
  for(frame <- new VmReader(Thread.currentThread.getContextClassLoader.getResourceAsStream("binaries/bin1.obf"))) {
  }
  
}
