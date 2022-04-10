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

  it should "create an interface that implements an interface" in{

    Scope{g=>

      g.InterfaceDef("interface1", {i=>

        i.AssignVariable("x")

        i.Method(Public, "implementMe", Parameters())

      })

      g.InterfaceDef("interface2", Implements("interface1"), i=>{

        i.AssignVariable("y")

        i.Method(Public, "implementMe2", Parameters())

      })

      g.ClassDef("implemented", Implements("interface2"), c=>{

        // These were a part of interface 1 that interface 2 inherited
        c.AssignVariable("x").toValue(1)
        c.Method(Public, "implementMe", Parameters(), {m=>
          Value(1)
        })

        // These were the additional ones defined in interface 2, after it implemented interface1
        c.AssignVariable("y").toValue(2)
        c.Method(Public, "implementMe2", Parameters(), {m=>
          Value(2)
        })


      })

      // Create an instance of the class
      g.AssignVariable("obj").toNewObjectOf("implemented")

      g.Variable("obj").getField("x").getValue shouldBe 1
      g.Variable("obj").getMethod("implementMe").Execute.getValue shouldBe 1
      g.Variable("obj").getField("y").getValue shouldBe 2
      g.Variable("obj").getMethod("implementMe2").Execute.getValue shouldBe 2
    }
  }

}
