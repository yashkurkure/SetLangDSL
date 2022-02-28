package SetLangDSL.DSLScope

// Imports from Scala
import scala.annotation.targetName
import scala.collection.mutable

// Imports from DSL package
import SetLangDSL.Value
import SetLangDSL.DSLScope.ExecutionBindings
import SetLangDSL.DSLCLass.ClassContext

/**
 * ExecutionContext
 *
 * The instance of this class acts a context in which bindings are created for
 * variables and child scopes.
 *
 * */
class ExecutionContext(parent: ExecutionContext)
{
  protected val bindings = new ExecutionBindings(this)

  /**
   * Assign
   * Assign operation to create a binding in the current ExecutionContext
   *
   * returns the ExecutionBindings instance
   * */
  def Assign : ExecutionBindings ={
    bindings
  }

  /**
   * Variable(String)
   * This method is used to access a Variable with an active binding in the scope
   *
   * If no binding is found in the current scope, then the parent scope is called
   *
   * returns the value of the Variable that is bound to
   * if the binding is not found, it will return null
   * */
  /*def Variable(name: String): Any = {

    // Get the incomplete binding (This can tell us if the binding exists or not)
    val incompleteBinding = bindings.Variable(name)

    // If the value of the incomplete binding is not null, that means a binding exits
    if incompleteBinding.Value() != null then
      // return the value associated with the name
      incompleteBinding.Value()
    else if parent != null then
      //if not found in the current context, check the parent context
      parent.Variable(name)
    else
      //when the parent value is null, we have reached the global scope
      //since there is no outer scope, we will halt the search for the binding by returning null
      // returning null is the same as the binding not being found
      null
  }*/

  def Variable(name: String): Value = {

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


  /**
   * Scope(f: ExecutionContext => Unit): Unit
   *
   * Creates an anonymous child scope
   * */
  def Scope(f: ExecutionContext => Unit): Unit = {
    println("Creating Anonymous Scope")
    //create a execution context
    val scope = new ExecutionContext(this)
    //execute the user's operations of the context
    f(scope)
  }

  /**
   * Scope(f: ExecutionContext => String): Unit
   *
   * Creates a named child scope
   *
   * The named child scope can be accessed later using Scope(name:String)
   * */
  @targetName("NamedScope")
  def Scope(f: ExecutionContext => String): Unit = {
    println("Creating Named Scope")
    //create a execution context
    val scope = new ExecutionContext(this)

    //get the name of the execution context, to create a binding for it
    val scopeName = f(scope)

    //create a binding for the context, so that it's bindings can be accessed later
    this.Assign.Variable(scopeName).toValue(scope)
  }

  /**
   * Scope(name: String): Unit
   *
   * returns the ExecutionContext of a named scope, with an active binding
   * returns null if the name does not have a binding to a scope
   *
   * Example Usage:
   *                Scope("namedScope").Variable("v")
   *                This will get the value of the variable "v" in the scope "namedScope"
   * */
  def Scope(name: String): ExecutionContext = {

    //search if the binding exists
    val incompleteBinding = bindings.Variable(name)

    //if the value of the binding is not null, then the binding was found
    if incompleteBinding.getValue != null then
      //return the value as an execution context
      println("Found")
      val valueAsType = incompleteBinding.getValue
      //Check if the variable is bound to a Scope(ExecutionContext)
      if valueAsType.checkIfTypeScope then
        valueAsType.getValue.asInstanceOf[ExecutionContext]
      else
        null
    else if parent != null then
      //Search the parent if the binding is not found
      parent.Scope(name)
    else
      println("Not Found")
      //if the parent is null, we have reached the global scope and the binding was not found
      //return null as the binding was not found
      null
  }

  def ClassDef(name: String, f: ClassContext => Unit): ClassContext=
  {
    val classDefinition = new ClassContext(name)
    f(classDefinition)
    classDefinition
  }

}
