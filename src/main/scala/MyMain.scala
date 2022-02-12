import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*
import collection.mutable.Set
/*
 *  Copyright (c) 2022. Yash Kurkure. All rights reserved.
 */
object MyMain {


  @main def main():Unit = {

    Assign(Variable("var"), Value("a")).eval()
    Assign(Variable("someSetName"), Insert("a","b","c")).eval()
    Macro("someName", Delete(Variable("var"))).eval()
    Assign(Variable("someSetName"), Macro("someName")).eval()

    println(Variable("someSetName").eval().getValue())
  }

}
