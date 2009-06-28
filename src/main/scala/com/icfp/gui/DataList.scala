package com.icfp.gui

import javax.swing.table.AbstractTableModel
import scala.collection.mutable.Map
import scala.swing.Table
import vm.{Vm, InstructionExecuted, VmInitialized}

abstract class DataList(vm: Vm)
extends Table {
  
  def data: Array[Vm.Data]
  
  def maxValue: Vm.Address
  
  model = new AbstractTableModel {
    
    vm.reactions += {
      case VmInitialized(_) => fireTableDataChanged()
      case InstructionExecuted(_) => fireTableDataChanged()
    }

    val AddressColumn = 0
    val DataColumn = 1
    
    override def getColumnName(col: Int): String = col match {
      case AddressColumn => "Address"
      case DataColumn => "Data"
    }
    
    override def getRowCount(): Int = maxValue
    
    override def getColumnCount(): Int = 2
    
    override def getValueAt(row: Int, col: Int): Object = {
      if(col == AddressColumn) return "0x" + row
      if(col == DataColumn) return data(row).toString()
      return ""
    }
    
    override def isCellEditable(row: Int, col: Int): Boolean = col match {
      case AddressColumn => false
      case DataColumn => true
    }
    
    override def setValueAt(value: Object, row: Int, col: int) {
      try {
        data(row) = new java.lang.Double(value.toString()).asInstanceOf[Vm.Data]
      } catch { case _ => }
    }
    
  }
}
