package SetLangDSL.DSLClass
import SetLangDSL.DSL.*
import SetLangDSL.DSLScope.ScopeBinding
import scala.collection.mutable

class ClassBinding(access: accessSpecifier,
                   name: String,
                   bindingMap: mutable.Map[String, Value],
                   accessBindingMap: mutable.Map[String, accessSpecifier],
                   value: Value = null
                            ) extends ScopeBinding(name, bindingMap, value){

  override def toValue(value: Any): Unit = {
    bindingMap += (name->Value(value))
    accessBindingMap += (name->access)
  }

}
