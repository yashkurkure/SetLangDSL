package SetLangDSL.DSLScope

//Scala imports
import SetLangDSL.Skeleton.{Bindings, Context, IncompleteBinding}
import SetLangDSL.Value
import scala.collection.mutable

class ScopeBindings(context: ScopeContext)
  extends Bindings[ScopeBindings](context)
{

  val bindingMap: mutable.Map[String, Value] = mutable.Map.empty[String, Value]

  def Variable(name: String): ScopeIncompleteBinding = {
    null
  }

  def Scope(name: String): ScopeIncompleteBinding = {
    null
  }

}
