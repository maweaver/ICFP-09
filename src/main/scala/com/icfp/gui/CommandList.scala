package com.icfp.gui

import javax.swing.table.AbstractTableModel

import scala.swing.Table
import vm.{Cmpz, DCode, SCode, Vm}

class CommandList(vm: Vm)
extends Table {
  
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
            
            if(col == R1Column) return scode.r10.toString()
            
            if(info.isInstanceOf[Cmpz]) {
              val cmpz = info.asInstanceOf[Cmpz]
              if(col == OpColumn) return cmpz.op
            }
          }
          
          if(info.isInstanceOf[DCode]) {
            val dcode = info.asInstanceOf[DCode]
            
            if(col == R1Column) return dcode.r10.toString()
            if(col == R2Column) return dcode.r20.toString()
          }
      }
    }
    
  }  
  
}
