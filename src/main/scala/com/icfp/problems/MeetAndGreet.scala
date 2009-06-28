package com.icfp.problems

import java.awt.{Color, Graphics, Point}
import javax.swing.table.{AbstractTableModel}
import scala.swing.{Component, Orientation, Panel, ScrollPane, SplitPane, Table}
import gui.MigPanel
import util.{GraphicsUtil, Physics}
import vm.Vm

/**
 * Info about a step in the Meet and Greet Problem
 */
case class MeetAndGreetInfo(stepNum: Int, score: Double, fuel: Double, sxEarth: Double, syEarth: Double, sxTarget: Double, syTarget: Double, radius: Double)

/**
 * Problem 1, Hohmann, described in section 6.1
 */
object MeetAndGreet
extends Problem {
  
  /**
   * Info on the steps that have been executed
   */
  private var info: List[MeetAndGreetInfo] = Nil

  /**
   * @inheritDoc
   */
  override def name = "Meet and Greet"
  
  /**
   * @inheritDoc
   */
  override def configurations = 2001 :: 2002 :: 2003 :: 2004 ::  Nil
  
  /**
   * @inheritDoc
   */
  override def binary = "bin2"

  /**
   * @inheritDoc
   */
  override def control(stepNum: Int) {
  }
  
  /**
   * @inheritDoc
   */
  override def observe(stepNum: Int) {
    info ::= MeetAndGreetInfo(stepNum, 
      vm.outputPorts(0x0), // score
      vm.outputPorts(0x1), // fuel
      vm.outputPorts(0x2), // sx earth
      vm.outputPorts(0x3), // sy earth
      vm.outputPorts(0x4), // sx target
      vm.outputPorts(0x5), // sy target
      Math.sqrt(Math.pow(vm.outputPorts(0x2), 2.0d) + Math.pow(vm.outputPorts(0x3), 2.0d))) // radius
        
    dataTable.model.asInstanceOf[AbstractTableModel].fireTableDataChanged()
    graphicsPanel.peer.repaint(0, 0, 0, graphicsPanel.size.getWidth.toInt, graphicsPanel.size.getHeight.toInt)
  }
  
  val dataTable = new Table {
      val StepNumColumn = 0
      val ScoreColumn = 1
      val FuelColumn = 2
      val SxEarthColumn = 3
      val SyEarthColumn = 4
      val SxTargetColumn = 5
      val SyTargetColumn = 6
      val RadiusColumn = 7
      
      model = new AbstractTableModel {
      
        override def getColumnName(col: Int): String = col match {
          case StepNumColumn => "Step Number"
          case ScoreColumn => "Score"
          case FuelColumn => "Fuel"
          case SxEarthColumn => "Sx Earth"
          case SyEarthColumn => "Sy Earth"
          case SxTargetColumn => "Sx Target"
          case SyTargetColumn => "Sy Target"
          case RadiusColumn => "Radius"
        }
      
        override def getRowCount(): Int = info.length
     
        override def getColumnCount(): Int = 8
      
        override def getValueAt(row: Int, col: Int): Object = {
          val currInfo = info.drop(row).first
          col match {
            case StepNumColumn => currInfo.stepNum.toString()
            case ScoreColumn => currInfo.score.toString()
            case FuelColumn => currInfo.fuel.toString()
            case SxEarthColumn => currInfo.sxEarth.toString()
            case SyEarthColumn => currInfo.syEarth.toString()
            case SxTargetColumn => currInfo.sxTarget.toString()
            case SyTargetColumn => currInfo.syTarget.toString()
            case RadiusColumn => currInfo.radius.toString()
            case _ => ""
          }
        }
      }
    }
    
  val graphicsPanel = new Panel {
      override def paintComponent(g: Graphics) {
        
        val maxRadius = 6.0d * Physics.Re
         
        g.setColor(Color.BLACK)
        g.fillRect(0, 0, size.getWidth.toInt, size.getHeight.toInt)
        
        GraphicsUtil.drawRadius(g, size, maxRadius, Physics.Re, Color.GREEN, true)
        
        if(!info.isEmpty) {
          val lastInfo = info.first
          
          GraphicsUtil.drawRadius(g, size, maxRadius, lastInfo.radius, Color.WHITE, false)
          GraphicsUtil.drawPoint(g, size, maxRadius, 0 - lastInfo.sxEarth, 0 - lastInfo.syEarth, Color.RED)
          GraphicsUtil.drawPoint(g, size, maxRadius, 0 - lastInfo.sxEarth + lastInfo.sxTarget, 0 - lastInfo.syEarth + lastInfo.syTarget, Color.YELLOW)
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
