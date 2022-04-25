import SetLangDSL.DSL.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable

class MapTests extends AnyFlatSpec with Matchers{
  behavior of "Function Map"


  it should "Map a set of sets to a values" in {

    Scope{g=>
      g.AssignVariable("set1").Insert(1,2,3,4)

      g.AssignVariable("set2").toValue(g.Variable("set1").Map{v=>Value(v.getValue.asInstanceOf[Int]+1)})

      g.Variable("set2").getValue shouldBe mutable.Set[Any](2,3,4,5)
    }

  }
}
