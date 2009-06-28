package com.icfp.problems

import java.awt.{Color, Graphics, Point}
import javax.swing.{JTable}
import javax.swing.table.{AbstractTableModel}
import scala.swing.{Component, Orientation, Panel, ScrollPane, SplitPane, Table}
import gui.MigPanel
import util.{GraphicsUtil, Physics}
import vm.Vm

/**
 * Info about a target in the clear skies problem
 */
case class TargetInfo(sx: Double, sy: Double, collected: Double)

/**
 * Info about a step in the Clear Skies problem
 */
case class ClearSkiesInfo(stepNum: Int, score: Double, fuel: Double, sxEarth: Double, syEarth: Double, sxFuel: Double, syFuel: Double, fuelerFuel: Double, targets: List[TargetInfo])

/**
 * Problem 1, Hohmann, described in section 6.1
 */
object ClearSkies
extends Problem {
  
  /**
   * Info on the steps that have been executed
   */
  private var info: List[ClearSkiesInfo] = Nil

  /**
   * @inheritDoc
   */
  override def name = "Clear Skies"
  
  /**
   * @inheritDoc
   */
  override def configurations = 4001 :: 4002 :: 4003 :: 4004 ::  Nil
  
  /**
   * @inheritDoc
   */
  override def binary = "bin4"

  /**
   * @inheritDoc
   */
  override def control(stepNum: Int) {
  }
  
  /**
   * @inheritDoc
   */
  override def observe(stepNum: Int) {
    var targets: List[TargetInfo] = Nil
    
    for(k <- 0 until 12)
      targets ::= TargetInfo(vm.outputPorts(3 * k + 0x7), vm.outputPorts(3 * k + 0x8), vm.outputPorts(3 * k + 0x9))
    
    info ::= ClearSkiesInfo(stepNum, 
      vm.outputPorts(0x0), // score
      vm.outputPorts(0x1), // fuel
      vm.outputPorts(0x2), // sx earth
      vm.outputPorts(0x3), // sy earth
      vm.outputPorts(0x4), // sx fuel
      vm.outputPorts(0x5), // sy fuel
      vm.outputPorts(0x6), // fueler fuel left
      targets)
        
    dataTable.model.asInstanceOf[AbstractTableModel].fireTableDataChanged()
    graphicsPanel.peer.repaint(0, 0, 0, graphicsPanel.size.getWidth.toInt, graphicsPanel.size.getHeight.toInt)
  }
  
  val dataTable = new Table {
      val StepNumColumn = 0
      val ScoreColumn = 1
      val FuelColumn = 2
      val SxEarthColumn = 3
      val SyEarthColumn = 4
      val SxFuelColumn = 5
      val SyFuelColumn = 6
      val FuelerFuelColumn = 7
      val TargetInfoStartColumn = 8
      
      peer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF)
      
      model = new AbstractTableModel {
      
        override def getColumnName(col: Int): String = col match {
          case StepNumColumn => "Step Number"
          case ScoreColumn => "Score"
          case FuelColumn => "Fuel"
          case SxEarthColumn => "Sx Earth"
          case SyEarthColumn => "Sy Earth"
          case SxFuelColumn => "Sx Fuel"
          case SyFuelColumn => "Sy Fuel"
          case FuelerFuelColumn => "Fueler Fuel"
          case x => 
            if(x % 0x7 == 0) 
              "Sx " + ((x - 8) / 3)
            else if(x % 8 == 0)
              "Sy " + ((x - 8) / 3)
            else
              "Found " + ((x - 8) / 3)
        }
      
        override def getRowCount(): Int = info.length
     
        override def getColumnCount(): Int = 44
      
        override def getValueAt(row: Int, col: Int): Object = {
          val currInfo = info.drop(row).first
          col match {
            case StepNumColumn => currInfo.stepNum.toString()
            case ScoreColumn => currInfo.score.toString()
            case FuelColumn => currInfo.fuel.toString()
            case SxEarthColumn => currInfo.sxEarth.toString()
            case SyEarthColumn => currInfo.syEarth.toString()
            case SxFuelColumn => currInfo.sxFuel.toString()
            case SyFuelColumn => currInfo.syFuel.toString()
            case FuelerFuelColumn => currInfo.fuelerFuel.toString()
            case _ => {
              val targetNum = (col - 8) / 3
              val target = currInfo.targets(targetNum)
              if(col % 7 == 0)
                return target.sx.toString()
              else if(col % 8 == 0)
                return target.sy.toString()
              else
                return target.collected.toString()
            }
          }
        }
      }
    }
    
  val graphicsPanel = new Panel {
      override def paintComponent(g: Graphics) {
        
        val maxRadius = 8.0d * Physics.Re
         
        g.setColor(Color.BLACK)
        g.fillRect(0, 0, size.getWidth.toInt, size.getHeight.toInt)
        
        GraphicsUtil.drawRadius(g, size, maxRadius, Physics.Re, Color.GREEN, true)
        
        if(!info.isEmpty) {
          val lastInfo = info.first
          
          GraphicsUtil.drawPoint(g, size, maxRadius, 0 - lastInfo.sxEarth + lastInfo.sxFuel, 0 - lastInfo.sxEarth + lastInfo.syFuel, Color.BLUE)
          
          for(target <- lastInfo.targets) {
            if(target.collected == 0.0d)
              GraphicsUtil.drawPoint(g, size, maxRadius, 0 - lastInfo.sxEarth + target.sx, 0 - lastInfo.syEarth + target.sy, Color.WHITE)
            else
              GraphicsUtil.drawPoint(g, size, maxRadius, 0 - lastInfo.sxEarth + target.sx, 0 - lastInfo.syEarth + target.sy, Color.YELLOW)
          }
          GraphicsUtil.drawPoint(g, size, maxRadius, 0 - lastInfo.sxEarth, 0 - lastInfo.syEarth, Color.RED)
        }
     }
   }
  
  val _visualizer  = new MigPanel("", "[100%]", "[50%][50%]") {
    add(graphicsPanel, "growx, growy, wrap")
    add(new ScrollPane(dataTable), "growx, growy")
  }
  
  /**
   * @inheritDoc
   */
  def visualizer: Component = _visualizer
  
  override def reset() {
    super.reset()
    info = Nil
  }
}
