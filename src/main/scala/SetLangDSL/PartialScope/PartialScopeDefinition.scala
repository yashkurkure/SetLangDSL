package SetLangDSL.PartialScope

import scala.collection.mutable
import SetLangDSL.DSL
import SetLangDSL.DSLScope.{ScopeBinding, ScopeDefinition}

/**
 * PartialScopeDefinition
 *
 * This is similar to a normal scope, but this kind of scope handles undefined variables differently.
 *
 * Take for example:
 *  g.PartialScope{p=>
 *  p.AssignVariable("v1").Insert(1,2,3)
 *  p.AssignVariable("v2").Union(p.Variable("v1"), p.Variable("v2"))
 *  }.eval( )
 *
 * */
class PartialScopeDefinition(parent: ScopeDefinition, body: PartialScopeDefinition=>DSL.Value) extends ScopeDefinition(parent){


  val undefinedVariables: mutable.Map[String, DSL.Value] = mutable.Map.empty[String, DSL.Value]

  /**
   * AssignVariable (overridden)
   *
   * Not affected by partial evaluation
   * */
  override def AssignVariable(name: String): ScopeBinding = {

    // if an undefined variable is assigned a value, then remove it from undefinedVariables list
    if undefinedVariables.contains(name) then
      undefinedVariables.remove(name)

    super.AssignVariable(name)
  }

  /**
   * Variable (overridden)
   *
   * Used to access a variable
   * If the variable is undeclared we add to the undefinedVariables Map
   *
   * */
  override def Variable(name: String): DSL.Value = {
    val value = super.Variable(name)

    //The variable is already defined
    if value != null then return value

    //if the variable is undefined then add it to the undefinedVariables map

    // Only add it to the map if it does not exist in the map
    if !undefinedVariables.contains(name) then
      undefinedVariables.addOne(name, null)

    DSL.Value(null)
  }

  /**
   * evaluate
   *
   * This function allows the programmer to define any undefined variables
   *  that were previously left undefined.
   *
   *
   * */
  def evaluate(p: PartialScopeDefinition => Unit): DSL.Value = {
    p(this)
    body(this)
  }

}
