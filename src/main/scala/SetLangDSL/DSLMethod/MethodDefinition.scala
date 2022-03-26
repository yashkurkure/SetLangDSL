package SetLangDSL.DSLMethod
/*
 *
 *  Copyright (c) 2022. Yash Kurkure. All rights reserved.
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

import SetLangDSL.DSLScope.{ScopeDefinition, ScopeBinding}
import SetLangDSL.DSL.*

class MethodDefinition(parent: ScopeDefinition,
                       access: accessSpecifier, // access specifier for the method
                       name: String, //name of the method
                       parameters: Parameters, //parameters of the method
                       body: MethodDefinition=>Value //method definition
                      ) extends ScopeDefinition(parent){

  /**
   * Auxiliary Private Constructor
   * 
   * Used for deep copying 
   * */
  private def this(parent: ScopeDefinition,
                   access: accessSpecifier,
                   name: String,
                   parameters: Parameters,
                  body: MethodDefinition=>Value,
                   methodDefinition: MethodDefinition) = {
    
    this(parent, access, name, parameters, body)
    methodDefinition.bindingMap.foreach((k,v)=>this.bindingMap.put(k,v))
  }
  
  
  //override val bindings: MethodBindings = new MethodBindings(this)

  def getParameters: Parameters = parameters
  def getAccessSpecifier: accessSpecifier = access
  def getBody: MethodDefinition=>Value = body

  //override def Assign: MethodBindings = bindings

  // Experimental
  override def AssignVariable(name: String): MethodBinding = {
    // The variable should be already be bound or
    // The variable should not be a parameter to the method
    if(bindingMap.contains(name) || parameters.parameters.contains(name)) then
    //println("Binding found for name: " + name)
      new MethodBinding(name, bindingMap, bindingMap(name))
    else
    //println("Creating incomplete binding for: " + name)
      new MethodBinding(name, bindingMap)
  }
  
  override def deepCopy(): MethodDefinition = {
    new MethodDefinition(parent, access, name, parameters, body, this)
  }
  
  def isAbstract(): Boolean = if (this.body == null && this.parent == null) true else false

}
