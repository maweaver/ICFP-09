package com.icfp.problems

import java.awt.{Color, Graphics}
import javax.swing.table.{AbstractTableModel}
import scala.swing.{Component, Orientation, Panel, ScrollPane, SplitPane, Table}
import vm.Vm

/**
 * Info about a step in the Hohmann problem
 */
case class HohmannInfo(stepNum: Int, score: Double, fuel: Double, sx: Double, sy: Double, radius: Double, targetRadius: Double)

/**
 * Problem 1, Hohmann, described in section 6.1
 */
object Hohmann
extends Problem {
  
  /**
   * Info on the steps that have been executed
   */
  private var info: Seq[HohmannInfo] = Nil

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
  override def control(stepNum: Int) {
  }
  
  /**
   * @inheritDoc
   */
  override def observe(stepNum: Int) {
    info = info ++ List(
      HohmannInfo(stepNum, 
        vm.outputPorts.getOrElse(0x0, 0.0d), // score
        vm.outputPorts.getOrElse(0x1, 0.0d), // fuel
        vm.outputPorts.getOrElse(0x2, 0.0d), // sx
        vm.outputPorts.getOrElse(0x3, 0.0d), // sy
        Math.sqrt(Math.pow(vm.outputPorts.getOrElse(0x2, 0.0d), 2.0d) + Math.pow(vm.outputPorts.getOrElse(0x3, 0.0d), 2.0d)),
        vm.outputPorts.getOrElse(0x4, 0.0d))) //targetRadius
        
    dataTable.model.asInstanceOf[AbstractTableModel].fireTableDataChanged()
  }
  
  val dataTable  = new Table {
    val StepNumColumn = 0
    val ScoreColumn = 1
    val FuelColumn = 2
    val SxColumn = 3
    val SyColumn = 4
    val RadiusColumn = 5
    val TargetRadiusColumn = 6
      
    model = new AbstractTableModel {
      
      override def getColumnName(col: Int): String = col match {
        case StepNumColumn => "Step Number"
        case ScoreColumn => "Score"
        case FuelColumn => "Fuel"
        case SxColumn => "Sx"
        case SyColumn => "Sy"
        case RadiusColumn => "Radius"
        case TargetRadiusColumn => "Target Radius"
      }
      
      override def getRowCount(): Int = info.length
     
      override def getColumnCount(): Int = 7
      
      override def getValueAt(row: Int, col: Int): Object = {
        val currInfo = info.drop(row).first
        col match {
          case StepNumColumn => currInfo.stepNum.toString()
          case ScoreColumn => currInfo.score.toString()
          case FuelColumn => currInfo.fuel.toString()
          case SxColumn => currInfo.sx.toString()
          case SyColumn => currInfo.sy.toString()
          case RadiusColumn => currInfo.radius.toString()
          case TargetRadiusColumn => currInfo.targetRadius.toString()
          case _ => ""
        }
      }
    }
  }
  
  /**
   * @inheritDoc
   */
  def visualizer: Component = new ScrollPane(dataTable)
  
  override def reset() {
    super.reset()
    info = Nil
  }
}
