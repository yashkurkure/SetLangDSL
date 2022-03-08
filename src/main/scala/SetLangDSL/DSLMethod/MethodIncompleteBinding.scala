package SetLangDSL.DSLMethod

//Scala imports
import SetLangDSL.DSL.accessSpecifier
import SetLangDSL.Skeleton.IncompleteBinding

import scala.collection.mutable

//DSL imports
import SetLangDSL.DSL.*

class MethodIncompleteBinding(
                              name: String,
                              bindingMap: mutable.Map[String, Value],
                              value: Value = null)
  extends IncompleteBinding[MethodIncompleteBinding](name, bindingMap, value) {

  /*  override def toValue(value:Any): Unit = {
      accessBindingMap += (name->access)
      bindingMap += (name->Value(value))
    }*/

  def toValue(value: Any): Unit = {
    bindingMap += (name->Value(value))
  }

  def Insert(value: Any): Value = {
    val set = mutable.Set.empty[Any]
    value match {
      case value1: Value => set.add(value1.getValue)
      case _ => set.add(value)
    }
    val asInstanceOfType = Value(set)
    bindingMap += (name -> Value(set))
    asInstanceOfType
  }

  def Insert(values: Tuple): Value = {
    val set = mutable.Set.empty[Any]
    values.productIterator.foreach {
      case value1: Value => set.add(value1.getValue)
      case x => set.add(x)
    }
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
