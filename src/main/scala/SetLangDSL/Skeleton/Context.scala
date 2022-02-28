package SetLangDSL.Skeleton
import SetLangDSL.Value
import scala.annotation.targetName
trait Context[T <: Context[T]](parent: T) { self: T =>
  
  val bindings: Bindings[_]
  
  def Assign: Bindings[_]
  
  def Variable: Value

  @targetName("Create Anonymous Scope")
  def Scope(f:T=>Unit): Unit

  @targetName("Create Named Scope")
  def Scope(name: String, f: T => Unit): Unit

  @targetName("Get Named Scope")
  def Scope(name: String): T
}