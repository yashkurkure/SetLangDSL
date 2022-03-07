package SetLangDSL
import scala.collection.mutable

package object DSLSetOperations
{
  def SetInsert(value: Any): mutable.Set[Any] = {
    val set = mutable.Set.empty[Any]
    if(value.isInstanceOf[Value])
      set.add(value.asInstanceOf[Value].getValue)
    else
      set.add(value)
    set
  }

  def SetInsert(values: Tuple): mutable.Set[Any] = {
    val set = mutable.Set.empty[Any]
    values.productIterator.foreach(x=>{
      if(x.isInstanceOf[Value])
        set.add(x.asInstanceOf[Value].getValue)
      else
        set.add(x)
    })
    set
  }

  def SetUnion(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] | set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      resultSet
    else
      null
  }

  def SetIntersection(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] & set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      resultSet
    else
      null
  }

  def SetDifference(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] &~ set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      resultSet
    else
      null
  }

  def SetSymmetricDifference(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = (set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] | set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]) &~ (set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] & set2AsValue.getValue.asInstanceOf[mutable.Set[Any]])
      resultSet
    else
      null
  }

  def SetCartesianProduct(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val set1 = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      val set2 = set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      val resultSet = mutable.Set.empty[Any]
      if set1.isEmpty || set2.isEmpty then
        resultSet
      else
        set1.foreach(mem1=>set2.foreach(mem2=>resultSet.add((mem1, mem2))))
        resultSet
      resultSet
    else
      null
  }

}
