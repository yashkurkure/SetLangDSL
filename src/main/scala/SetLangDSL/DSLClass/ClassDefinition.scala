package SetLangDSL.DSLClass

import SetLangDSL.DSL.*
import SetLangDSL.DSLMethod.MethodDefinition
import SetLangDSL.DSLScope.{ScopeBindings, ScopeDefinition}

import scala.collection.mutable

class ClassDefinition(name: String, parent: ClassDefinition) extends ScopeDefinition(parent)
{
  override val bindings: ClassBindings = new ClassBindings(this)
  val parameters = new mutable.ArrayBuffer[String]

  /**
   * Assign (overridden)
   *
   * returns ClassBindings instead of ScopeBindings
   * */
  override def Assign: ClassBindings = bindings
  
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
    this.Assign.Variable(access, name).toValue(method)
  }

}
