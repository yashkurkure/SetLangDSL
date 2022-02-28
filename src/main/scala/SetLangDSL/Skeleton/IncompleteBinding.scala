package SetLangDSL.Skeleton

import SetLangDSL.Value
import scala.collection.mutable
trait IncompleteBinding[T <: IncompleteBinding[T]](name: String, 
                                                   bindingMap: mutable.Map[String, Value], 
                                                   value: Value) 
{ self: T =>

  def toValue(value: Any): Unit
  
  def getValue: Value = value
  
  def Insert(value: Any): Value

  def Insert(values: Tuple): Value

  def Union(set1AsValue: Value, set2AsValue: Value): Value

  def Intersection(set1AsValue: Value, set2AsValue: Value): Value

  def Difference(set1AsValue: Value, set2AsValue: Value): Value

  def SymmetricDifference(set1AsValue: Value, set2AsValue: Value): Value

  def CartesianProduct(set1AsValue: Value, set2AsValue: Value): Value


}