package SetLangDSL.DSLScope

import SetLangDSL.Skeleton.Bindings
import SetLangDSL.Value

import scala.collection.mutable

/**
 * ExecutionBindings
 *
 * The instance of this class holds the bindings of a certain execution context
 *
 * */
class ExecutionBindings(context: ExecutionContext)
{
  protected val bindingMap = mutable.Map.empty[String, Value]
  

  def Variable(name: String): ExecutionIncompleteBinding = {
    if(bindingMap.contains(name)) then
      //println("Binding found for name: " + name)
      new ExecutionIncompleteBinding(name, bindingMap, bindingMap(name))
    else
      //println("Creating incomplete binding for: " + name)
      new ExecutionIncompleteBinding(name, bindingMap)
  }

  def Scope(name: String): ExecutionIncompleteBinding = {
    if(bindingMap.contains(name)) then
      new ExecutionIncompleteBinding(name, bindingMap, bindingMap(name))
    else
      new ExecutionIncompleteBinding(name, bindingMap)
  }
  
}
