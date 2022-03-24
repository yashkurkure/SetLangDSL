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
import SetLangDSL.DSLMethod.MethodContext
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
  val instanceBindings = mutable.Map.empty[String, Value]


  /**
   * instanceAccessBindings
   *
   * Maps the names to their defined access specifiers.
   * Maps (String->accessSpecifier)
   *
   * Note: accessSpecifier is defined in SetLangDSL.DSL
   * */
  val instanceAccessBindings = mutable.Map.empty[String, accessSpecifier]


  /**
   * We must load the bindings from the class definition into the class instance.
   *  before the instance is used to access fields and methods.
   * */
  // Load the bindings
  classDefinition.bindings.bindingMap.foreach((s,v)=>{
      instanceBindings += (s->v)
    })

  // Load the map of access specifiers
  classDefinition.bindings.accessBindingMap.foreach((s,a)=>{
    instanceAccessBindings += (s->a)
  })


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
    if instanceBindings.contains(name) then
      if instanceAccessBindings(name) == Public then
        instanceBindings(name)
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
    if instanceBindings.contains(name) && instanceAccessBindings(name) == Public then
      if instanceBindings(name).checkIfTypeMethodContext then
        instanceBindings(name).asInstanceOf[MethodContext]
      else
        null
    else
      null

  }

  /**
   * withParameters
   *
   * TODO: Inspect working
   * */
  def withParameters(value: Any): Unit = {
    val parameters = classDefinition.parameters
    instanceBindings += (parameters(0) -> Value(value))
  }

  /**
   * withParameters
   *
   * TODO: Inspect working
   * */
  def withParameters(values: Tuple): Unit = {
    val valuesAsArray = new ArrayBuffer[Any]
    values.productIterator.foreach(value=>valuesAsArray.addOne(value))
    val parameters = classDefinition.parameters
    if(parameters.size == valuesAsArray.size) then
      for( i <- 0 to parameters.size){
        instanceBindings += (parameters(i)->Value(valuesAsArray(i)))
      }
  }
}
