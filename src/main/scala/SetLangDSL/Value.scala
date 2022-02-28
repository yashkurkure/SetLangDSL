package SetLangDSL

import scala.collection.mutable
import SetLangDSL.DSLScope._
import DSLClass._
import DSLScope._
import DSLMethod._

class Value(value: Any)
{

  def equals(value: Value) : Boolean = {
    if this.getValue == value.getValue then
      true
    else
      false
  }

  def getValue:Any = value
  

  // This insert adds 1 element to an existing set
  def Insert(value: Any):Value = {
    //value must be an instance of an set for this to work
    if this.checkIfTypeSet then
      //println("Yay insert is working")
      val set = value.asInstanceOf[mutable.Set[Any]]
      value match {
        case value1: Value => set.add(value1.getValue)
        case _ => set.add(value)
      }
      this
    else
    //println("Insert went into else:(")
      null
  }

  // This insert can insert more than 2 elements to an existing set
  def Insert(values: Tuple):Value = {
    //value must be an instance of an set for this to work
    if this.checkIfTypeSet then
      //println("Yay insert is working")
      val set = value.asInstanceOf[mutable.Set[Any]]
      values.productIterator.foreach {
        case value1: Value => set.add(value1.getValue)
        case x => set.add(x)
      }
      this
    else
      //println("Insert went into else:(")
      null

  }

  def Delete(setMember: Any): Value = {
    if this.checkIfTypeSet then
      val set = value.asInstanceOf[mutable.Set[Any]]
      setMember match {
        case value1: Value => set.remove(value1.getValue)
        case _ => set.remove(setMember)
      }
      this
    else
      null
  }

  def Check(setMember: Any): Value = {
    if this.checkIfTypeSet then
      val set = value.asInstanceOf[mutable.Set[Any]]
      setMember match {
        case value1: Value => if set.contains(value1.getValue) then Value(true) else Value(false)
        case _ => if set.contains(setMember) then Value(true) else Value(false)
      }
    else
      null
  }


  def checkIfTypeScope: Boolean = {
    //println("checkType")
    if this.getValue.isInstanceOf[ScopeDefinition] then true
    else false
  }

  def checkIfTypeSet: Boolean = {
    if this.getValue.isInstanceOf[mutable.Set[Any]] then true
    else false
  }

  def checkIfTypeClassDefinition: Boolean = {
    if this.getValue.isInstanceOf[ClassDefinition] then true
    else
      false
  }
  
  def checkIfTypeClassInstance: Boolean = {
    if this.getValue.isInstanceOf[ClassInstance] then true
    else
      false
    
  }

  def checkIfTypeMethodDefinition: Boolean = {
    if this.getValue.isInstanceOf[MethodDefinition] then true
    else
      false
  }

  def checkIfTypeMethodContext: Boolean = {
    if this.getValue.isInstanceOf[MethodContext] then true
    else
      false

  }
  
  def checkIfTypeMacro: Boolean = {
    if this.getValue.isInstanceOf[Value=>Unit] then true
    else
      false
  }


}
