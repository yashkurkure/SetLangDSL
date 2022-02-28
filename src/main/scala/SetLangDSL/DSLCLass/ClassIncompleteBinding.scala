package SetLangDSL.DSLCLass

//Scala imports
import scala.collection.mutable

// DSL imports
import SetLangDSL.DSL.accessSpecifier
import SetLangDSL.Value
import SetLangDSL.DSLScope.ExecutionIncompleteBinding

class ClassIncompleteBinding(
                              access: accessSpecifier, 
                              name: String, 
                              bindingMap: mutable.Map[String, Value], 
                              accessBindingMap : mutable.Map[String, accessSpecifier],
                              value: Value = null) 
  extends ExecutionIncompleteBinding(name, bindingMap, value) {
  
  override def toValue(value:Any): Unit = {
    accessBindingMap += (name->access)
    bindingMap += (name->Value(value))
  }

}
