package SetLangDSL.DSLMethod

//Scala imports
import scala.collection.mutable

//DSL imports
import SetLangDSL.Value
import SetLangDSL.DSLScope.ExecutionContext
import SetLangDSL.DSLScope.ExecutionBindings
import SetLangDSL.DSLScope.ExecutionIncompleteBinding

class MethodIncompleteBinding(name: String, bindingMap: mutable.Map[String, Value], value: Value = null) 
  extends ExecutionIncompleteBinding (name, bindingMap, value){

}
