package SetLangDSL.DSLScope

import SetLangDSL.Skeleton.{Bindings, Context, IncompleteBinding}
import SetLangDSL.Value
import scala.collection.mutable

class ScopeIncompleteBinding(name: String,
                             bindingMap: mutable.Map[String, Value],
                             value: Value = null)
  extends IncompleteBinding[ScopeIncompleteBinding](name, bindingMap, value){

  def toValue(value: Any): Unit = {
    null
  }

  def Insert(value: Any): Value = {
    null
  }

  def Insert(values: Tuple): Value = {
    null
  }

  def Union(set1AsValue: Value, set2AsValue: Value): Value = {
    null
  }

  def Intersection(set1AsValue: Value, set2AsValue: Value): Value = {
    null
  }

  def Difference(set1AsValue: Value, set2AsValue: Value): Value = {
    null
  }

  def SymmetricDifference(set1AsValue: Value, set2AsValue: Value): Value = {
    null
  }

  def CartesianProduct(set1AsValue: Value, set2AsValue: Value): Value = {
    null
  }


}



