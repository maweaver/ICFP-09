package com.icfp.gui

import javax.swing.table.AbstractTableModel
import scala.collection.mutable.Map
import scala.swing.Table
import vm.{Vm, InstructionExecuted}

abstract class DataList(vm: Vm)
extends Table {
  
  def dataMap: Map[Vm.Address, Vm.Data]
  
  def maxValue: Vm.Address
  
  model = new AbstractTableModel {
    
    vm.reactions += {
      case InstructionExecuted(_) => fireTableDataChanged()
    }

    val AddressColumn = 0
    val DataColumn = 1
    
    override def getColumnName(col: Int): String = col match {
      case AddressColumn => "Address"
      case DataColumn => "Data"
    }
    
    override def getRowCount(): Int = dataMap.keys.foldLeft(0) { (a, b) => Math.max(a, b) }
    
    override def getColumnCount(): Int = 2
    
    override def getValueAt(row: Int, col: Int): Object = {
      if(col == AddressColumn) return "0x" + Integer.toHexString(row)
      if(col == DataColumn) return dataMap.getOrElse(row, 0.0d).toString()
      return ""
    }
    
  }
}
