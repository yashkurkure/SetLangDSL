/*
 *
 *  Copyright (c) 2022. Yash Kurkure. All rights reserved.
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */
package SetLangDSL.DSLScope

// Scala Imports
import scala.annotation.targetName
import scala.collection.mutable
// DSL Imports
import SetLangDSL.DSL.*
import SetLangDSL.DSLClass.ClassDefinition



/**
 * class ScopeDefinition
 *
 * This class represents a scope
 *
 * Constructors
 * ScopeDefinition(parent: ScopeDefinition)
 *    parent: Is the parent scope for the newly defined scope
 *
 * */
class ScopeDefinition(parent: ScopeDefinition) {


  // Book keeping for the scope's bindings
  //val bindings: ScopeBindings = new ScopeBindings(this)

  // Experimental
  val bindingMap: mutable.Map[String, Value] = mutable.Map.empty[String, Value]


  /**
   * Assign
   *
   * Returns the bindings, on which you can
   *  call operations to create new bindings
   * */
//  def Assign: ScopeBindings = {
//    bindings
//  }

  //Experimental
  def AssignVariable(name: String):ScopeIncompleteBinding = {
    if bindingMap.contains(name) then
      new ScopeIncompleteBinding(name, bindingMap, bindingMap(name))
    else
      new ScopeIncompleteBinding(name, bindingMap)
  }


  /**
   * Variable
   *  parameters:
   *    name: Variable name (binding name)
   *
   * Used to access an existing binding.
   * The value of the binding is returned using Value()
   * If the binding does not exist, it would return null
   * */
  def Variable(name: String): Value= {
    // Get the incomplete binding (This can tell us if the binding exists or not)
    val incompleteBinding = AssignVariable(name)

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
   * ExecuteMacro
   *  parameters:
   *    macroName: Name of the macro to be executed
   *    variable: Variable on which the macro is to be executed
   *
   * Used to execute a macro
   * Example:
   *  s.ExecuteMacro("myMacro", s.Variable("x"))
   * */
  def ExecuteMacro(macroName: String, variable: Value): Unit = {
    val macroBody = this.Variable(macroName).getValue.asInstanceOf[Value=>Unit]
    macroBody(variable)
  }


  /**
   * Scope
   *  parameters:
   *    f: A function that takes in the scope definition
   *
   * Used to create a child anonymous scope
   * An anonymous scope is not stored in the bindings, thus
   *  once it is executed, all its data would disappear
   * */
  @targetName("Create Anonymous Scope")
  def Scope(f:ScopeDefinition=>Unit): Unit = {
    //println("Creating Anonymous Scope")
    //create a execution context
    val scope = new ScopeDefinition(this)
    //execute the user's operations of the context
    f(scope)
  }


  /**
   * Scope
   *  parameters:
   *    scopeName: string representing the name of the scope
   *    f: A function that takes in the scope definition
   *
   * Used to create a child named scope
   * Named scope is stored in the bindings, thus
   *  once it is executed, it's data can be accessed later
   * */
  @targetName("Create Named Scope")
  def Scope(scopeName: String, f:ScopeDefinition => Unit): Unit = {
    //println("Creating Named Scope")
    //create a execution context
    val scope = new ScopeDefinition(this)

    //get the name of the execution context, to create a binding for it
    f(scope)

    //create a binding for the context, so that it's bindings can be accessed later
    this.AssignVariable(scopeName).toValue(scope)
  }


  /**
   * Scope
   *  parameters:
   *    scopeName: string representing the name of a scope
   *
   * Used to access a named scope
   * One can access all the bindings of the named scope by using
   *  this as an entry point.
   * */
  @targetName("Get Named Scope")
  def Scope(scopeName: String): ScopeDefinition = {
    //search if the binding exists
    val incompleteBinding = AssignVariable(scopeName)

    //if the value of the binding is not null, then the binding was found
    if incompleteBinding.getValue != null then
      //return the value as an execution context
      //println("Found")
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
    //println("Not Found")
    //if the parent is null, we have reached the global scope and the binding was not found
    //return null as the binding was not found
      null
  }


  /**
   * ClassDef()
   *
   * Serves as an entry point to create a Class
   *
   * */
  def ClassDef(className: String, f: ClassDefinition => Unit): Unit = {
    val classDefinition = new ClassDefinition(className, null)
    f(classDefinition)
    this.AssignVariable(className).toValue(classDefinition)
  }

  // Todo: Create a deep copy of this class
  def deepCopy(): ScopeDefinition = {
    //Fields that would need copying
    // bindings


    null
  }

}
