package SetLangDSL

// Scala imports
import SetLangDSL.DSLScope.ScopeDefinition

import scala.collection.mutable
import DSLClass.*
import DSLMethod.*
import DSLScope.*
import SetLangDSL.DSLInterface.InterfaceDefinition

object DSL {

  // Entry point for the DSL
  def Scope(f: ScopeDefinition => Unit): ScopeDefinition = {
    val scopeDefinition = new ScopeDefinition(null)
    f(scopeDefinition)
    scopeDefinition
  }

  // Parameters for Class Methods/ Constructors
  case class Parameters(parameters: String*)
  
  // For Class inheritance
  case class Extends(className: String)
  
  // For Interface Implementation
  case class Implements(interfaceName: String)

  // For exceptions
  case class Throws(classDefinition: ClassDefinition)





  val RAISED_EXCEPTION = 100
  class Message(msgCode: Integer, extras: Any*)
  {

    def extrasMap: mutable.Map[String, Any] = mutable.Map.empty[String, Any]
    
    def what: Integer = this.msgCode

    def getExtra: mutable.Map[String, Any] = extrasMap

  }

  // Access specifiers for Class Members
  sealed trait accessSpecifier
  case object Public extends accessSpecifier
  case object Private extends accessSpecifier
  case object Protected extends accessSpecifier

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
        val set = this.getValue.asInstanceOf[mutable.Set[Any]]
        value match {
          case asTypeValue: Value => set.add(asTypeValue.getValue)
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
    
    def getField(name: String): Value = {
      // Check if this is a class instance
      if this.checkIfTypeClassInstance then
        val instance = this.getValue.asInstanceOf[ClassInstance]
        instance.getField(name)
      else
        null
    }
    
    def getMethod(name: String): MethodContext = {
      if this.checkIfTypeClassInstance then
        val instance = this.getValue.asInstanceOf[ClassInstance]
        instance.getMethod(name)
      else
        null
    }
    
    def checkIfTypeScope: Boolean = {
      //println("checkType")
      if this.getValue.isInstanceOf[ScopeDefinition] then true
      else false
    }

    def checkIfTypeSet: Boolean = {
      if this.getValue.isInstanceOf[mutable.Set[_]] then true
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

    def checkIfTypeInterfaceDefinition: Boolean = {
      if this.getValue.isInstanceOf[InterfaceDefinition] then true
      else
        false

    }

    def checkIfTypeMacro: Boolean = {
      if this.getValue.isInstanceOf[_=>_] then true
      else
        false
    }

    def checkIfTypeInteger: Boolean = {
      if this.getValue.isInstanceOf[Integer] then true
      else
        false
    }

    
    //TODO: Given some value object
    // Evaluate the object as a boolean value
    def evalAsBoolean: Boolean = {

      if this.checkIfTypeInteger then
        if this.getValue == 0 then false
        else true
      else
        true
    }
    
  }

}

