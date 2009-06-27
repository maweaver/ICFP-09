package com.icfp.gui

import java.awt.{Color, Point, Rectangle}
import javax.swing.JViewport
import javax.swing.table.AbstractTableModel

import scala.swing.Table
import vm.{Cmpz, DCode, SCode, Vm, InstructionExecuted, VmInitialized}

class CommandList(vm: Vm)
extends Table {
  
  selectionBackground = Color.YELLOW
  
  def selectCurrentAddress() {
    selection.rows.clear()
    if(vm.currentAddress < model.getRowCount) {
      selection.rows += vm.currentAddress
      scrollToVisible(vm.currentAddress)
    }
  }
  
  vm.reactions += { 
    case InstructionExecuted(_) => selectCurrentAddress()
  }
    
  vm.reactions += {
    case VmInitialized(_) => selectCurrentAddress()
  }
  
  selectCurrentAddress()
  
  
  def scrollToVisible(rowIndex: Int) {
    val viewport = peer.getParent.asInstanceOf[JViewport]
    val rect = peer.getCellRect(rowIndex, 0, true)
    val pt = viewport.getViewPosition
    rect.setLocation(rect.x-pt.x, rect.y-pt.y)
    viewport.scrollRectToVisible(rect)
  }
  
  model = new AbstractTableModel {
    
    val AddressColumn = 0
    val OpcodeColumn = 1
    val R1Column = 2
    val R2Column = 3
    val OpColumn = 4
    
    override def getColumnName(col: Int): String = col match {
      case AddressColumn => "Address"
      case OpcodeColumn => "OpCode"
      case R1Column => "Address 1"
      case R2Column => "Address 2"
      case OpColumn => "Operation"
    }
    
    override def getRowCount(): Int = vm.instructions.keys.foldLeft(0) { (a, b) => Math.max(a, b) }
    
    override def getColumnCount(): Int = 5
    
    override def getValueAt(row: Int, col: Int): Object = {
      if(col == AddressColumn) return "0x" + Integer.toHexString(row)
      
      vm.instructions.get(row) match {
        case None => return ""
        case Some(info) =>
          if(col == OpcodeColumn) return info.opcode
           
          if(info.isInstanceOf[SCode]) {
            val scode = info.asInstanceOf[SCode]
            
            if(col == R1Column) return "0x" + Integer.toHexString(scode.r10)
            
            if(info.isInstanceOf[Cmpz]) {
              val cmpz = info.asInstanceOf[Cmpz]
              if(col == OpColumn) return cmpz.op
            }
          }
          
          if(info.isInstanceOf[DCode]) {
            val dcode = info.asInstanceOf[DCode]
            
            if(col == R1Column) return "0x" + Integer.toHexString(dcode.r10)
            if(col == R2Column) return "0x" + Integer.toHexString(dcode.r20)
          }
      }
      
      return ""
    }
    
  }  
  
}
