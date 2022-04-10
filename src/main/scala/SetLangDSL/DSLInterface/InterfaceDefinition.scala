package SetLangDSL.DSLInterface

import SetLangDSL.DSL.*
import SetLangDSL.DSLClass.ClassBinding
import SetLangDSL.DSLMethod.MethodDefinition
import SetLangDSL.DSLScope.ScopeDefinition

import scala.collection.mutable

/**
 * InterfaceDefinition
 *
 * This class represents an interface
 *
 *  When an interface is created in a scope, a binding would be created
 *    between the interfaceName and the instance of this class
 *
 * */
class InterfaceDefinition(name: String, parent: InterfaceDefinition) extends ScopeDefinition(null){

  /**
   * Private constructor
   *
   * Used to create deep copies
   * */
  private def this(name: String, parent: InterfaceDefinition, interfaceDefinition: InterfaceDefinition) = {
    this(name, parent)
    interfaceDefinition.bindingMap.foreach((k,v)=>this.bindingMap.put(k,v))
    interfaceDefinition.accessBindingMap.foreach((k,v)=>this.accessBindingMap.put(k,v))
  }

  /**
   * deepCopy
   *
   * Returns a deep copy of the current instance
   *
   * */
  override def deepCopy(): InterfaceDefinition = {
    new InterfaceDefinition(name, this)
  }

  /**
   * Holds the access specifiers for members
   *
   * String => accessSpecifier
   *
   * */
  val accessBindingMap = mutable.Map.empty[String, accessSpecifier]


  /**
   * Notes:
   *  Extending other interfaces:
   *    Case 1: Interfaces don't have common methods/fields
   *    Case 2: Interfaces have common fields:
   *            Fields can have different access specifiers, in that case use:
   *            User can choose which interface's implementation to use
   *    Case 3: Interfaces have common methods:
   *            Methods can have different access specifiers and Parameters, in that case use:
   *            User can choose which interface's methods to use
   * */


  /**
   * AssignVariable
   *
   * Assigns a variable in the interface, default access specifier is Public
   *
   * Note: In the case of interface the fields cannot hold a default value
   *
   * The value for the variable will have to defined by the programmer in the class
   *  that implements the interface
   *
   * */
  override def AssignVariable(name: String): InterfaceBinding = {
    AssignVariable(Public, name)
  }


  /**
   * AssignVariable
   *
   * Assigns a variable in the interface, this overloaded version allows
   *  setting a access specifier
   *
   *  Note: In the case of interfaces, the fields cannot be hold a default value
   *
   * */
  def AssignVariable(access: accessSpecifier, name: String): InterfaceBinding = {
    if !bindingMap.contains(name) then
      bindingMap.put(name, null)
      accessBindingMap.put(name, access)
    null
  }

  /**
   * Method
   *
   * Used to create a method in the interface
   *
   * Note that this does not define e body for the method
   *  The body of the method will be defined by the programmer
   *    in the class that implements this method
   *
   * */
  def Method(access: accessSpecifier,
             name: String,
             parameters: Parameters) = {

    // For a method to be abstract
    // 1. The parent scope(the scope of the class that implements it) is unknown, so it will be null
    // 2. The body of the method is not defined yet, so the body parameter is null
   val method = new MethodDefinition(
      null, // ScopeDefinition, i.e the scope in which the method will be defined
      access,
      name,
      parameters,
      null  // Method body, i.e the statements that will be executed when the method is executed
    )
    // add the method definition to the binding map
    bindingMap.put(name, Value(method))
    // add the method's accessSpecifier to the accessBindingMap
    accessBindingMap.put(name, access)
  }

  /**
   * loadBindings
   * */
  def loadBindings(accessBindingMap: mutable.Map[String, accessSpecifier]): mutable.ArrayBuffer[String] = {

    val bindingNames: mutable.ArrayBuffer[String] = mutable.ArrayBuffer.empty[String]
    // The parent's access binding map is loaded
    if parent != null then 
      // load the parent bindings into binding names and access specifier
      val parentBindingNames = parent.loadBindings(accessBindingMap)
      parentBindingNames.foreach(k=>bindingNames.addOne(k))
    
    // Load the access specifier information into the accessBindingMap
    this.accessBindingMap.foreach((k,v) => {
      
      // We need to check if the parent was defining an access specifier
      // Note: The parent interface's definition takes the precedence over the child's
      if !accessBindingMap.contains(k) then
        //only add the access binding if the parent had not already added one
        accessBindingMap.addOne(k,v)
    })
    
    // Load all the binding names into a array buffer
    this.bindingMap.foreach((k,v)=>{
      if !bindingNames.contains(k) then
        bindingNames.addOne(k)
    })
    
    
    //return the array buffer
    bindingNames
  }

}
