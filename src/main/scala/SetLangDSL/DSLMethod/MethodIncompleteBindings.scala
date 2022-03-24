package SetLangDSL.DSLMethod

import SetLangDSL.DSLScope.ScopeIncompleteBinding
import scala.collection.mutable
import SetLangDSL.DSL.*

class MethodIncompleteBinding(name: String,
                              bindingMap: mutable.Map[String, Value],
                              value: Value = null) extends ScopeIncompleteBinding(name, bindingMap, value){


}
