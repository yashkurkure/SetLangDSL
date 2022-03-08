package SetLangDSL.DSLScope

//Scala imports
import SetLangDSL.Skeleton.{Bindings, Definition, IncompleteBinding}
import scala.collection.mutable

import SetLangDSL.DSL.*

class ScopeBindings(context: ScopeDefinition)
  extends Bindings[ScopeBindings](context)
{

  val bindingMap: mutable.Map[String, Value] = mutable.Map.empty[String, Value]

  def Variable(name: String): ScopeIncompleteBinding = {
    if(bindingMap.contains(name)) then
    //println("Binding found for name: " + name)
      new ScopeIncompleteBinding(name, bindingMap, bindingMap(name))
    else
    //println("Creating incomplete binding for: " + name)
      new ScopeIncompleteBinding(name, bindingMap)
  }

  def Scope(name: String): ScopeIncompleteBinding = {
    if(bindingMap.contains(name)) then
      new ScopeIncompleteBinding(name, bindingMap, bindingMap(name))
    else
      new ScopeIncompleteBinding(name, bindingMap)
  }

}
