package com.icfp.gui

import scala.swing.{Component, LayoutContainer, Panel}
import net.miginfocom.swing.MigLayout

/**
 * A panel which uses the MiG Layout (http://www.miglayout.com)
 */
class MigPanel(layoutConstraints: String, colConstraints: String, rowConstraints: String)
extends Panel
with LayoutContainer {

	def this() = this("", "", "")

	def this(layoutConstraints: String) = this(layoutConstraints, "", "")

	def this(layoutConstraints: String, colConstraints: String) = 
		this(layoutConstraints, colConstraints, "")

	override lazy val peer = 
		new javax.swing.JPanel(new MigLayout(layoutConstraints, colConstraints, rowConstraints))

	protected def layoutManager = peer.getLayout.asInstanceOf[MigLayout]

	class Constraints(val peer: String) extends Proxy {
		def self = peer
	}

	protected def constraintsFor(comp: Component) =
		new Constraints(layoutManager.getComponentConstraints(comp.peer).asInstanceOf[String])

	protected def areValid(c: Constraints): (Boolean, String) = (true, "")

	protected def add(c: Component, l: Constraints) { peer.add(c.peer, l) }

	def add(c: Component, l: String) { peer.add(c.peer, l) }

	def add(c: Component) { peer.add(c.peer, "") }
}
