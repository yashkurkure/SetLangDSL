package SetLangDSL.Skeleton
import SetLangDSL.DSLScope.ScopeDefinition
import SetLangDSL.DSL.*

import scala.annotation.targetName
trait Definition[T <: Definition[T]](parent: T) { self: T =>
  
  protected val bindings: Bindings[_]
  
  def Assign: Bindings[_]
  
  def Variable(name: String): Value

  @targetName("Create Anonymous Scope")
  def Scope(f:ScopeDefinition=>Unit): Unit

  @targetName("Create Named Scope")
  def Scope(scopeName: String, f: ScopeDefinition => Unit): Unit

  @targetName("Get Named Scope")
  def Scope(scopeName: String): T
}