package SetLangDSL.DSLClass
import scala.collection.mutable
import SetLangDSL.Value

import scala.collection.mutable.ArrayBuffer
class ClassInstance(classDefinition: ClassDefinition) {
  
  def getDefinition: ClassDefinition = classDefinition
  
  val classBindings = mutable.Map.empty[String, Value]

  def withParameters(value: Any): Unit = {
    val parameters = classDefinition.parameters
    classBindings += (parameters(0) -> Value(value))
  }

  def withParameters(values: Tuple): Unit = {
    val valuesAsArray = new ArrayBuffer[Any]
    values.productIterator.foreach(value=>valuesAsArray.addOne(value))
    val parameters = classDefinition.parameters
    if(parameters.size == valuesAsArray.size) then
      for( i <- 0 to parameters.size){
        classBindings += (parameters(i)->Value(valuesAsArray(i)))
      }
  }

}