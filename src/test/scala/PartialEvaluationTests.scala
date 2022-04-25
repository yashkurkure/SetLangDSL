import SetLangDSL.DSL.Scope
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable

class PartialEvaluationTests extends AnyFlatSpec with Matchers{
  behavior of "Partially Evaluated Expressions"

  /**
   * Test 1
   *
   * Creating a simple partially evaluated statement
   *
   * */
  it should "create a partially evaluated expression" in {

    Scope{g=>
      val f = g.PartialScope{p=>
        p.AssignVariable("x").Insert(1,2,3)
        p.AssignVariable("z").Union(p.Variable("x"), p.Variable("y"))
      }

      g.AssignVariable("result").toValue(f.evaluate{p=>
        p.AssignVariable("y").Insert(4,5,6)
      })

      //Test
      g.Variable("result").getValue shouldBe Set[Any](1,2,3,4,5,6)
    }

  }


}
