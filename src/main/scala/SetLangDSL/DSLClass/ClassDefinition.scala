package SetLangDSL.DSLClass

import SetLangDSL.DSL.*
import SetLangDSL.DSLInterface.InterfaceDefinition
import SetLangDSL.DSLMethod.{MethodContext, MethodDefinition}
import SetLangDSL.DSLScope.{ScopeBinding, ScopeDefinition}

import scala.collection.mutable

class ClassDefinition(name: String, parent: ClassDefinition) extends ScopeDefinition(parent)
{

  private def this(name: String, parent: ClassDefinition, classDefinition: ClassDefinition)={
    this(name, if (parent == null) null else parent.deepCopy())

    // Copy the mutable objects
    classDefinition.bindingMap.foreach((k,v)=>this.bindingMap.put(k,v))
    classDefinition.accessBindingMap.foreach((k,v)=>this.accessBindingMap.put(k,v))
    classDefinition.parameters.foreach(s=>this.parameters.addOne(s))
  }

  /**
   * Constructor
   * 
   * Constructs the class definition object using an InterfaceDefinition
   * */
  def this(name: String, parent: ClassDefinition, interface: InterfaceDefinition) = {
    this(name, parent)
    
    // add access specifier and binding names into the class definition
    val bindingNames = interface.loadBindings(this.accessBindingMap)
    bindingNames.foreach(s=>this.interfaceBindingNames.addOne(s))
  }


  /**
   * parameters
   * 
   * an ArrayBuffer that holds the names of the constructor parameters
   * 
   * At the time of instance creation this array buffer will be accessed to
   *  bind the parameter names to the values inputted by the programmer
   * 
   * */
  val parameters = new mutable.ArrayBuffer[String]

  // Experimental
  val accessBindingMap = mutable.Map.empty[String, accessSpecifier]

  //Used for interfaces
  val interfaceBindingNames: mutable.ArrayBuffer[String] = mutable.ArrayBuffer.empty[String]

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

  def getParent: ClassDefinition = parent

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
             f: MethodDefinition=>Value): Unit = {
    val method = new MethodDefinition(this, access, name, parameters, f)
    this.AssignVariable(access, name).toValue(method)
  }

  override def Variable(name: String): Value = {
    this.getField(name)
  }


  /**
   * getMethod
   *  parameters:
   *    name: Name of the method to get
   *    restrictToPublic: if true, only public bindings will be returned
   *
   * Returns a "MethodContext", which can be used to execute the method
   *
   * Access Specifier Rules:
   *
   * The ClassDefinition instance is never used by the user to call the method, it
   *  is only used to define the class. But each ClassInstance holds a deep copy of the
   *   ClassDefinition that the instance will use to handle bindings and call methods.
   *
   * Hence, when accessing methods from the ClassDefinition, we have the following cases:
   *  (provided a binding for the method definition exists)
   *
   * Method found in this class
   * 1) method is Public - allowed to access
   * 2) method is Private - allowed to access
   * 3) method is Protected = allowed to access
   *
   * Method found in parent class
   * 1) method is Public - allowed to access
   * 2) method is Private - NOT allowed to access
   * 3) method is Protected - allowed to access
   * */
  def getMethod(name: String, restrictToPublic: Boolean = false): MethodContext = {

    // Search for the binding in current class
    if this.bindingMap.contains(name) then
      if bindingMap(name).checkIfTypeMethodDefinition then
        val methodDefinition = bindingMap(name).getValue.asInstanceOf[MethodDefinition]
        if restrictToPublic && methodDefinition.getAccessSpecifier != Public then
          null
        else
          new MethodContext(methodDefinition.deepCopy())
      else
        // Case: Binding found, but the name does not refer to a MethodDefinition
        null
    else
      // Case: Binding not found, check parent class
      if this.parent != null then
        val methodContext = this.parent.getMethod(name, restrictToPublic)
        // But if the method is Private we cannot let the user access it
        if methodContext == null then
          // Case: Method not found in parent either
          null
        else if methodContext.getAccessSpecifier == Private then
          // Case: Method found in parent, but it is private
          null
        else
          // Case: method found in parent with access level: public or protected
          if restrictToPublic && methodContext.getAccessSpecifier != Public then
            null
          else
            methodContext
      else
        // Case: There is no parent class for this class
        null
  }


  /**
   * getField
   *  parameters:
   *    name: Name of the field to be searched
   *    restrictToPublic: if true, only public bindings will be returned
   *
   * The ClassDefinition instance is never used by the user to access a field, it
   *  is only used to define the class. But each ClassInstance holds a deep copy of the
   *   ClassDefinition that the instance will use to handle bindings to these fields.
   *
   * Field found in this class
   * 1) method is Public - allowed to access
   * 2) method is Private - allowed to access
   * 3) method is Protected = allowed to access
   *
   * Field found in parent class
   * 1) method is Public - allowed to access
   * 2) method is Private - NOT allowed to access
   * 3) method is Protected - allowed to access
   *
   * */
  def getField(name: String, restrictToPublic: Boolean = false): Value = {

    // Check if this class has the binding
    if bindingMap.contains(name) then
      if restrictToPublic && accessBindingMap(name) != Public then
        null
      else
        bindingMap(name)
    else
      // Case: This call does not have this binding
      // Check the parent
      // getFieldParent will only look for Public and Protected bindings
      getFieldParent(name, restrictToPublic)
  }

  /**
   * getFieldParent
   *  parameters:
   *    name: Name of the field to be searched
   *    restrictToPublic: if true, only public bindings will be returned
   *
   * Used to search for a field in the parent class(es)
   *
   * This will be used by the getField method to search the parent in case
   *  the binding is not found in this class.
   *
   * Note: If the binding is found in the parent, but it is private, then null will be returned
   * */
  private def getFieldParent(name: String, restrictToPublic: Boolean = false): Value = {
    if parent != null then
      if parent.bindingMap.contains(name) then
        if parent.accessBindingMap(name) != Private then
          if restrictToPublic && parent.accessBindingMap(name) != Public then
            null
          else
            parent.bindingMap(name)
        else
          // Case: Binding found, but is private
          null
      else
        // Case: Binding not found in parent
        // Look for it in the parent of the parent
        parent.getFieldParent(name, restrictToPublic)
    else
      // Case: parent == null, class has not parent
      null
  }


  def checkInterfaceImplementation: Boolean = {
    println(interfaceBindingNames.toSet)
    println(bindingMap.keySet)
    interfaceBindingNames.toSet subsetOf bindingMap.keySet
  }

  override def deepCopy(): ClassDefinition = {
    new ClassDefinition(name, parent, this)
  }

}
