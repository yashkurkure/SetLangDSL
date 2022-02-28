package SetLangDSL

import SetLangDSL.DSLClass.ClassDefinition
import SetLangDSL.DSLScope.ScopeDefinition

object DSL {
  //Creating a global scope
  //This acts as an entry point for the DSL
  def Scope(f: ScopeDefinition => Unit): ScopeDefinition = {
    val scopeDefinition = new ScopeDefinition(null)
    f(scopeDefinition)
    scopeDefinition
  }
  
  sealed trait accessSpecifier
  final case object Public extends accessSpecifier
  final case object Protected extends accessSpecifier
  final case object Private extends accessSpecifier
}
