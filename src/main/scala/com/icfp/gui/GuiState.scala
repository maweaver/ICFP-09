package com.icfp.gui

import scala.swing.{Component, Publisher}
import scala.swing.event.Event
import problems.Problem

/**
 * Event dispatched when the current problem changes
 */
case class ProblemChanged(newProblem: Problem)
extends Event

/**
 * Event dispatched when the target step changes
 */
case class StepIncrementChanged(newVal: Int)
extends Event

/**
 * The state of the user interface
 */
class GuiState(val component: Component)
extends Publisher {
  
  private var _currentProblem: Problem = Problem.all.first
  
  /**
   * The problem being displayed
   */
  def currentProblem: Problem = _currentProblem
  
  def currentProblem_=(value: Problem) { 
    _currentProblem = value; 
    publish(ProblemChanged(value)) 
  }
  
  
  private var _stepIncrement: Int = 900
 
  /**
   * The step to stop a running execution at
   */
  def stepIncrement: Int = _stepIncrement
  
  def stepIncrement_=(value: Int) {
    _stepIncrement = value
    publish(StepIncrementChanged(value))
  }
  
}
