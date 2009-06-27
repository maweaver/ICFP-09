package com.icfp.gui

import scala.swing.Publisher
import scala.swing.event.Event
import problems.Problem

/**
 * Event dispatched when the current problem changes
 */
case class ProblemChanged(newProblem: Problem)
extends Event

/**
 * The state of the user interface
 */
class GuiState 
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
  
}
