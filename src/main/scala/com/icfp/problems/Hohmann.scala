package com.icfp.problems

import java.awt.{Color, Graphics, Point}
import javax.swing.table.{AbstractTableModel}
import scala.swing.{Component, Orientation, Panel, ScrollPane, SplitPane, Table}
import gui.MigPanel
import util.{GraphicsUtil, Physics}
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
  private var info: List[HohmannInfo] = Nil

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
  
  var doingHohmann = false
  
  var clockwise: Option[Boolean] = None
  
  var lastPhi: Option[Double] = None
  
  var r1: Option[Double] = None

  var timePredict: Double = 0.0d

  /**
   * @inheritDoc
   */
  override def control(stepNum: Int) {
    val targetRadius = vm.outputPorts(0x4)
    val sx = vm.outputPorts(0x2)
    val sy = vm.outputPorts(0x3)
    val polars = Physics.toPolar(-sx, -sy)

    if(!lastPhi.isEmpty && clockwise.isEmpty) {
      var deltaPhi = polars._2 - lastPhi.get
      
      var newDeltaPhi = 
        if(deltaPhi > Physics.Pi)
          deltaPhi - 2.0d * Physics.Pi
        else if(deltaPhi < -Physics.Pi)
          deltaPhi + 2.0d * Physics.Pi
        else
          deltaPhi
      println("clockwise check " + polars._2 + " " + lastPhi.get + ", deltaPhi = " + newDeltaPhi)
      clockwise = Some(newDeltaPhi < 0)
    }

    vm.inputPorts(0x2) = 0.0d
    vm.inputPorts(0x3) = 0.0d
    
    //println("Current radius is " + polars._1 + ", angle is " + polars._2 + ", last angle was " + lastPhi + ", distance is " + (Math.abs(targetRadius - polars._1)) + ", clockwise? " + clockwise)
    lastPhi = Some(polars._2)
    
    // Go into hohmann maneuver
    if(!clockwise.isEmpty &&
      !doingHohmann && 
      targetRadius != 0.0d && 
      Math.abs(targetRadius - polars._1) > 1000) {
        
      r1 = Some(polars._1)
        
      val deltaVs = Physics.hohmannIn((-sx, -sy), targetRadius, clockwise.get)
      
      timePredict = vm.currentStep + Physics.hohmannTime((-sx, -sy), targetRadius)
      println("Starting Hohmann maneuver at time " + vm.currentStep + " using deltavs of (" + deltaVs._1 + ", " + deltaVs._2 + "); expected duration is " + Physics.hohmannTime((-sx, -sy), targetRadius))
      println("Should come out at " + timePredict)
      vm.inputPorts(0x2) = deltaVs._1
      vm.inputPorts(0x3) = deltaVs._2
      doingHohmann = true
    }
    
    // Come out of hohmann maneuver
    if(!clockwise.isEmpty &&
      !r1.isEmpty &&
      doingHohmann &&
      (vm.currentStep >= timePredict)) {

      /*Math.abs(targetRadius - polars._1) < 10)*/
      
      val deltaVs = Physics.hohmannOut(r1.get, (-sx, -sy), targetRadius, clockwise.get)
      
      println("Finishing Hohmann maneuver at time " + vm.currentStep + " using deltavs of (" + deltaVs._1 + ", " + deltaVs._2 + ")")
      vm.inputPorts(0x2) = deltaVs._1
      vm.inputPorts(0x3) = deltaVs._2
      doingHohmann = false
    }
  }
  
  /**
   * @inheritDoc
   */
  override def observe(stepNum: Int) {
    info ::= HohmannInfo(stepNum, 
      vm.outputPorts(0x0), // score
      vm.outputPorts(0x1), // fuel
      vm.outputPorts(0x2), // sx
      vm.outputPorts(0x3), // sy
      Math.sqrt(Math.pow(vm.outputPorts(0x2), 2.0d) + Math.pow(vm.outputPorts(0x3), 2.0d)),
      vm.outputPorts(0x4)) //targetRadius
        
    dataTable.model.asInstanceOf[AbstractTableModel].fireTableDataChanged()
    graphicsPanel.peer.repaint(0, 0, 0, graphicsPanel.size.getWidth.toInt, graphicsPanel.size.getHeight.toInt)
  }
  
  val dataTable = new Table {
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
    
  val graphicsPanel = new Panel {
      override def paintComponent(g: Graphics) {
        
        val maxRadius = 6.0d * Physics.Re
         
        g.setColor(Color.BLACK)
        g.fillRect(0, 0, size.getWidth.toInt, size.getHeight.toInt)
        
        GraphicsUtil.drawRadius(g, size, maxRadius, Physics.Re, Color.GREEN, true)
        
        if(!info.isEmpty) {
          val lastInfo = info.first
          
          GraphicsUtil.drawRadius(g, size, maxRadius, lastInfo.radius, Color.WHITE, false)
          GraphicsUtil.drawRadius(g, size, maxRadius, lastInfo.targetRadius, Color.BLUE, false)
        
          GraphicsUtil.drawPoint(g, size, maxRadius, -lastInfo.sx, -lastInfo.sy, Color.RED)
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
    doingHohmann = false
    clockwise = None
    lastPhi = None
    r1 = None
  }
}
