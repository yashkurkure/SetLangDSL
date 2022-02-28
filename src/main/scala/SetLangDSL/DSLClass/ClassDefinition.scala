package SetLangDSL.DSLClass

// Imports from Scala
import SetLangDSL.DSLScope.ScopeDefinition
import SetLangDSL.Parameters
import SetLangDSL.Skeleton.Definition

import scala.annotation.targetName
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

// Imports from DSL
import SetLangDSL.Value
import SetLangDSL.DSL.accessSpecifier
import SetLangDSL.DSLScope.ScopeDefinition
import SetLangDSL.DSLMethod.MethodDefinition
import SetLangDSL.Skeleton._


class ClassDefinition(name: String, parent: ClassDefinition)
  extends Definition[ClassDefinition](parent)
{

  val bindings: ClassBindings = new ClassBindings(this)
  val parameters = new ArrayBuffer[String]

  def Constructor(f: ClassDefinition=>Unit): Unit = {
    f(this)
  }

  def Method(access: accessSpecifier, 
             name: String,
             parameters: Parameters,
             f: MethodDefinition => Value) : Unit ={
    
    val method = new MethodDefinition(access, name, parameters, f)
    this.Assign.Variable(access, name).toValue(method)
  }


  def Assign: ClassBindings = {
      bindings
    }

  def Variable(name: String): Value = {
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

  @targetName("Create Anonymous Scope")
  def Scope(f:ScopeDefinition=>Unit): Unit = null

  @targetName("Create Named Scope")
  def Scope(scopeName: String, f: ScopeDefinition => Unit): Unit = null

  @targetName("Get Named Scope")
  def Scope(scopeName: String): ClassDefinition = {
    null
  }
}
