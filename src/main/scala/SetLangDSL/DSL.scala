package SetLangDSL

import SetLangDSL.DSLCLass.ClassContext
import SetLangDSL.DSLScope.ExecutionContext

object DSL {
  //Creating a global scope
  //This acts as an entry point for the DSL
  def Scope(f: ExecutionContext => Unit): ExecutionContext = {
    val scopeDefinition = new ExecutionContext(null)
    f(scopeDefinition)
    scopeDefinition
  }
  
  sealed trait accessSpecifier
  final case object Public extends accessSpecifier
  final case object Protected extends accessSpecifier
  final case object Private extends accessSpecifier
}
