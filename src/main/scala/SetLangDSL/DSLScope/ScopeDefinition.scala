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
import SetLangDSL.DSLClass.ClassInstance
import SetLangDSL.DSLInterface.InterfaceDefinition

import scala.annotation.targetName
import scala.collection.mutable
// DSL Imports
import SetLangDSL.DSL.*
import SetLangDSL.concatStrings
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


  /**
   * Private constructor
   * - Used for creating a deep copy of objects of this class
   * */
  private def this(parent: ScopeDefinition, scopeDefinition: ScopeDefinition) = {
    this(parent)
    scopeDefinition.bindingMap.foreach((k,v)=>this.bindingMap.put(k,v))
  }

  /**
   * deepCopy
   *
   * returns a deep copy of the current object
   * */
  def deepCopy(): ScopeDefinition = {

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return null

    //Fields that would need copying
    // bindingMap
    new ScopeDefinition(parent, this)
  }

  /**
   * bindingMap
   *
   * Contains all the (NAME:String -> value: Value) bindings
   *  this would include all:
   *    - Variable
   *    - ScopeDefinition
   *    - ClassDefinition
   *    - ClassInstance
   *    - InterfaceDefinition
   *
   * */
  val bindingMap: mutable.Map[String, Value] = mutable.Map.empty[String, Value]

  /**
   * messages
   *
   * This is a queue that holds messages
   *  the messages can be used to let modify the actions of operations
   *
   *  For example in the case of exceptions:
   *    - An exception is thrown
   *    - the throw method will add a message to the queue stating a message was thrown
   *    - the following statements in the scope after the throw will read this message
   *        and they would not modify the state of the bindings in the scope
   *    - once catch is called, the exception message will the dequeued and handled
   *    - the operations hence forth the catch will run normally there after.
   *
   *
   * */
  val messages: mutable.Queue[Message] = mutable.Queue.empty[Message]


  /**
   * AssignVariable
   *
   * Returns the bindings, on which you can
   *  call operations to create new bindings
   * */
  def AssignVariable(name: String):ScopeBinding = {

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return new ScopeBinding(null, null, null)

    if bindingMap.contains(name) then
      new ScopeBinding(name, bindingMap, bindingMap(name))
    else
      new ScopeBinding(name, bindingMap)
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

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return Value(null)

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

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return

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

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return

    //create a scope definition
    val scope = new ScopeDefinition(this)
    //execute the programmer's code for the scope
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

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return

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

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return null

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

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return

    val classDefinition = new ClassDefinition(className, null)
    f(classDefinition)
    this.AssignVariable(className).toValue(classDefinition)
  }

  /**
   * ClassDef()
   *
   * Serves as an entry point to create a Class
   * - This method will allow extending other classes
   * */
  def ClassDef(className: String, extend: Extends, f:ClassDefinition=>Unit): Unit = {

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return


    //First find the className that we need to extend
    val parentClassName = extend.className

    //Check if the definition for the parentClass exists
    val parentClassDefinition = this.Variable(parentClassName)

    if parentClassDefinition != null && parentClassDefinition.checkIfTypeClassDefinition then
      val classDefinition = new ClassDefinition(className, parentClassDefinition.getValue.asInstanceOf[ClassDefinition])
      f(classDefinition)
      this.AssignVariable(className).toValue(classDefinition)
    else
      throw Exception(concatStrings("Could not find definition of parent class: ", parentClassName))
  }

  /**
   * ClassDef()
   *
   * Serves as an entry point to create a Class
   * - This method will allow implementing interfaces
   * */
  def ClassDef(className: String, implement: Implements, f: ClassDefinition=>Unit): Unit = {

    // Check for any raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return

    // Get the interface Name
    val interfaceName = implement.interfaceName

    // Look for the interface definition
    val interfaceDefinition = this.Variable(interfaceName)

    // If a binding is found and the name is bound to an interface definition
    if interfaceDefinition != null && interfaceDefinition.checkIfTypeInterfaceDefinition then

      // Create a class definition
      //  pass the interface definition to it
      val classDefinition = new ClassDefinition(
        className, // class name
        null, // no parent scope
        interfaceDefinition.getValue.asInstanceOf[InterfaceDefinition] //interface definition
      )

      // Load the programmer's code into the class
      f(classDefinition)

      // Check if the programmer implemented all the members of the interface
      if !classDefinition.checkInterfaceImplementation then
        // if not throw an exception
        throw Exception("All methods/fields of interface not implemented")

      // Create a binding for the interface
      this.AssignVariable(className).toValue(classDefinition)

    // If the interface definition was not found throw an exception
    else
      throw Exception(concatStrings("Could not find definition for interface: ", interfaceName))
  }

  /**
   * InterfaceDef()
   *
   * To define a new interface
   * */
  def InterfaceDef(interfaceName: String, f: InterfaceDefinition=>Unit): Unit = {

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return

    val interfaceDefinition = new InterfaceDefinition(interfaceName, null)
    f(interfaceDefinition)
    this.AssignVariable(interfaceName).toValue(interfaceDefinition)
  }

  /**
   * InterfaceDef()
   *
   * To define a new interface that implements another interface
   *
   *
   *
   * */
  def InterfaceDef(interfaceName: String, implements: Implements, f: InterfaceDefinition=>Unit): Unit = {

    // Check for raised exceptions
    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return

    // Parent interface's name
    val parentInterfaceName = implements.interfaceName

    // Get the parent interface's definition
    val _parentInterfaceDefinition: Value = this.Variable(parentInterfaceName)

    // If a binding is found and the name is bound to an interfaceDefinition
    if _parentInterfaceDefinition != null && _parentInterfaceDefinition.checkIfTypeInterfaceDefinition then
      //TODO
      val parentInterfaceDefinition: InterfaceDefinition = _parentInterfaceDefinition.getValue.asInstanceOf[InterfaceDefinition]

      val interfaceDefinition = new InterfaceDefinition(interfaceName, parentInterfaceDefinition)

      // Add the programmers methods/fields
      f(interfaceDefinition)

      //Create a binding
      this.AssignVariable(interfaceName).toValue(interfaceDefinition)

    // If the interface definition was not found
    else
      throw Exception(concatStrings("Could not find definition for interface: ", parentInterfaceName))


  }

  /**
   *
   *
   * */
  def ExceptionClassDef(className: String, f: ClassDefinition => Unit): Unit = {

    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then return

    this.InterfaceDef("Exception", c=>{
      c.AssignVariable("reason")
    })

    this.ClassDef(className, Implements("Exception"), f)
  }

  /**
   * Conditional()
   *
   * Given an expression that evaluates to a Value object
   *  either the scope ifTrue or ifFalse
   *  will be executed depending on the Value object's evalAsBoolean
   *  method's return value
   * */
  def Conditional(expression: Value, ifTrue:ScopeDefinition=>Unit, ifFalse: ScopeDefinition=>Unit): Unit = {

    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      return

    //println("Creating Anonymous Scope")
    //create a scope definition
    val scope = new ScopeDefinition(this)

    // evaluate the expression's value as a Boolean
    if expression.evalAsBoolean then
      // true case
      ifTrue(scope)
    else
      // false case
      ifFalse(scope)
  }

  private def addMessageToParent(msg: Message): Unit = {
    if parent!= null then
      parent.messages.enqueue(msg)
  }


  //TODO
  def ThrowException(className: String): Unit = {

    //Search for the class definition
    val exceptionClassDefinition = this.Variable(className)

    // If a binding is found and the name is bound to a class definition
    if exceptionClassDefinition != null && exceptionClassDefinition.checkIfTypeClassDefinition then
      //create an instance of the exception class
      val exceptionClassInstance = new ClassInstance(exceptionClassDefinition.getValue.asInstanceOf[ClassDefinition])

      //create a message for the exception
      val exceptionMsg = new Message(RAISED_EXCEPTION)
      exceptionMsg.extrasMap.put("exception", exceptionClassInstance)

      //add the exception to the message queue
      messages.enqueue(exceptionMsg)
      addMessageToParent(exceptionMsg)
    else
      throw Exception(concatStrings("Class not found: ", className))
  }

  //TODO
  def Catch(className: String, f: ScopeDefinition=>Unit): Unit = {

    if messages.nonEmpty && messages.front.what == RAISED_EXCEPTION then
      val msg = messages.dequeue
      val exceptionClassInstance = msg.extrasMap.get("exception")
      f(this)
  }


}
