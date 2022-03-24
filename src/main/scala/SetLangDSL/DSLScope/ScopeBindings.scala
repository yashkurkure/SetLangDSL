package SetLangDSL.DSLScope

import SetLangDSL.DSL.Value

import scala.collection.mutable

class ScopeBindings(scope: ScopeDefinition) {

  val bindingMap: mutable.Map[String, Value] = mutable.Map.empty[String, Value]

  def Variable(name: String): ScopeIncompleteBinding = {
    if(bindingMap.contains(name)) then
    //println("Binding found for name: " + name)
      new ScopeIncompleteBinding(name, bindingMap, bindingMap(name))
    else
    //println("Creating incomplete binding for: " + name)
      new ScopeIncompleteBinding(name, bindingMap)
  }

}
