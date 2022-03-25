package SetLangDSL.DSLMethod


// Scala Import
import scala.collection.mutable

// DSL Imports
import SetLangDSL.DSL.Value
import SetLangDSL.DSL.*
import SetLangDSL.DSLClass.ClassInstance
import SetLangDSL.DSLScope.ScopeBinding


class MethodContext(methodDefinition: MethodDefinition) {

  //val parameterBindings = mutable.Map.empty[String, Value]
  
  def getAccessSpecifier: accessSpecifier = {
    methodDefinition.getAccessSpecifier
  }

  def withParameters(value: Any): MethodContext = {
    val parameters = methodDefinition.getParameters.parameters
    methodDefinition.bindingMap += (parameters(0)-> Value(value))
    this
  }

  def withParameters(values: Tuple): MethodContext = {
    val valuesAsArray = new mutable.ArrayBuffer[Any]
    values.productIterator.foreach(value=>valuesAsArray.addOne(value))
    val parameters = methodDefinition.getParameters.parameters
    if parameters.size == valuesAsArray.size then{
      for( i <- 0 to parameters.size){
        methodDefinition.bindingMap += (parameters(i)->Value(valuesAsArray(i)))
      }
      this
    }
    else
      null
  }

  //TODO: Execution of the method
  def Execute: Value = {
    methodDefinition.getBody(methodDefinition)
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
