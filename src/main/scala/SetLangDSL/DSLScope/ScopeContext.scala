package SetLangDSL.DSLScope

import SetLangDSL.Skeleton.{Context, Bindings, IncompleteBinding}
import SetLangDSL.Value

import scala.annotation.targetName

class ScopeContext(parent: ScopeContext) 
  extends Context[ScopeContext](parent)
{
  val bindings: ScopeBindings = new ScopeBindings(this)

  def Assign: Bindings[_] = {
    null
  }

  def Variable: Value= {
    null
  }

  @targetName("Create Anonymous Scope")
  def Scope(f:ScopeContext=>Unit): Unit = {
    
  }

  @targetName("Create Named Scope")
  def Scope(name: String, f:ScopeContext => Unit): Unit = {
    
  }

  @targetName("Get Named Scope")
  def Scope(name: String): ScopeContext = {
    null
  }


}
