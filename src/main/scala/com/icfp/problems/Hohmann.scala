package com.icfp.problems

import scala.swing.Panel
import vm.Vm

/**
 * Problem 1, Hohmann, described in section 6.1
 */
object Hohmann
extends Problem {

  /**
   * @inheritDoc
   */
  override def setVm(value: Vm) {
  }

  /**
   * @inheritDoc
   */
  override def name = "Hohmann"
  
  /**
   * @inheritDoc
   */
  override def configurations = 1001 :: 1002 :: 1003 :: 1004 ::  Nil
  
  /**
   * @inheritDoc
   */
  override def binary = "bin1"
  
  
  /**
   * @inheritDoc
   */
  def visualizer: Panel = new Panel {
     
  }
}
