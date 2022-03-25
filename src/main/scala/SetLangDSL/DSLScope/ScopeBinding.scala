/*
 *
 *  Copyright (c) 2022. Yash Kurkure. All rights reserved.
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */
package SetLangDSL.DSLScope

//Scala Imports
import scala.collection.mutable

//DSL Imports
import SetLangDSL.DSL.Value
import SetLangDSL.DSLClass.{ClassDefinition, ClassInstance}
import SetLangDSL.DSLMethod.{MethodContext, MethodDefinition}
import SetLangDSL.DSLSetOperations.*
import SetLangDSL.DSL.*


/**
 * ScopeIncompleteBinding
 *
 * This class represents an incomplete binding.
 *  Incomplete Binding is an instance of this class, which you can use
 *  to assign a name to a value using the methods of this class.
 *  For example: someIncompleteBinding.toValue(1) will assign the "name" field
 *    in the class to the value 1.
 *
 * The instance of this class is also used to operate on set Variables and create bindings to store te results
 *
 * Constructors:
 *  ScopeIncompleteBinding(name, bindingMap, value = null)
 *    parameters:
 *      name: The name of the binding
 *      bindingMap: The map with all bindings
 *      value: if the binding already exists this will be set to the value of the binding, else null
 * */
class ScopeBinding(name: String,
                   bindingMap: mutable.Map[String, Value],
                   value: Value = null) {

  /**
   * getValue
   *
   * Used to get the value of the binding. Used when a binding's
   *  value needs to be accessed.
   * */
  def getValue:Value = value


  /**
   * toValue
   *  parameters:
   *    value: The value to which the name is to be associated with
   *
   * Adds a binding to the binding map for some value.
   *
   * */
  def toValue(value: Any): Unit = {

    if value == null then return
    // Only create the binding if value is set to null
    if this.value == null then

      // To prevent cases like Value(Value("v"))
      if value.isInstanceOf[Value] then
        toValue(value.asInstanceOf[Value].getValue)
      else
        // Create the binding
        bindingMap += (name -> Value(value))
  }


  /**
   * toMacro
   *  parameters:
   *    f: A function representing the macro body
   *
   * Creates a name to macro binding
   * */
  def toMacro(f: Value=>Unit): Unit = {

    if f == null then return 
    
    //Only create the binding if the name is not already bound
    if this.value == null then
      bindingMap +=(name -> Value(f))
  }


  /**
   * NewObject
   *  parameters:
   *    className: name of the class of which we are creating an instance
   *
   *  Used for creating an instance of a defined class
   * */
  def toNewObjectOf(className: String): ClassInstance = {
    if this.value == null then
      //check if the class definition exists
      if bindingMap.contains(className) && bindingMap(className).checkIfTypeClassDefinition then
        //create an instance
        val classDefinition = bindingMap(className).getValue.asInstanceOf[ClassDefinition]
        val instance = new ClassInstance(classDefinition.deepCopy())
        //val instance = new ClassInstance(classDefinition)
        //create a binding for the instance
        println("Created a new object and added to bindings")
        bindingMap += (name->Value(instance))
        instance
      else
        println("DID NOT Created a new object and added to bindings")
        null
    else
      null
  }


//  /**
//   * Method
//   *  parameters:
//   *    name: name of the method
//   *    classInstance: instance of the class on which the method is called
//   *
//   * Used to execute some method os an instance
//   * The result of the method's execution is stored in the binding: this.name -> result
//   * */
//  def Method(name: String, classInstance: Value): MethodContext = {
//
//    // Check if classInstance is of type ClassInstance
//    if classInstance.checkIfTypeClassInstance then
//      // Get the class definition
//      val classDefinition = classInstance.getValue.asInstanceOf[ClassInstance].getDefinition
//      // Check if the name of the method is bound to a method definition
//      if classDefinition.Variable(name).checkIfTypeMethodDefinition then
//        // Get the method definition
//        val methodDefinition = classDefinition.Variable(name).getValue.asInstanceOf[MethodDefinition]
//        // Check if the method is not private
//        if(methodDefinition.getAccessSpecifier != Private)
//          // return a method context
//          new MethodContext(this, classInstance.getValue.asInstanceOf[ClassInstance], methodDefinition)
//
//        // Return null in all other cases
//        else
//          null
//      else
//        null
//    else
//      null
//  }


  /**
   * Insert
   *  parameters:
   *    value: a Value to inserted into a set
   *
   * Used to create a set, with value as a member
   * */
  def Insert(value: Any): Value = {
    
    if value == null then return null
    
    // create a set
    val set = SetInsert(value)
    // if the result is null, there was a problem
    if set == null then
      null
    else
      // create a binding
      // check if a binding already exists
      if this.value == null then
        val asInstanceOfValue = Value(set)
        bindingMap += (name -> asInstanceOfValue)
        asInstanceOfValue
      else
        // return null if the binding already exists
        null
  }


  /**
   * Insert
   *  parameters:
   *    values: a Tuple of Values to be inserted into a set
   *
   * Used to create a set, with members of the tuple as memebers of the set
   * */
  def Insert(values: Tuple): Value = {
    
    // create a set
    val set = SetInsert(values)
    // if the set is null there was a problem creating the set
    if set == null then
      null
    else
      // create a binding
      // check if a binding already exists
      if this.value == null then
        val asInstanceOfValue = Value(set)
        bindingMap += (name -> asInstanceOfValue)
        asInstanceOfValue
      else
        // return null if the binding already exists
        null
  }

  /**
   * Union
   *  parameters:
   *    set1AsValue: Value instance denoting a set
   *    set2AsValue: Value instance denoting a set
   *
   * Used to create the Union of 2 sets and create a binding for the result set
   * */
  def Union(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue == null || set2AsValue == null then return null
    val set = SetUnion(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      if this.value == null then
        val asInstanceOfValue = Value(set)
        bindingMap += (name -> asInstanceOfValue)
        asInstanceOfValue
      else
        null

  }


  /**
   * Intersection
   *  parameters:
   *    set1AsValue: Value instance denoting a set
   *    set2AsValue: Value instance denoting a set
   *
   * Used to create the intersection of 2 sets and create a binding for the result set
   * */
  def Intersection(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue == null || set2AsValue == null then return null
    val set = SetIntersection(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      if this.value == null then
        val asInstanceOfValue = Value(set)
        bindingMap += (name -> asInstanceOfValue)
        asInstanceOfValue
      else
        null
  }


  /**
   * Difference
   *  parameters:
   *    set1AsValue: Value instance denoting a set
   *    set2AsValue: Value instance denoting a set
   *
   * Used to create the difference of 2 sets and create a binding for the result set
   * */
  def Difference(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue == null || set2AsValue == null then return null
    val set = SetDifference(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      if this.value == null then
        val asInstanceOfValue = Value(set)
        bindingMap += (name -> asInstanceOfValue)
        asInstanceOfValue
      else
        null
  }


  /**
   * SymmetricDifference
   *  parameters:
   *    set1AsValue: Value instance denoting a set
   *    set2AsValue: Value instance denoting a set
   *
   * Used to create the symmetric difference of 2 sets and create a binding for the result set
   * */
  def SymmetricDifference(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue == null || set2AsValue == null then return null
    val set = SetSymmetricDifference(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      if this.value == null then
        val asInstanceOfValue = Value(set)
        bindingMap += (name -> asInstanceOfValue)
        asInstanceOfValue
      else
        null
  }


  /**
   * CartesianProduct
   *  parameters:
   *    set1AsValue: Value instance denoting a set
   *    set2AsValue: Value instance denoting a set
   *
   * Used to create the cartesian product of 2 sets and create a binding for the result set
   * */
  def CartesianProduct(set1AsValue: Value, set2AsValue: Value): Value = {
    if set1AsValue == null || set2AsValue == null then return null
    val set = SetCartesianProduct(set1AsValue, set2AsValue)
    if set == null then
      null
    else
      if this.value == null then
        val asInstanceOfValue = Value(set)
        bindingMap += (name -> asInstanceOfValue)
        asInstanceOfValue
      else
        null
  }

}
