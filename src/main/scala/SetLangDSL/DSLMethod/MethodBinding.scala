package SetLangDSL.DSLMethod

import SetLangDSL.DSLScope.ScopeBinding
import scala.collection.mutable
import SetLangDSL.DSL.*

class MethodBinding(name: String,
                    bindingMap: mutable.Map[String, Value],
                    value: Value = null) extends ScopeBinding(name, bindingMap, value){


}
