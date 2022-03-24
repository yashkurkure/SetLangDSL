package SetLangDSL.DSLClass
import SetLangDSL.DSL.*
import SetLangDSL.DSLScope.ScopeIncompleteBinding
import scala.collection.mutable

class ClassIncompleteBinding(access: accessSpecifier,
                             name: String,
                             bindingMap: mutable.Map[String, Value],
                             accessBindingMap: mutable.Map[String, accessSpecifier],
                             value: Value = null
                            ) extends ScopeIncompleteBinding(name, bindingMap, value){

  override def toValue(value: Any): Unit = {
    bindingMap += (name->Value(value))
    accessBindingMap += (name->access)
  }

}
