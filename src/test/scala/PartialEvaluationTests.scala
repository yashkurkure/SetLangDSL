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
   * Only 1 undefined variable
   *
   * */
  it should "create a partially evaluated expression" in {

    Scope{g=>

      // Create the Scope for partial evaluation
      val f = g.PartialScope{p=>
        p.AssignVariable("x").Insert(1,2,3)
        p.AssignVariable("z").Union(p.Variable("x"), p.Variable("y"))
      }

      // Assign and evaluate the partially evaluated scope by
      // assigning the undefined variables and calling evaluate
      g.AssignVariable("result").toValue(f.evaluate{p=>
        p.AssignVariable("y").Insert(4,5,6)
      })

      //Test
      g.Variable("result").getValue shouldBe Set[Any](1,2,3,4,5,6)
    }

  }

  it should "partially evaluated expression evaluated twice" in {

    Scope{g=>

      // Create the Scope for partial evaluation
      val f = g.PartialScope{p=>
        p.AssignVariable("x").Insert(1,2,3)
        p.AssignVariable("z").Union(p.Variable("x"), p.Variable("y"))
        p.AssignVariable("a").Union(p.Variable("z"), p.Variable("b"))
      }

      // Assign and evaluate the partially evaluated scope by
      // assigning the undefined variables and calling evaluate
      g.AssignVariable("result1").toValue(f.evaluate{p=>
        p.AssignVariable("y").Insert(4,5,6)
      })

      g.AssignVariable("result2").toValue(f.evaluate{p=>
        p.AssignVariable("b").Insert(7,8,9)
      })

      //Test
      g.Variable("result1") shouldBe null
      g.Variable("result2").getValue shouldBe mutable.Set[Any](1,2,3,4,5,6,7,8,9)
    }

  }


}
