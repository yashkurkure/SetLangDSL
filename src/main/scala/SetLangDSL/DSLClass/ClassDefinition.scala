package SetLangDSL.DSLClass

import SetLangDSL.DSL.*
import SetLangDSL.DSLMethod.MethodDefinition
import SetLangDSL.DSLScope.{ScopeDefinition, ScopeIncompleteBinding}

import scala.collection.mutable

class ClassDefinition(name: String, parent: ClassDefinition) extends ScopeDefinition(parent)
{



  //override val bindings: ClassBindings = new ClassBindings(this)
  val parameters = new mutable.ArrayBuffer[String]

  // Experimental
  val accessBindingMap = mutable.Map.empty[String, accessSpecifier]
  override def AssignVariable(name: String): ClassIncompleteBinding = {

    //check if the binding already exists, and is not null
    if(bindingMap.contains(name) && bindingMap(name) != null) then
    //println("Binding found for name: " + name)
      new ClassIncompleteBinding(Public, name, bindingMap, accessBindingMap, bindingMap(name))
    else
    //println("Creating incomplete binding for: " + name)
      new ClassIncompleteBinding(Public, name, bindingMap, accessBindingMap)

  }

  def AssignVariable(access: accessSpecifier, name: String): ClassIncompleteBinding = {
    //check if the binding already exists, and is not null
    //check if the variable is private or not
    if(bindingMap.contains(name) && accessBindingMap(name) != Private) then
      println("Binding found for name: " + name)
      new ClassIncompleteBinding(access, name, bindingMap, accessBindingMap, bindingMap(name))
    else
      println("Creating incomplete binding for: " + name)
      new ClassIncompleteBinding(access, name, bindingMap, accessBindingMap)
  }



  /**
   * Assign (overridden)
   *
   * returns ClassBindings instead of ScopeBindings
   * */
  //override def Assign: ClassBindings = bindings
  
  /**
   * getParameters
   * 
   * Get the Array buffer containing the constructor's parameters
   * */
  def getConstructorParameters: mutable.ArrayBuffer[String] = parameters

  /**
   * Constructor
   *
   * Defining the Constructor for the class
   * */
  def Constructor(parameters: Parameters, f: ClassDefinition=> Unit): Unit = {
    // Add the constructor parameters to the Array Buffer
    parameters.parameters.foreach(i=>this.parameters.addOne(i))
    
    // Create the bindings for the constructor
    f(this)
  }

  def Method(access: accessSpecifier,
             name: String,
             parameters: Parameters,
             f: MethodDefinition=>Value) = {
    val method = new MethodDefinition(this, access, name, parameters, f)
    this.AssignVariable(access, name).toValue(method)
  }

}
