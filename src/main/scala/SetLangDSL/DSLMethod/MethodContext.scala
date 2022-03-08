package SetLangDSL.DSLMethod

import SetLangDSL.DSLMethod.*
import SetLangDSL.DSLClass.*
import SetLangDSL.DSL.*
import SetLangDSL.DSLScope.{ScopeBindings, ScopeDefinition, ScopeIncompleteBinding}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
class MethodContext(
                     scopeIncompleteBinding: ScopeIncompleteBinding, classInstance: ClassInstance, methodDefinition: MethodDefinition) {

  val parameterBindings = mutable.Map.empty[String, Value]

  def withParameters(value: Any): MethodContext = {
    val parameters = methodDefinition.getParameters.parameters
    parameterBindings += (parameters(0)-> Value(value))
    this
  }

  def withParameters(values: Tuple): MethodContext = {
    val valuesAsArray = new ArrayBuffer[Any]
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
  
  def Execute: Unit = {
    val methodBindings = methodDefinition.bindings
    val bindingMap = methodBindings.bindingMap

    //Create the method parameter bindings
    parameterBindings.foreach((name, value)=>{
      bindingMap+=(name->value)
    })
    val body = methodDefinition.getBody
    val result = body(methodDefinition)

    //Remove the method parameter bindings
    parameterBindings.foreach((name, value)=>{
      bindingMap.remove(name)
    })
    scopeIncompleteBinding.toValue(result)
  }

}
