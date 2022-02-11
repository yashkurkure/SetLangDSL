package SetLangDSL

import SetLangDSL.SetLang.construct
import SetLangDSL.SetLang.construct.*

import scala.collection.mutable.Map

class scope(name: String, body: construct, parentScope: scope) {

  private val bindings = Map.empty[String, Any]

  def getName(): String = name

  def createInternalScope(name: String, body: construct): construct={
    //Check if the name is already bound to something
    if bindings.contains(name) then
      Value(null)
    else
      //Create a new SetLangDSL.scope
      val childScope = new scope(name, body, this)
      //Create a binding for the child SetLangDSL.scope
      bindings += (name -> childScope)
      Value(true)
  }

  def searchBinding(name: String):construct={
    if parentScope == null then // look in global SetLangDSL.scope
      if bindings.contains(name) then
        Value(bindings.get(name))
      else
        Value(null)

    else  // search in local SetLangDSL.scope or above
      if bindings.contains(name) then
        Value(bindings.get(name))
      else
        parentScope.searchBinding(name)
  }

  def createBinding(name: String, value: Any): construct={
    if bindings.contains(name) then //name is already bound to something
      Value(null)
    else //create the binding, since no name is bound to it
      bindings += (name -> value)
      Value(value)

  }

  def evaluateScope(): construct =
  {
    body.evalInScope(this)
  }


  def createAnonymousScope():Any={
    null
  }

}