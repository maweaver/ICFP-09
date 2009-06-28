package com.icfp.util

object Physics {
  
  /**
   * Pi
   */
  val Pi = 2.0d * Math.acos(0.0d)
 
  /**
   * Gravitational constant
   */
  val G = 6.67428e-11d
  
  /**
   * Mass of the earth
   */
  val Me = 6.0e24d
  
  /**
   * Radius of the earth
   */
  val Re = 6557000.000001654d
  
  def normalizeAngle(angle: Double): Double = {
    def makeLess2Pi(angle: Double): Double = {
      if(angle > 2.0d * Pi) {
        makeLess2Pi(angle - 2.0d * Pi)
      } else {
        angle
      }
    }
    
    def makeGreaterZero(angle: Double): Double = {
      if(angle < 0.0d) {
        makeGreaterZero(angle + 2.0d * Pi)
      } else {
        angle
      }
    }
    
    makeLess2Pi(makeGreaterZero(angle))
  }
 
  /**
   * Converts cartesian coordinates to polar; return value is (r, phi)
   */
  def toPolar(pos: (Double, Double)): (Double, Double) = {
    val r = Math.sqrt(Math.pow(pos._1, 2.0d) + Math.pow(pos._2, 2.0d))
    val x = pos._1
    val y = pos._2
    val phi =
     if(x > 0 && y >= 0) {
       Math.atan(y / x)
     } else if(x > 0 && y < 0) {
       Math.atan(y / x) + 2.0d * Pi
     } else if(x < 0) {
       Math.atan(y / x) + Pi
     } else if(x == 0 && y > 0) {
       Pi / 2.0d
     } else if(x == 0 && y < 0) {
       1.5d * Pi
     } else {
       0.0d
     }
     //val phi = Math.atan2(pos._1, pos._2)
    (r, phi)
  }
  
  /**
   * Provides the components of velocity necessary to transfer into an elliptical
   * orbit for a Hohmann maneuver
   */
  def hohmannIn(pos: (Double, Double), r2: Double, clockwise: Boolean): (Double, Double) = {
    val polars = toPolar(pos)
    val r1 = polars._1
    val dvDir = 
      if(clockwise) {
        polars._2 - Pi / 2.0d 
      } else {
        polars._2 + Pi / 2.0d
      }
      
    val vMag = Math.sqrt(G * Me / r1) * (Math.sqrt((2 * r2) / (r1 + r2)) - 1)
    
    println("Direction: " + (if(clockwise) "Clockwise" else "Counter-Clockwise"))
    println("G: " + G)
    println("Me: " + Me)
    println("U: " + (G * Me))
    println("Cartesian coordinate: (" + pos._1 + ", " + pos._2 + ")")
    println("Polar coordinates: (" + polars._1 + ", " + polars._2 + ")")
    println("R1: " + r1)
    println("R2: " + r2)
    println("Direction to fire thrusters: " + dvDir)
    println("Velocity: magnitude = " + vMag + ", components are (" + (vMag * Math.cos(dvDir)) + ", " + (vMag * Math.sin(dvDir)) + ")")

    (vMag * Math.cos(dvDir), vMag * Math.sin(dvDir))
  }
  
  /**
   * Provides the components of velocity necessary to transfer back into a normal
   * orbit out of the elliptical Hohmann orbit
   */
  def hohmannOut(r1: Double, pos: (Double, Double), r2: Double, clockwise: Boolean): (Double, Double) = {
    val polars = toPolar(pos)
    val dvDir = 
      if(clockwise) {
        polars._2 - Pi / 2.0d 
      } else {
        polars._2 + Pi / 2.0d
      }

    val vMag = Math.sqrt(G * Me / r2) * (1 - Math.sqrt((2 * r1) / (r1 + r2)))
    
    println("Direction: " + (if(clockwise) "Clockwise" else "Counter-Clockwise"))
    println("G: " + G)
    println("Me: " + Me)
    println("U: " + (G * Me))
    println("Cartesian coordinate: (" + pos._1 + ", " + pos._2 + ")")
    println("Polar coordinates: (" + polars._1 + ", " + polars._2 + ")")
    println("R1: " + r1)
    println("R2: " + r2)
    println("Direction to fire thrusters: " + dvDir)
    println("Velocity: magnitude = " + vMag + ", components are (" + (vMag * Math.cos(dvDir)) + ", " + (vMag * Math.sin(dvDir)) + ")")

    (vMag * Math.cos(dvDir), vMag * Math.sin(dvDir))
  }
  
  /**
   * Amount of time to do the Hohmann maneuver.  Could be useful for interception
   * puzzles?
   */
  def hohmannTime(pos: (Double, Double), r2: Double): Double = {
    val r1 = toPolar(pos)._1
     
    Pi * Math.sqrt(Math.pow(r1 + r2, 3.0d) / (8.0d * G * Me))
  }
  
  def distance(p1: (Double, Double), p2: (Double, Double)): Double =
    Math.sqrt(Math.pow(p2._1 - p1._1, 2.0d) + Math.pow(p2._2 - p1._2, 2.0d))
}
