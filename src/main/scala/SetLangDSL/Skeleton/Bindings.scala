package SetLangDSL.Skeleton

//Scala imports
import scala.collection.mutable

//DSL imports
import SetLangDSL.Value

trait Bindings[T <: Bindings[T]](context: Definition[_]) { self: T =>
  
  protected val bindingMap: mutable.Map[String, Value]
  
  def Variable(name: String): IncompleteBinding[_]
  
  def Scope(name: String): IncompleteBinding[_]
  
}
