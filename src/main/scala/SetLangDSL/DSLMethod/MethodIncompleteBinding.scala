package SetLangDSL.DSLMethod

//Scala imports
import scala.collection.mutable

//DSL imports
import SetLangDSL.Value
import SetLangDSL.DSLScope.ExecutionContext
import SetLangDSL.DSLScope.ExecutionBindings
import SetLangDSL.DSLScope.IncompleteBinding

class MethodIncompleteBinding(name: String, bindingMap: mutable.Map[String, Value], value: Value = null) 
  extends IncompleteBinding (name, bindingMap, value){

}
