package SetLangDSL.DSLInterface

import SetLangDSL.DSL.*
import SetLangDSL.DSLClass.ClassBinding
import SetLangDSL.DSLMethod.MethodDefinition
import SetLangDSL.DSLScope.ScopeDefinition

import scala.collection.mutable

class InterfaceDefinition(name: String) extends ScopeDefinition(null){

  private def this(name: String, interfaceDefinition: InterfaceDefinition) = {
    this(name)
    interfaceDefinition.bindingMap.foreach((k,v)=>this.bindingMap.put(k,v))
    interfaceDefinition.accessBindingMap.foreach((k,v)=>this.accessBindingMap.put(k,v))
  }


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

  val accessBindingMap = mutable.Map.empty[String, accessSpecifier]
  override def AssignVariable(name: String): InterfaceBinding = {
    AssignVariable(Public, name)
  }

  def AssignVariable(access: accessSpecifier, name: String): InterfaceBinding = {
    if !bindingMap.contains(name) then
      bindingMap.put(name, null)
      accessBindingMap.put(name, access)
    null
  }


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
    bindingMap.put(name, Value(method))
    accessBindingMap.put(name, access)
  }

  override def deepCopy(): InterfaceDefinition = {
    new InterfaceDefinition(name, this)
  }

  def loadBindings(): mutable.ArrayBuffer[String] = {

    val bindingNames: mutable.ArrayBuffer[String] = mutable.ArrayBuffer.empty[String]
    this.bindingMap.foreach((k,v)=>{
      bindingNames.addOne(k)
    })
    this.accessBindingMap.foreach((k,v)=>{
      bindingNames.addOne(k)
    })
    bindingNames
  }

}
