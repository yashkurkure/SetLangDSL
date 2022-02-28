package SetLangDSL.Skeleton

//Scala imports
import scala.collection.mutable

//DSL imports
import SetLangDSL.Value

trait Bindings[T <: Bindings[T]](context: Context[_]) { self: T =>
  
  val bindingMap: mutable.Map[String, Value]
  
  def Variable(name: String): IncompleteBinding[_]
  
  def Scope(name: String): IncompleteBinding[_]
  
}
