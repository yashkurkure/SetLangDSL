package SetLangDSL.DSLScope

import SetLangDSL.DSLClass._
import SetLangDSL.DSLMethod._
import SetLangDSL.DSLScope._
import SetLangDSL.Skeleton.{Bindings, Definition, IncompleteBinding}
import SetLangDSL.Value
import SetLangDSL.DSL._
import SetLangDSL.DSLSetOperations.*

import scala.collection.mutable

class ScopeIncompleteBinding(name: String,
                             bindingMap: mutable.Map[String, Value],
                             value: Value = null)
  extends IncompleteBinding[ScopeIncompleteBinding](name, bindingMap, value){

  def NewObject(className: String): ClassInstance = {
    //check if the class definition exists
    if bindingMap.contains(className) && bindingMap(className).checkIfTypeClassDefinition then
      //create an instance
      val instance = new ClassInstance(bindingMap(className).getValue.asInstanceOf[ClassDefinition])
      //create a binding for the instance
      bindingMap += (name->Value(instance))
      instance
    else
      null
  }

  def Method(name: String, classInstance: Value): MethodContext = {
    if classInstance.checkIfTypeClassInstance then
      val classDefinition = classInstance.getValue.asInstanceOf[ClassInstance].getDefinition
      if classDefinition.Variable(name).checkIfTypeMethodDefinition then
        val methodDefinition = classDefinition.Variable(name).getValue.asInstanceOf[MethodDefinition]
        if(methodDefinition.getAccessSpecifier != Private)
          new MethodContext(this, classInstance.getValue.asInstanceOf[ClassInstance], methodDefinition)
        else
          null
      else
        null
    else
      null
  }

  def toValue(value: Any): Unit = {
    if value.isInstanceOf[Value] then
      toValue(value.asInstanceOf[Value].getValue)
    else
      bindingMap += (name -> Value(value))
  }
  
  def toMacro(f: Value=>Unit) = {
    bindingMap +=(name -> Value(f))
  }

  def Insert(value: Any): Value = {
    val set = SetInsert(value)
    if set == null then
      null
    else
      val asInstanceOfValue = Value(set)
      bindingMap += (name -> asInstanceOfValue)
      asInstanceOfValue
  }

  def Insert(values: Tuple): Value = {
    val set = SetInsert(values)
    if set == null then
      null
    else
      val asInstanceOfValue = Value(set)
      bindingMap += (name -> asInstanceOfValue)
      asInstanceOfValue
  }

  def Union(set1AsValue: Value, set2AsValue: Value): Value = {
    val set = SetUnion(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      val asInstanceOfValue = Value(set)
      bindingMap += (name -> asInstanceOfValue)
      asInstanceOfValue
  }

  def Intersection(set1AsValue: Value, set2AsValue: Value): Value = {
    val set = SetIntersection(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      val asInstanceOfValue = Value(set)
      bindingMap += (name -> asInstanceOfValue)
      asInstanceOfValue
  }

  def Difference(set1AsValue: Value, set2AsValue: Value): Value = {
    val set = SetDifference(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      val asInstanceOfValue = Value(set)
      bindingMap += (name -> asInstanceOfValue)
      asInstanceOfValue
  }

  def SymmetricDifference(set1AsValue: Value, set2AsValue: Value): Value = {
    val set = SetSymmetricDifference(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      val asInstanceOfValue = Value(set)
      bindingMap += (name -> asInstanceOfValue)
      asInstanceOfValue
  }

  def CartesianProduct(set1AsValue: Value, set2AsValue: Value): Value = {
    val set = SetCartesianProduct(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      val asInstanceOfValue = Value(set)
      bindingMap += (name -> asInstanceOfValue)
      asInstanceOfValue
  }


}



