package SetLangDSL.DSLScope

import SetLangDSL.DSLClass.{ClassDefinition, ClassInstance}
import SetLangDSL.Skeleton.{Bindings, Definition, IncompleteBinding}
import SetLangDSL.Value

import scala.annotation.targetName

class ScopeDefinition(parent: ScopeDefinition)
  extends Definition[ScopeDefinition](parent)
{
  val bindings: ScopeBindings = new ScopeBindings(this)

  def Assign: ScopeBindings = {
    bindings
  }

  def Variable(name: String): Value= {
    // Get the incomplete binding (This can tell us if the binding exists or not)
    val incompleteBinding = bindings.Variable(name)

    // If the value of the incomplete binding is not null, that means a binding exits
    if incompleteBinding.getValue != null then
    // return the value associated with the name
      incompleteBinding.getValue
    else if parent != null then
    //println("Checking the parent for name: " + name)
    //if not found in the current context, check the parent context
      parent.Variable(name)
    else
    //when the parent value is null, we have reached the global scope
    //since there is no outer scope, we will halt the search for the binding by returning null
    // returning null is the same as the binding not being found
      null
  }
  
  def ExecuteMacro(macroName: String, variable: Value): Unit = {
    val macroBody = this.Variable(macroName).getValue.asInstanceOf[Value=>Unit]
    macroBody(variable)
  }

  @targetName("Create Anonymous Scope")
  def Scope(f:ScopeDefinition=>Unit): Unit = {
    println("Creating Anonymous Scope")
    //create a execution context
    val scope = new ScopeDefinition(this)
    //execute the user's operations of the context
    f(scope)
  }

  @targetName("Create Named Scope")
  def Scope(scopeName: String, f:ScopeDefinition => Unit): Unit = {
    println("Creating Named Scope")
    //create a execution context
    val scope = new ScopeDefinition(this)

    //get the name of the execution context, to create a binding for it
    f(scope)

    //create a binding for the context, so that it's bindings can be accessed later
    this.Assign.Variable(scopeName).toValue(scope)
  }

  @targetName("Get Named Scope")
  def Scope(scopeName: String): ScopeDefinition = {
    //search if the binding exists
    val incompleteBinding = bindings.Variable(scopeName)

    //if the value of the binding is not null, then the binding was found
    if incompleteBinding.getValue != null then
      //return the value as an execution context
      println("Found")
      val valueAsType = incompleteBinding.getValue
      //Check if the variable is bound to a Scope(ExecutionContext)
      if valueAsType.checkIfTypeScope then
        valueAsType.getValue.asInstanceOf[ScopeDefinition]
      else
        null
    else if parent != null then
    //Search the parent if the binding is not found
      parent.Scope(scopeName)
    else
      println("Not Found")
      //if the parent is null, we have reached the global scope and the binding was not found
      //return null as the binding was not found
      null
  }

  def ClassDef(className: String, f: ClassDefinition => Unit): Unit=
  {
    val classDefinition = new ClassDefinition(className, null)
    f(classDefinition)
    this.Assign.Variable(className).toValue(classDefinition)
  }
  
}
