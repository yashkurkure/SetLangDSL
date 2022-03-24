import SetLangDSL.DSL.*
import SetLangDSL._
object MyMain
{
  @main def main(): Unit = {
    println("Main method")


    Scope{g=>
      g.Assign.Variable("myMacro").toMacro{m=>
        m.Delete(1)
      }
    }


//    Scope{g=>
//      g.Assign.Variable("mySet").Insert("a","b","c")
//      g.Assign.Variable("myMacro").toMacro(v=>{
//        v.Delete("b")
//      })
//      g.ExecuteMacro("myMacro", g.Variable("mySet"))
//      println(g.Variable("mySet").getValue)
//    }

    /*// Global Scope
    Scope{g=>

      g.ClassDef("numbers", c=>{

        //Creating class variables
        c.Assign.Variable(Private, "privateNumber").toValue(1)
        c.Assign.Variable(Protected, "protectedNumber").toValue(2)
        c.Assign.Variable(Public, "publicNumber1").toValue(3)
        c.Assign.Variable("publicNumber2").toValue(4)

        //Creating class constructor
        c.Constructor{t=>

        }

        //Creating class methods
        // getConstant returns the constant 100
        c.Method(Public, "getConstant", Parameters(),m=>{
          Value(100)
        })

        //getTheNumber returns:
        // classVariable publicNumber1 if whichNumber == 1
        // classVariable publicNumber2 if whichNumber == 2
        c.Method(Public, "getTheNumber", Parameters("whichNumber"), m=>{

          // The advantage of using an hierarchy of methods and classes for the DSL
          // The user is able to use the if then else construct of scala
          if m.Variable("whichNumber").equals(Value(1)) then
            c.Variable("publicNumber1")
          else
            c.Variable("publicNumber2")
        })

      })

      //Creating a class object
      g.Assign.Variable("myObject").NewObject("numbers")

      //Executing a method of a class object
      g.Assign.Variable("result1").Method("getTheNumber", g.Variable("myObject")).withParameters(Value(2)).Execute

    }*/



    /*Scope{g=>
      //g.Assign.Variable("x").Union(g.Variable("y"), g.Variable("z"))
      g.Assign.Variable("y").Insert("a","b","c")
      g.Assign.Variable("z").Insert("d","e","f")
      g.Assign.Variable("x").Union(g.Variable("y"), g.Variable("z"))
      println(g.Variable("x").Value)
    }*/


    /*Scope{g=>{
      g.Assign.Variable("set").Insert("a","b","c","d")
      println(g.Variable("set").Value)
      println(g.Variable("set").Insert().Value)
    }}

    Scope{g=>{
      g.Assign.Variable("v").Value(3)
      println(g.Variable("v").Value)
      g.Scope(s=>{
        s.Assign.Variable("x").Value(4)
        println(s.Variable("x").Value)
        println(s.Variable("v").Value)
      })
    }}*/

    /*Scope{g=>{
      g.Assign.Variable("v").Value(3)
      g.Scope{s=>
        s.Assign.Variable("v").Value(4)
        "scope1"
      }
      println(g.Scope("scope1").Variable("v").Value)
    }}*/

    /*Scope{g=>{
      g.Assign.Variable("notASet").Value("a")
      g.Assign.Variable("someSet").Insert("a","b")
      g.Variable("someSet").Insert("b","c","d")
      println(g.Variable("notASet").Insert("b","c")) //Insert() would return null because Variable notASet is not a set
      println(g.Variable("someSet").Value)
      println(g.Variable("notASet").Value)
    }}

  }*/

/*  def scopeReference ={
    Scope{g=>
      g.Assign.Variable("v").Value(3)
      //println(s.Variable("v"))
      g Scope{s2=>
        s2.Assign.Variable("v").Value(4)
        println(s2.Variable("v"))
      }
      println(g.Variable("v"))

      g Scope{s3 =>
        s3.Assign.Variable("v").Value(87)
        println(s3.Variable("v"))
        "namedScope"}
      println(g.Scope("namedScope").Variable("v"))
    }*/
  }


}
