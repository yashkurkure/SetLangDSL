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

import SetLangDSL.DSLScope.{ScopeBindings, ScopeDefinition}
import SetLangDSL.DSL.*

class MethodDefinition(parent: ScopeDefinition,
                       access: accessSpecifier, // access specifier for the method
                       name: String, //name of the method
                       parameters: Parameters, //parameters of the method
                       body: MethodDefinition=>Value //method definition
                      ) extends ScopeDefinition(parent){

  override val bindings: MethodBindings = new MethodBindings(this)

  def getParameters: Parameters = parameters
  def getAccessSpecifier: accessSpecifier = access
  def getBody: MethodDefinition=>Value = body

  override def Assign: MethodBindings = bindings

}
