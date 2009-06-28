package com.icfp.vm

import scala.collection.mutable.Map

/**
 * Info on what occurred during a given step
 */
case class StepInfo(stepNum: Int, changedInputs: Map[Vm.Address, Vm.Data])

/**
 * Class storing a stack trace for the vm
 */
class Trace(vm: Vm) {
  
  var history: List[StepInfo] = Nil
  
  var lastInputs = Array.make(Vm.MaxAddr, 0.0d)
  
  val teamId = 87
  
  def dump(): Array[Byte] = {
    println("Team id: " + teamId)
    println("Scenario id: " + vm.scenarioId)
    for(historyItem <- history.reverse) {
      println("Delta for step " + historyItem.stepNum)
      println("Number of changes: " + historyItem.changedInputs.keySet.size)
      for(key <- historyItem.changedInputs.keys)
        println("Input at address 0x" + Integer.toHexString(key) + " changed to " + historyItem.changedInputs(key))
    }
    
    new Array[Byte](0)
  }
  
  def processStep() {
    val changedInputs = Map[Vm.Address, Vm.Data]()
    lastInputs.zip(vm.inputPorts).zipWithIndex.filter { (a) => a._1._1 != a._1._2 }.foreach { (a) => changedInputs += a._2 -> a._1._2 }
    
    lastInputs = Array.make(Vm.MaxAddr, 0.0d)
    Array.copy(vm.inputPorts, 0, lastInputs, 0, Vm.MaxAddr)

    if(changedInputs.keySet.size > 0)    
      history ::= StepInfo(vm.currentStep, changedInputs)
  }
  
  def reset() {
    lastInputs = Array.make(Vm.MaxAddr, 0.0d)
  }
}
