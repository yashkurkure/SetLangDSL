package SetLangDSL.DSLCLass

// Imports from Scala
import SetLangDSL.DSLScope.ExecutionBindings

import scala.collection.mutable

// Imports from DSL
import SetLangDSL.Value
import SetLangDSL.DSL.accessSpecifier
import SetLangDSL.DSLMethod.MethodContext
import SetLangDSL.DSLScope.ExecutionContext

class ClassContext(name: String) 
  extends ExecutionContext(null)
{
  private val classBindings = new ClassBindings(this)
  
  //Use linked hashSet so the order of the parameters is preserved
  private val classParameters = mutable.LinkedHashSet.empty[String]
  
  def Constructor(f: ClassContext=>Unit): Unit = {
    f(this)
  }
  
  def Parameters(params: String*): Unit = {
    params.foreach(param=>classParameters.add(param))
  }
  
  def Method(access: accessSpecifier, name: String, f: MethodContext => Value) : Unit ={
    //create the method
    //def methodDefinition = new SetLangMethod()
    //f(methodDefinition) //Let the user create the method definition
    //bind the method to a name
  }

  // Creating class variables
  override def Assign: ClassBindings = {
    classBindings
  }
}
