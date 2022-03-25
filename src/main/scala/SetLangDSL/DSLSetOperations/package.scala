package SetLangDSL

import scala.collection.mutable
import SetLangDSL.DSL.Value

package object DSLSetOperations {
  /**
   * SetInsert(value: Any)
   *
   * Returns a new set with the the value to insert
   *
   * */
  def SetInsert(value: Any): mutable.Set[Any] = {

    // Create an empty set
    val set = mutable.Set.empty[Any]

    // Check if the passed value is a raw value of encased in Value
    if (value.isInstanceOf[Value])

    // Convert the value to raw
    // Insert the value in the set
      set.add(value.asInstanceOf[Value].getValue)
    else
    // Insert the raw value in the set
      set.add(value)

    // return the set value
    set
  }


  /**
   * SetInsert(values: Tuple)
   *
   * Returns a new set with the values specified in the tuple
   *
   * */
  def SetInsert(values: Tuple): mutable.Set[Any] = {

    // Create an empty set
    val set = mutable.Set.empty[Any]

    // For each value in the tuple
    values.productIterator.foreach(x => {

      // Do not add null values to the set
      if x != null then
      // If the value is encased in Value
        if (x.isInstanceOf[Value])
          set.add(x.asInstanceOf[Value].getValue)

        // If the value is raw
        else
          set.add(x)
    })
    set
  }


  /**
   * SetUnion(set1AsValue: Value, set2AsValue: Value)
   *
   * Returns a result set of the union of the user's sets
   *
   * */
  def SetUnion(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {

    // Check if the Values are of type set
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] | set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      resultSet

    // Return null if the values passed are not sets
    else
      null
  }


  /**
   * SetUIntersection(set1AsValue: Value, set2AsValue: Value)
   *
   * Returns a result set of the intersection of the user's sets
   *
   * */
  def SetIntersection(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {

    // Check if the Values are of type set
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] & set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      resultSet

    // Return null if the values passed are not sets
    else
      null
  }


  /**
   * SetDifference(set1AsValue: Value, set2AsValue: Value)
   *
   * Returns a result set of the difference of the user's sets
   *
   * */
  def SetDifference(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {

    // Check if the Values are of type set
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] &~ set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      resultSet

    // Return null if the values passed are not sets
    else
      null
  }


  /**
   * SetSymmetricDifference(set1AsValue: Value, set2AsValue: Value)
   *
   * Returns a result set of the symmetric difference of the user's sets
   *
   * */
  def SetSymmetricDifference(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {

    // Check if the Values are of type set
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then
      val resultSet = (set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] | set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]) &~ (set1AsValue.getValue.asInstanceOf[mutable.Set[Any]] & set2AsValue.getValue.asInstanceOf[mutable.Set[Any]])
      resultSet

    // Return null if the values passed are not sets
    else
      null
  }


  /**
   * SetCartesianProduct(set1AsValue: Value, set2AsValue: Value)
   *
   * Returns a result set of the cartesian product of the user's sets
   *
   * */
  def SetCartesianProduct(set1AsValue: Value, set2AsValue: Value): mutable.Set[Any] = {

    // Check if the values are of type set
    if set1AsValue.checkIfTypeSet && set2AsValue.checkIfTypeSet then

      // Get the raw values of the set
      val set1 = set1AsValue.getValue.asInstanceOf[mutable.Set[Any]]
      val set2 = set2AsValue.getValue.asInstanceOf[mutable.Set[Any]]

      // Create a new empty set
      val resultSet = mutable.Set.empty[Any]

      // Check if any of the user's sets are empty
      if set1.isEmpty || set2.isEmpty then

      // Return empty set if any of the user's sets are empty
        resultSet

      else

        // Calculate the cartesian product
        set1.foreach(mem1 => set2.foreach(mem2 => resultSet.add((mem1, mem2))))
        resultSet

    else
    // Return null if the values are not sets
      null
  }
}
