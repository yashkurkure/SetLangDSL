import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable
import SetLangDSL.DSL.*

class InterfaceTests extends AnyFlatSpec with Matchers {
  behavior of "Interfaces"

  it should "implement an interface on a class" in {

    Scope{g=>

      g.InterfaceDef("interface", {i=>

        i.AssignVariable("x")

        i.Method(Public, "implementMe", Parameters())

      })

      g.ClassDef("class", Implements("interface"), {c=>

        // You can try commenting out any of the below
        // If something is not implement it will throw an exception
        c.AssignVariable("x").toValue(1)
        c.Method(Public, "implementMe", Parameters(), {m=>
          Value(1)
        })
      })

      // Create an instance of the class
      g.AssignVariable("obj").toNewObjectOf("class")
      g.Variable("obj").getField("x").getValue shouldBe 1
      g.Variable("obj").getMethod("implementMe").Execute.getValue shouldBe 1
    }
  }

}
