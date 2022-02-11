import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*
import collection.mutable.Set

object Main {


  @main def main = {
    //Create a set and bind it to a name
    Assign(Variable("testSet"), Insert(1,2,3,4,5,6,7,8,9)).eval()

    println(Variable("testSet").eval().getValue())

    //Delete the value 1 from the set
    Assign(Variable("testSet"), Delete(Value(1))).eval()

    println(Variable("testSet").eval().getValue())
    
  }

}
