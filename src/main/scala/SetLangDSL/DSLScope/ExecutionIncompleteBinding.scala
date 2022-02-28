package SetLangDSL.DSLScope

import SetLangDSL.Value

import scala.collection.mutable

/**
 * IncompleteBinding
 *
 * The instance of this class acts as an intermediate step for creating a binding.
 * A binding will only be created if the "name" is not already bound to something
 *
 * */
class ExecutionIncompleteBinding(name: String, bindingMap: mutable.Map[String, Value], value: Value = null)
{
  def toValue(value: Any): Unit = {
    bindingMap += (name -> Value(value))
    //println("Created binding for: " + name)
  }

  def getValue: Value = value

  // This insert is to add 1 element to the set
  def Insert(value: Any):Value = {
    val set = mutable.Set.empty[Any]
    if(value.isInstanceOf[Value])
      set.add(value.asInstanceOf[Value].getValue)
    else
      set.add(value)
    val asInstanceOfType = Value(set)
    bindingMap += (name -> Value(set))
    asInstanceOfType
  }

  // This insert can add more than 2 elements to the set
  def Insert(values: Tuple):Value = {
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

  def Union(set1AsType: Value, set2AsType: Value): Value = {
    if set1AsType.checkIfTypeSet && set2AsType.checkIfTypeSet then
      val resultSet = set1AsType.getValue.asInstanceOf[mutable.Set[Any]] | set2AsType.getValue.asInstanceOf[mutable.Set[Any]]
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }

  def Intersection(set1AsType: Value, set2AsType: Value): Value = {
    if set1AsType.checkIfTypeSet && set2AsType.checkIfTypeSet then
      val resultSet = set1AsType.getValue.asInstanceOf[mutable.Set[Any]] & set2AsType.getValue.asInstanceOf[mutable.Set[Any]]
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }

  def Difference(set1AsType: Value, set2AsType: Value): Value = {
    if set1AsType.checkIfTypeSet && set2AsType.checkIfTypeSet then
      val resultSet = set1AsType.getValue.asInstanceOf[mutable.Set[Any]] &~ set2AsType.getValue.asInstanceOf[mutable.Set[Any]]
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }

  def SymmetricDifference(set1AsType: Value, set2AsType: Value): Value = {
    if set1AsType.checkIfTypeSet && set2AsType.checkIfTypeSet then
      val resultSet = (set1AsType.getValue.asInstanceOf[mutable.Set[Any]] | set2AsType.getValue.asInstanceOf[mutable.Set[Any]]) &~ (set1AsType.getValue.asInstanceOf[mutable.Set[Any]] & set2AsType.getValue.asInstanceOf[mutable.Set[Any]])
      bindingMap += (name->Value(resultSet))
      Value(resultSet)
    else
      null
  }

  def CartesianProduct(set1AsType: Value, set2AsType: Value): Value = {
    if set1AsType.checkIfTypeSet && set2AsType.checkIfTypeSet then
      val set1 = set1AsType.getValue.asInstanceOf[mutable.Set[Any]]
      val set2 = set2AsType.getValue.asInstanceOf[mutable.Set[Any]]
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
