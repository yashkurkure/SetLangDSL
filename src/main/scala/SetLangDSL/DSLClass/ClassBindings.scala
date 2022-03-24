package SetLangDSL.DSLClass

import SetLangDSL.DSLScope.{ScopeBindings, ScopeDefinition}
import scala.collection.mutable
import SetLangDSL.DSL.*


class ClassBindings(scopeDefinition: ScopeDefinition) extends ScopeBindings(scopeDefinition)
{

  private val accessBindingMap = mutable.Map.empty[String, accessSpecifier]

  override def Variable(name: String): ClassIncompleteBinding = {
    //check if the binding already exists, and is not null
    if(bindingMap.contains(name) && bindingMap(name) != null) then
    //println("Binding found for name: " + name)
      new ClassIncompleteBinding(Public, name, bindingMap, accessBindingMap, bindingMap(name))
    else
    //println("Creating incomplete binding for: " + name)
      new ClassIncompleteBinding(Public, name, bindingMap, accessBindingMap)
  }

  def Variable(access: accessSpecifier, name: String): ClassIncompleteBinding = {

    //check if the binding already exists, and is not null
    //check if the variable is private or not
    if(bindingMap.contains(name) && accessBindingMap(name) != Private) then
      println("Binding found for name: " + name)
      new ClassIncompleteBinding(access, name, bindingMap, accessBindingMap, bindingMap(name))
    else
      println("Creating incomplete binding for: " + name)
      new ClassIncompleteBinding(access, name, bindingMap, accessBindingMap)
  }


}
