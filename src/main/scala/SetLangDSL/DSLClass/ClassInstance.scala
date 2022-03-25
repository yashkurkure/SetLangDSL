/*
 *
 *  Copyright (c) 2022. Yash Kurkure. All rights reserved.
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */
package SetLangDSL.DSLClass

// Scala Imports
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

// DSL Imports
import SetLangDSL.DSLMethod.*
import SetLangDSL.DSL.*
import SetLangDSL.*

/**
 * ClassInstance
 *
 * Each instance of this class handles the runtime objects created of defined classes.
 *      Handling includes, bindings, access to fields and methods etc.
 *
 * Constructors:
 *  ClassInstance(classDefinition)
 *    classDefinition: This is the ClassDefinition of which the instance is being created.
 *      The class definition is like a blueprint for the instance to be created.
 *
 * */
class ClassInstance (classDefinition: ClassDefinition){


  /**
   * instanceBindings
   *
   * Contains the active bindings for the instance.
   * Maps (String->Value)
   * */
  //val instanceBindings = mutable.Map.empty[String, Value]


  /**
   * instanceAccessBindings
   *
   * Maps the names to their defined access specifiers.
   * Maps (String->accessSpecifier)
   *
   * Note: accessSpecifier is defined in SetLangDSL.DSL
   * */
  //val instanceAccessBindings = mutable.Map.empty[String, accessSpecifier]


  /**
   * constructorParameters
   *
   * Contains the names of the constructors parameters
   * */
  //val constructorParameters: ArrayBuffer[String] = new ArrayBuffer[String]


  /**
   * We must load the bindings and constructor parameters from the class
   *  definition into the class instance. before the instance is used to
   *  access fields and methods.
   * */
//  // Load the bindings
//  classDefinition.bindingMap.foreach((s,v)=>{
//      instanceBindings += (s->v)
//    })
//
//  // Load the map of access specifiers
//  classDefinition.accessBindingMap.foreach((s,a)=>{
//    instanceAccessBindings += (s->a)
//  })
//
//  // Load the constructor parameters
//  classDefinition.getConstructorParameters.foreach(s=>constructorParameters.addOne(s))


  /**
   * getDefinition
   *
   * Returns the ClassDefinition instance used by the instance to create the object.
   * TODO: Remove this later, as it is NOT SAFE.
   * */
  def getDefinition: ClassDefinition = classDefinition


  /**
   * getField
   *  parameters:
   *    name: String name of the class field to be accessed.
   *
   * Used to access class fields
   * Note: This will be wrapped by the getField method of the class Value.
   *        The user would not call this manually.
   * */
  def getField(name: String): Value = {
    if classDefinition.bindingMap.contains(name) then
      if classDefinition.accessBindingMap(name) == Public then
        classDefinition.bindingMap(name)
      else
        //TODO: Throw exception: Illegal access to private/protected field
        null
    else
      //TODO: Throw exception: Field with name does not exist
      null
  }


  /**
   * getMethod
   *  parameters:
   *    name: String name of the class method to be accessed.
   *
   * Used to access class methods.
   * Note: This will be wrapped by the getMethod method of the class Value.
   *        The user will not call this manually.
   * */
  def getMethod(name: String): MethodContext = {
    if classDefinition.bindingMap.contains(name) && classDefinition.accessBindingMap(name) == Public then
      println("getMethod: Found method definition")
      if classDefinition.bindingMap(name).checkIfTypeMethodDefinition then
        println("getMethod: Of type methodDefinition")
        val methodDefinition = classDefinition.bindingMap(name).getValue.asInstanceOf[MethodDefinition]
        new MethodContext(methodDefinition.deepCopy())
      else
        null
    else
      null

  }

  /**
   * withParameters
   *  parameters:
   *    value: A single value for the first parameter
   *
   * Used to pass parameters to th constructor when the object is being instantiated.
   * If number of parameters is not 1, then a runtime exception will be thrown
   * */
  def withParameters(value: Any): Unit = {
    if classDefinition.parameters.size == 1 then
      classDefinition.bindingMap += (classDefinition.parameters(0) -> Value(value))
      classDefinition.accessBindingMap += (classDefinition.parameters(0)->Public)
    else
      throw RuntimeException("Extra/Less parameters passed to class constructor")
  }

  /**
   * withParameters
   *  parameters:
   *    values: A tuple of values (upto 22) denoting the value for parameters
   *              1 to 22 for the class constructor
   *
   * Used to pass parameters to th constructor when the object is being instantiated
   *
   * If number of values do not match the number of parameters, then a runtime exception will be thrown
   * */
  def withParameters(values: Tuple): Unit = {
    val valuesAsArray = new ArrayBuffer[Any]
    values.productIterator.foreach(value=>valuesAsArray.addOne(value))
    if(classDefinition.parameters.size == valuesAsArray.size) then
      for( i <- 0 to classDefinition.parameters.size - 1){
        classDefinition.bindingMap += (classDefinition.parameters(i)->Value(valuesAsArray(i)))
        classDefinition.accessBindingMap += (classDefinition.parameters(i)->Public)
      }
    else
      throw RuntimeException("Extra/Less parameters passed to class constructor")
  }
}
