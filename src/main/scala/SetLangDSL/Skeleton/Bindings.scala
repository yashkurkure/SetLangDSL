package SetLangDSL.Skeleton
import SetLangDSL.DSL.*

//Scala imports
import scala.collection.mutable

//DSL imports

trait Bindings[T <: Bindings[T]](context: Definition[_]) { self: T =>
  
  protected val bindingMap: mutable.Map[String, Value]
  
  def Variable(name: String): IncompleteBinding[_]
  
  def Scope(name: String): IncompleteBinding[_]
  
}
