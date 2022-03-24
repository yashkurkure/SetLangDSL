package SetLangDSL.DSLMethod
import SetLangDSL.DSLClass.ClassIncompleteBinding
import SetLangDSL.DSLScope.ScopeIncompleteBinding
import SetLangDSL.Skeleton.Bindings
import SetLangDSL.DSL._

import scala.collection.mutable

class MethodBindings(methodContext: MethodDefinition)
  extends Bindings[MethodBindings](methodContext)
{

  val bindingMap: mutable.Map[String, Value] = mutable.Map.empty[String, Value]

  def Variable(name: String): MethodIncompleteBinding = {
    
    // The variable should be already be bound or
    // The variable should not be a parameter to the method
    if(bindingMap.contains(name) || methodContext.getParameters.parameters.contains(name)) then
    //println("Binding found for name: " + name)
      new MethodIncompleteBinding(name, bindingMap, bindingMap(name))
    else
    //println("Creating incomplete binding for: " + name)
      new MethodIncompleteBinding(name, bindingMap)
  }

  def Scope(name: String): ClassIncompleteBinding = {
    null
  }
}
