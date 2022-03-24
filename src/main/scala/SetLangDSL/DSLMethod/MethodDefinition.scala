package SetLangDSL.DSLMethod

import SetLangDSL.DSLScope.{ScopeBindings, ScopeDefinition}
import SetLangDSL.DSL.*

class MethodDefinition(parent: ScopeDefinition,
                       access: accessSpecifier, // access specifier for the method
                       name: String, //name of the method
                       parameters: Parameters, //parameters of the method
                       body: MethodDefinition=>Value //method definition
                      ) extends ScopeDefinition(parent){

  override val bindings: MethodBindings = new MethodBindings(this)

  def getParameters: Parameters = parameters
  def getAccessSpecifier: accessSpecifier = access
  def getBody: MethodDefinition=>Value = body

  override def Assign: MethodBindings = bindings

}
