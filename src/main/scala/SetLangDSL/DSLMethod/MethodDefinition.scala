package SetLangDSL.DSLMethod

//Scala imports
import SetLangDSL.DSLClass.{ClassBindings, ClassDefinition, ClassIncompleteBinding}
import SetLangDSL.DSLScope.ScopeDefinition
import SetLangDSL.Skeleton.{Bindings, Definition}

import scala.annotation.targetName
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

//DSL imports
import SetLangDSL.DSL.accessSpecifier
import SetLangDSL.Value
import SetLangDSL.Parameters

/**
 * SetLangMethod
 *
 * This class extends ExecutionContext
 * A method is also a type of a scope, in which you can define variables and scopes
 *
 * The parent value for the execution context of a method is null, as the DSL does not support
 *  methods inside methods
 * */
/*class MethodContext(val access: accessSpecifier, val name: String)
  extends ExecutionContext(null){

  val methodParameters = mutable.LinkedHashSet.empty[String]

  def Parameters(params: String*): Unit = {
    params.foreach(param=>methodParameters.add(param))
  }

}*/

class MethodDefinition(access: accessSpecifier, name: String, parameters: Parameters, body: MethodDefinition=>Value)
extends Definition[MethodDefinition](null) {

  val bindings: MethodBindings = new MethodBindings(this)
  def getParameters: Parameters = parameters

  def getAccessSpecifier: accessSpecifier = access
  
  def getBody: MethodDefinition=>Value = body
  

  def Assign: MethodBindings = {
    bindings
  }

  def Variable(name: String): Value = {
    // Get the incomplete binding (This can tell us if the binding exists or not)
    val incompleteBinding = bindings.Variable(name)

    // If the value of the incomplete binding is not null, that means a binding exits
    if incompleteBinding.getValue != null then
    // return the value associated with the name
      incompleteBinding.getValue
    else
    //when the parent value is null, we have reached the global scope
    //since there is no outer scope, we will halt the search for the binding by returning null
    // returning null is the same as the binding not being found
      null
  }

  @targetName("Create Anonymous Scope")
  def Scope(f: ScopeDefinition => Unit): Unit = {
    null
  }

  @targetName("Create Named Scope")
  def Scope(scopeName: String, f: ScopeDefinition => Unit): Unit = {
    null
  }

  @targetName("Get Named Scope")
  def Scope(scopeName: String): MethodDefinition = {
    null
  }
}