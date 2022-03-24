package SetLangDSL.DSLMethod

import SetLangDSL.DSLScope.{ScopeBindings, ScopeDefinition}

class MethodBindings(methodDefinition: MethodDefinition) extends ScopeBindings(methodDefinition){

  override def Variable(name: String): MethodIncompleteBinding = {
    // The variable should be already be bound or
    // The variable should not be a parameter to the method
    if(bindingMap.contains(name) || methodDefinition.getParameters.parameters.contains(name)) then
    //println("Binding found for name: " + name)
      new MethodIncompleteBinding(name, bindingMap, bindingMap(name))
    else
    //println("Creating incomplete binding for: " + name)
      new MethodIncompleteBinding(name, bindingMap)
  }

}
