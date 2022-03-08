package SetLangDSL.DSLClass

// Scala imports

import scala.collection.mutable

// DSL imports
import SetLangDSL.DSL.accessSpecifier
import SetLangDSL.DSL._
import SetLangDSL.Skeleton._

class ClassBindings(classContext: ClassDefinition) extends Bindings[ClassBindings](classContext)
{

  val bindingMap: mutable.Map[String, Value] = mutable.Map.empty[String, Value]
  private val accessBindingMap = mutable.Map.empty[String, accessSpecifier]

  def Variable(name: String): ClassIncompleteBinding = {
    
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
  

  def Scope(name: String): ClassIncompleteBinding = {
    null
  }
}
