package SetLangDSL.DSLMethod

//Scala imports
import scala.collection.mutable

//DSL imports
import SetLangDSL.DSL.accessSpecifier
import SetLangDSL.DSLScope.ExecutionContext
import SetLangDSL.Value

/**
 * SetLangMethod
 * 
 * This class extends ExecutionContext
 * A method is also a type of a scope, in which you can define variables and scopes
 *
 * The parent value for the execution context of a method is null, as the DSL does not support
 *  methods inside methods
 * */
class MethodContext(val access: accessSpecifier, val name: String) 
  extends ExecutionContext(null){
  
  val methodParameters = mutable.LinkedHashSet.empty[String]
  
  def Parameters(params: String*): Unit = {
    params.foreach(param=>methodParameters.add(param))
  }

}
