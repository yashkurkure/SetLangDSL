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


/**
 * ScopeBindings
 *
 * This class represents the bindings of a certain scope.
 *  Each ScopeDefinition instance holds one instance of this class.
 *
 * Constructors:
 *  ScopeBindings(scope: ScopeDefinition)
 *    scope: The instance of the scope definition class associated with the bindings
 * */
class ScopeBindings(scope: ScopeDefinition) {

  
  private def this(scopeBindings: ScopeBindings, scope: ScopeDefinition) = {
    this(scope)
    scopeBindings.bindingMap.foreach((s,v)=>this.bindingMap.put(s,v))
  }
  
  
  /**
   * bindingMap
   *
   * The map that holds the bindings for the scope
   * */
  val bindingMap: mutable.Map[String, Value] = mutable.Map.empty[String, Value]


  /**
   * Variable
   *  parameters:
   *    name: The name of the variable
   *
   * Used to create a new binding
   *
   * Returns an instance of ScopeIncompleteBinding
   *  on this instance the user can call toValue(), toMacro(), insert()
   *  to create a binding to a value or a macro or a set.
   *
   *  If the binding is not present the value field in the
   *    ScopeIncompleteBinding instance is null.
   *    Think of this null value as the initial value of the
   *    binding.
   * */
  def Variable(name: String): ScopeIncompleteBinding = {

    // Search the biding in the map
    if(bindingMap.contains(name)) then
      // return an incomplete binding with the value if the binding exists
      new ScopeIncompleteBinding(name, bindingMap, bindingMap(name))
    else
      // return an incomplete binding with value set to null as the binding
      //    does not exist (A name->null binding)
      new ScopeIncompleteBinding(name, bindingMap)
  }
  
  def deepCopy(): ScopeBindings = {
    new ScopeBindings(this, scope)
  }

}
