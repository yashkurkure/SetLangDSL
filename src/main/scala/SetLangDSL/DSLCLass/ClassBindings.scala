package SetLangDSL.DSLCLass

// Scala imports
import scala.collection.mutable

// DSL imports
import SetLangDSL.DSLScope.{ExecutionBindings, ExecutionIncompleteBinding}
import SetLangDSL.DSL.accessSpecifier
import SetLangDSL.DSL._
class ClassBindings(classContext: ClassContext) extends ExecutionBindings(classContext)
{
  private val accessBindingMap = mutable.Map.empty[String, accessSpecifier]
  
  //creates public scope in class
  //Todo
  override def Scope(name: String): ClassIncompleteBinding = {
    null
  }

  //creates public variable
  //Todo
  override def Variable(name: String): ClassIncompleteBinding = {
    if(bindingMap.contains(name)) then
      new ClassIncompleteBinding(accessBindingMap(name), name, bindingMap, accessBindingMap, bindingMap(name))
    else
      new ClassIncompleteBinding(Public, name, bindingMap, accessBindingMap)
  }
  
  //create a public, private or protected variable
  //Todo
  def Variable(access: accessSpecifier, name:String): ClassIncompleteBinding = {
    if(bindingMap.contains(name)) then
      new ClassIncompleteBinding(accessBindingMap(name), name, bindingMap, accessBindingMap, bindingMap(name))
    else
      new ClassIncompleteBinding(access, name, bindingMap, accessBindingMap)
  }
}
