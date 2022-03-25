package SetLangDSL.DSLMethod


// Scala Import
import scala.collection.mutable

// DSL Imports
import SetLangDSL.DSL.Value
import SetLangDSL.DSLClass.ClassInstance
import SetLangDSL.DSLScope.ScopeIncompleteBinding


class MethodContext(classInstance: ClassInstance, methodDefinition: MethodDefinition) {

  val parameterBindings = mutable.Map.empty[String, Value]

  def withParameters(value: Any): MethodContext = {
    val parameters = methodDefinition.getParameters.parameters
    parameterBindings += (parameters(0)-> Value(value))
    this
  }

  def withParameters(values: Tuple): MethodContext = {
    val valuesAsArray = new mutable.ArrayBuffer[Any]
    values.productIterator.foreach(value=>valuesAsArray.addOne(value))
    val parameters = methodDefinition.getParameters.parameters
    if parameters.size == valuesAsArray.size then{
      for( i <- 0 to parameters.size){
        parameterBindings += (parameters(i)->Value(valuesAsArray(i)))
      }
      this
    }
    else
      null
  }

  //TODO: Execution of the method
  def Execute: Value = {

    // Get the parameters for the method
    val parameters: Seq[String] = methodDefinition.getParameters.parameters
    // Where should these parameters go?
    // In the methodDefinition's Bindings?
    // or the MethodContext's bindings?
    // Solution=> Each method context gets a deep copy of it's methodDefinition



    null
  }

//  def Execute: Unit = {
//    val methodBindings = methodDefinition.bindings
//    val bindingMap = methodBindings.bindingMap
//
//    //Create the method parameter bindings
//    parameterBindings.foreach((name, value)=>{
//      bindingMap+=(name->value)
//    })
//    val body = methodDefinition.getBody
//    val result = body(methodDefinition)
//
//    //Remove the method parameter bindings
//    parameterBindings.foreach((name, value)=>{
//      bindingMap.remove(name)
//    })
//    scopeIncompleteBinding.toValue(result)
//  }

}
