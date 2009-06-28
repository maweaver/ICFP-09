package com.icfp.util

import java.awt.{Color, Graphics, Dimension, Point}

object GraphicsUtil {
  
  def globalToLocal(imageSize: Dimension, maxRadius: Double, x: Double, y: Double): (Double, Double) = {
    val minDim = Math.min(imageSize.getWidth, imageSize.getHeight)
    (imageSize.getWidth / 2.0d + ((x / maxRadius) * minDim) / 2.0d,
     imageSize.getHeight / 2.0d - ((y / maxRadius) * minDim) / 2.0d)
  }
  
  def drawRadius(g: Graphics, imageSize: Dimension, maxRadius: Double, radius: Double, color: Color, fill: Boolean) {
    
    g.setColor(color)
    
    val percentOut = radius / maxRadius

    val ul = globalToLocal(imageSize, maxRadius, -radius, radius)
    val lr = globalToLocal(imageSize, maxRadius, radius, -radius)
    
    if(fill) {
      g.fillOval(ul._1.toInt, ul._2.toInt, (lr._1 - ul._1).toInt, (lr._2 - ul._2).toInt)
    } else {
      g.drawOval(ul._1.toInt, ul._2.toInt, (lr._1 - ul._1).toInt, (lr._2 - ul._2).toInt)
    }
  }
  
  /**
   * Draws a point using coordinates relative to the center of the earth
   */
   def drawPoint(g: Graphics, imageSize: Dimension, maxRadius: Double, globalX: Double, globalY: Double, color: Color) {
     val localPoint = globalToLocal(imageSize, maxRadius, globalX, globalY)
     g.setColor(color)
     g.fillRect(localPoint._1.toInt - 2, localPoint._2.toInt - 2, 4, 4)
   }
}
