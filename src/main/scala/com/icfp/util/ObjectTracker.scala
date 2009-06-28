package com.icfp.util

class ObjectTracker {
  
  def addPosition(pos: (Double, Double)) {
    if(positions.length > 0) {
      val lastPos = positions.first
      val velocity = Physics.distance(pos, lastPos)
      
      if(velocities.length > 0) {
        val lastVelocity = velocities.first
        val acceleration = lastVelocity - velocity
        
        accelerations ::= acceleration
      }
      
      velocities ::= velocity
    }
    
    positions ::= pos
    angles ::= Physics.toPolar(positions.first)._2
    radii ::= Physics.toPolar(positions.first)._1
    
    // Limit output
    angles = angles.take(5)
    positions = positions.take(5)
    velocities = velocities.take(5)
    accelerations = accelerations.take(5)
    radii = radii.take(5)
  }
  
  var angles: List[Double] = Nil
  
  var radii: List[Double] = Nil

  var positions: List[(Double, Double)] = Nil
  
  var velocities: List[Double] = Nil
  
  var accelerations: List[Double] = Nil
  
  def clockwise: Option[Boolean] = {
    if(angles.length > 1) {
      val angle1 = angles.drop(1).first
      val angle2 = angles.first
      
      val deltaPhi = angle1 - angle2
      var newDeltaPhi = 
        if(deltaPhi > Physics.Pi)
          deltaPhi - 2.0d * Physics.Pi
        else if(deltaPhi < -Physics.Pi)
          deltaPhi + 2.0d * Physics.Pi
        else
          deltaPhi
      Some(newDeltaPhi > 0)
    } else {
      None
    }
  }
  
}
