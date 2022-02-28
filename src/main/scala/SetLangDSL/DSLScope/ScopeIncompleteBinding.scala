package SetLangDSL.DSLScope

import SetLangDSL.DSLClass._
import SetLangDSL.DSLMethod._
import SetLangDSL.DSLScope._
import SetLangDSL.Skeleton.{Bindings, Definition, IncompleteBinding}
import SetLangDSL.Value
import SetLangDSL.DSL._

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

  def Insert(value: Any): Value = {
    val set = mutable.Set.empty[Any]
    if(value.isInstanceOf[Value])
      set.add(value.asInstanceOf[Value].getValue)
    else
      set.add(value)
    val asInstanceOfType = Value(set)
    bindingMap += (name -> Value(set))
    asInstanceOfType
  }

  def Insert(values: Tuple): Value = {
    val set = mutable.Set.empty[Any]
    values.productIterator.foreach(x=>{
      if(x.isInstanceOf[Value])
        set.add(x.asInstanceOf[Value].getValue)
      else
        set.add(x)
    })
    val asInstanceOfType = Value(set)
    bindingMap += (name -> Value(set))
    asInstanceOfType
  }

  def Union(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] | set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }

  def Intersection(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] & set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }

  def Difference(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] &~ set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }

  def SymmetricDifference(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = (set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] | set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]) &~ (set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] & set2AsValue.getValue.asInstanceOf[mutable.Set[Any]])
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }

  def CartesianProduct(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val set1 = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      val set2 = set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      val resultSet = mutable.Set.empty[Any]
      if set1.isEmpty || set2.isEmpty then
        Value(resultSet)
      else
        set1.foreach(mem1=>set2.foreach(mem2=>resultSet.add((mem1, mem2))))
        Value(resultSet)
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }


}



