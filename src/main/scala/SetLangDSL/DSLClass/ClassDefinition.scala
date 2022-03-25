package SetLangDSL.DSLClass

import SetLangDSL.DSL.*
import SetLangDSL.DSLMethod.{MethodContext, MethodDefinition}
import SetLangDSL.DSLScope.{ScopeBinding, ScopeDefinition}

import scala.collection.mutable

class ClassDefinition(name: String, parent: ClassDefinition) extends ScopeDefinition(parent)
{

  private def this(name: String, parent: ClassDefinition, classDefinition: ClassDefinition)={
    this(name, parent)

    // Copy the mutable objects
    classDefinition.bindingMap.foreach((k,v)=>this.bindingMap.put(k,v))
    classDefinition.accessBindingMap.foreach((k,v)=>this.accessBindingMap.put(k,v))
    classDefinition.parameters.foreach(s=>this.parameters.addOne(s))
  }



  //override val bindings: ClassBindings = new ClassBindings(this)
  val parameters = new mutable.ArrayBuffer[String]

  // Experimental
  val accessBindingMap = mutable.Map.empty[String, accessSpecifier]
  override def AssignVariable(name: String): ClassBinding = {

    //check if the binding already exists, and is not null
    if(bindingMap.contains(name) && bindingMap(name) != null) then
    //println("Binding found for name: " + name)
      new ClassBinding(Public, name, bindingMap, accessBindingMap, bindingMap(name))
    else
    //println("Creating incomplete binding for: " + name)
      new ClassBinding(Public, name, bindingMap, accessBindingMap)

  }

  def AssignVariable(access: accessSpecifier, name: String): ClassBinding = {
    //check if the binding already exists, and is not null
    //check if the variable is private or not
    if(bindingMap.contains(name) && accessBindingMap(name) != Private) then
      println("Binding found for name: " + name)
      new ClassBinding(access, name, bindingMap, accessBindingMap, bindingMap(name))
    else
      println("Creating incomplete binding for: " + name)
      new ClassBinding(access, name, bindingMap, accessBindingMap)
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

  def getMethod(name: String): MethodContext = {
    // We don't need to check access specifier as this can only be used inside the class
    if bindingMap.contains(name) then
      println("getMethod: Found method definition")
      if bindingMap(name).checkIfTypeMethodDefinition then
        println("getMethod: Of type methodDefinition")
        val methodDefinition = bindingMap(name).getValue.asInstanceOf[MethodDefinition]
        new MethodContext(methodDefinition.deepCopy())
      else
        null
    else
      null
  }


  override def deepCopy(): ClassDefinition = {
    new ClassDefinition(name, parent, this)
  }

}
